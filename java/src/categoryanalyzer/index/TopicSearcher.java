package categoryanalyzer.index;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by xjurcak on 10/31/2014.
 */
public class TopicSearcher {

    /**
     * Search for topics in lucene index. Name of topic is specified by queryString parameter.
     * @param indexDir lucene index directory.
     * @param topicField field for query
     * @param queryString name of topic or article
     * @return
     * @throws ParseException
     */
    public static Topic[] search(File indexDir, TopicIndexer.TopicField topicField, String queryString) throws ParseException {
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir));
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();

            //build query for searcher
            QueryBuilder parser = new QueryBuilder(analyzer);
            Query query;
            if(topicField == TopicIndexer.TopicField.ID)
                query = new TermQuery(new Term(topicField.getName(), queryString));
            else
                query = new TermQuery(new Term(topicField.getName(), queryString));

            // Search for documents
            TopDocs results = searcher.search(query, 5 * 10);
            ScoreDoc[] hits = results.scoreDocs;

            Topic[] categories = new Topic[hits.length];

            int i = 0;
            Gson gson = new Gson();
            for(ScoreDoc doc : hits){

                //Create topic from lucene document
                Document storedDocument = searcher.doc(doc.doc);

                //get types ids from field. Types are stored as string list in json format.
                String types = storedDocument.get(TopicIndexer.TopicField.TYPES.getName());
                List<String> typesArray = null;
                if(types != null){
                    typesArray = gson.fromJson(types, new TypeToken<List<String>>(){}.getType());
                }

                categories[i] = new Topic(storedDocument.get(TopicIndexer.TopicField.ID.getName()), storedDocument.get(TopicIndexer.TopicField.TITLE.getName()), typesArray);
                i++;
            }

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
