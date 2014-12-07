package categoryanalyzer.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;

/**
 * Create lucene index for categories. Index fields names are:
 *  id
 *  title
 */
public class CategoryIndexer {

    /**
     * Create index from stream of categories to indexDir directory
     * @param reader stream of topics
     * @param indexDir directory to store index
     */
    public static void index(CategoryReader reader, File indexDir) {
        try {
            Category content;
            IndexWriter iw = createIndexWriter(indexDir);

            int i = 0;

            //iterate through stream
            while((content = reader.nextCategory()) != null){

                //if category valid index it
                if(content.getId() != null && content.getTitle() != null) {
                    i++;
                    indexCategory(iw, content.getId(), content.getTitle());
                    if(i%1000 == 0)
                        System.out.print("\rCategory indexed count: " + i);
                }
            }

            iw.close();
            System.out.println("\rCategory indexed finished total count: " + i);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static IndexWriter createIndexWriter(File indexPath) throws IOException {
        System.out.println("Indexing to directory '" + indexPath + "'...");

        Directory dir = FSDirectory.open(indexPath);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, analyzer);

        // Create a new index in the directory, removing any
        // previously indexed documents:
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        return new IndexWriter(dir, iwc);
    }

    /** Indexes a single category */
    static void indexCategory(IndexWriter writer, String id, String title) throws IOException {

            // make a new, empty document
            Document doc = new Document();

            Field pathField = new StringField("id", id, Field.Store.YES);
            doc.add(pathField);

            doc.add(new TextField("title", title, Field.Store.YES));

            if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                // New index, so we just add the document (no old document can be there):
                //System.out.println("adding id: " + id + " title: " + title);
                writer.addDocument(doc);
            } else {
                // Existing index (an old copy of this document may have been indexed) so
                // we use updateDocument instead to replace the old one matching the exact
                // path, if present:
                System.out.println("updating " + id);
                writer.updateDocument(new Term("id", id), doc);
            }
    }
}
