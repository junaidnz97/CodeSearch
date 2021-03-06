package com.howtodoinjava.demo.lucene.file;
 
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.uhighlight.UnifiedHighlighter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
 
public class LuceneReadIndexFromFileExample
{
    private static final String INDEX_DIR = "indexedFiles";
 
    public  void LuceneReadIndexFromFileExamplemain(String texttofind) throws Exception
    {
        IndexSearcher searcher = createSearcher();


        TopDocs foundDocs = searchInContent(texttofind, searcher);
        QueryParser qp = new QueryParser("contents", new StandardAnalyzer());
        Query query = qp.parse(texttofind);

        System.out.println("Total Results :: " + foundDocs.totalHits);

        Analyzer analyzer=new StandardAnalyzer();


        UnifiedHighlighter highlighter = new UnifiedHighlighter(searcher, analyzer);
        String[] fragments = highlighter.highlight("contents", query, foundDocs);

        for(String f : fragments)
        {
            System.out.println("%%NEW CODE%%");
            System.out.println(f);
            System.out.println("%%END%%");

        }

        for (ScoreDoc sd : foundDocs.scoreDocs)
        {
            Document d = searcher.doc(sd.doc);
            System.out.println("Path : "+ d.get("path") + ", Score : " + sd.score);
        }
    }
     
    private static TopDocs searchInContent(String textToFind, IndexSearcher searcher) throws Exception
    {
        QueryParser qp = new QueryParser("contents", new StandardAnalyzer());
        Query query = qp.parse(textToFind);
         
        TopDocs hits = searcher.search(query, 10);
        return hits;
    }
 
    private static IndexSearcher createSearcher() throws IOException
    {
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
         
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }
}