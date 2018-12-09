package com.howtodoinjava.demo.lucene.file;

public class mainclass {

    public static void main(String[] args)
    {
        String query="search";

        String wq="*search";
        LuceneWriteIndexFromFileExample luceneWriteIndexFromFileExample=new LuceneWriteIndexFromFileExample();
        luceneWriteIndexFromFileExample.LuceneWriteIndexFromFileExamplemain();

        LuceneReadIndexFromFileExample luceneReadIndexFromFileExample=new LuceneReadIndexFromFileExample();

        try {
            luceneReadIndexFromFileExample.LuceneReadIndexFromFileExamplemain(query);
        }
        catch (Exception e)
        {
            ;
        }

        Wildcardquery wildcardquery=new Wildcardquery();

        try {
            wildcardquery.Wildcardquerymain(wq);
        }
        catch (Exception e)
        {
            ;
        }

        System.out.println();
        System.out.println("NGRAM INDEXING");
        System.out.println();

        LuceneNGramWrite luceneNGramWrite=new LuceneNGramWrite();
        luceneNGramWrite.LuceneNGramWritemain();

        LuceneNGramRead luceneNGramRead=new LuceneNGramRead();
        try {

            luceneNGramRead.LuceneNGramReadmain(query);

        }
        catch (Exception e){
            ;
        }

    }
}
