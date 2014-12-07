package categoryanalyzer.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by xjurcak on 10/31/2014.
 */
public class CategoriesSearcher {

    /**
     * Search for categories in lucene index. Id of category is specified by id parameter.
     * @param indexDir lucene index directory.
     * @param id Category id.
     * @return
     * @throws ParseException
     */
    public static Category[] search(File indexDir, String id) throws ParseException {
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir));
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();

            Query query = new TermQuery(new Term("id", id));

            //System.out.println("Searching for: " + query.toString());

            // Collect enough docs to show 5 pages
            TopDocs results = searcher.search(query, 5 * 10);
            ScoreDoc[] hits = results.scoreDocs;

            Category[] categories = new Category[hits.length];

            int i = 0;
            for(ScoreDoc doc : hits){
                Document storedDocument = searcher.doc(doc.doc);
                categories[i] = new Category(storedDocument.get("id"), storedDocument.get("title"));
            }

            int numTotalHits = results.totalHits;
            //System.out.println(numTotalHits + " total matching documents");

            reader.close();

            return categories;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Category[0];
    }
}
