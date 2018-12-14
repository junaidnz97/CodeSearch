package com.howtodoinjava.demo.lucene.file;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class LuceneNGramWrite {
    static int MIN_N_GRAMS = 3;
    static int MAX_N_GRAMS = 5;

    public  void LuceneNGramWritemain()
    {
        String docsPath = "inputFiles";

        String indexPath = "indexedFiles";


        final Path docDir = Paths.get(docsPath);

        try
        {
            Directory dir = FSDirectory.open( Paths.get(indexPath) );


            Analyzer analyzer = new Analyzer() {
                @Override
                protected TokenStreamComponents createComponents(String s) {
                    Tokenizer source = new NGramTokenizer(MIN_N_GRAMS, MAX_N_GRAMS);
                    TokenStream firstfilter = new LowerCaseFilter(source);
                    TokenStream filter = new SnowballFilter(firstfilter, "English");
                    return new TokenStreamComponents(source, filter);

                }
            };

            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

            IndexWriter writer = new IndexWriter(dir, iwc);

            indexDocs(writer, docDir);

            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    static void indexDocs(final IndexWriter writer, Path path) throws IOException
    {
        if (Files.isDirectory(path))
        {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                {
                    try
                    {
                        indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
                    }
                    catch (IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        else
        {
            indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }

    static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException
    {


        try (InputStream stream = Files.newInputStream(file))
        {
            Document doc = new Document();
            doc.add(new StringField("path", file.toString(), Field.Store.YES));
            doc.add(new LongPoint("modified", lastModified));
            doc.add(new TextField("contents", new String(Files.readAllBytes(file)), Field.Store.YES));


            writer.updateDocument(new Term("path", file.toString()), doc);
        }
    }
}
