package com.howtodoinjava.demo.lucene.file;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.uhighlight.UnifiedHighlighter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;


public class Wildcardquery {

        private static final String INDEX_DIR = "indexedFiles";

        public  void Wildcardquerymain(String texttofind) throws Exception
        {
            Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));

            IndexReader reader = DirectoryReader.open(dir);

            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new StandardAnalyzer();




            Query query = new WildcardQuery(new Term("contents", texttofind));


            TopDocs hits=searcher.search(query,22);
            System.out.println("Search terms found in :: " + hits.totalHits + " files");


            UnifiedHighlighter highlighter = new UnifiedHighlighter(searcher, analyzer);
            String[] fragments = highlighter.highlight("contents", query, hits);

            for(String f : fragments)
            {
                System.out.println("%%NEW CODE%%");
                System.out.println(f);
                System.out.println("%%END%%");

            }

            for(ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                System.out.println("File: "+ doc.get("path")+" with score= " +scoreDoc.score);

            }


        }


}





