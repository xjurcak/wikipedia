package sk.xjurcak.ir.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.xml.builders.TermQueryBuilder;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by xjurcak on 10/31/2014.
 */
public class TopicSearcher {

    public static Topic[] search(File indexDir, String queryString) throws ParseException {
        return search(indexDir, TopicIndexer.TopicField.ID, queryString);
    }

    public static Topic[] search(File indexDir, TopicIndexer.TopicField topicField, String queryString) throws ParseException {
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir));
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();

            QueryBuilder parser = new QueryBuilder(analyzer);
            Query query;
            if(topicField == TopicIndexer.TopicField.ID)
                query = new TermQuery(new Term(topicField.getName(), queryString));
            else
                query = parser.createBooleanQuery(topicField.getName(), queryString);

            System.out.println("Searching for: " + query.toString());

            // Collect enough docs to show 5 pages
            TopDocs results = searcher.search(query, 5 * 10);
            ScoreDoc[] hits = results.scoreDocs;

            Topic[] categories = new Topic[hits.length];

            int i = 0;
            for(ScoreDoc doc : hits){
                Document storedDocument = searcher.doc(doc.doc);
                categories[i] = new Topic(storedDocument.get(TopicIndexer.TopicField.ID.getName()), storedDocument.get(TopicIndexer.TopicField.TITLE.getName()), storedDocument.get(TopicIndexer.TopicField.TYPES.getName()));
                i++;
            }

            int numTotalHits = results.totalHits;
            System.out.println(numTotalHits + " total matching documents");

            reader.close();

            return categories;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Topic[0];
    }
}
