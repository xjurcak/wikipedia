package categoryanalyzer.index;

import com.google.gson.Gson;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Create lucene index for topics. Index fields names are:
 *  id
 *  title
 *  types
 */
public class TopicIndexer {

    public enum TopicField{
        ID("id"),
        TITLE("title"),
        TYPES("types");
        private String name;

        public String getName() {
            return name;
        }

        TopicField(String name) {

            this.name = name;
        }
    }

    private static Gson gson = new Gson();

    /**
     * Create index from stream of topics to indexFile directory
     * @param reader stream of topics
     * @param indexDiR directory to store index
     */
    public static void index(TopicReader reader, File indexDiR) {
        index(reader, indexDiR, false);
    }

    private static void index(TopicReader reader, File indexDir, boolean update) {
        try {
            Topic topic;
            IndexWriter iw = createIndexWriter(indexDir, update);

            int i = 0;

            //iterate stream of topics
            while ((topic = reader.nextTopic()) != null) {
                i++;
                //if topic is valid write it to index
                if (topic.getId() != null && (topic.getTitle() != null || topic.getTypes() != null)) {

                    indexTopic(iw, topic);
                    if (i % 1000 == 0) {
                        System.out.print("\rTopics innndexed count: " + i);
                    }
                }
            }
            iw.close();
            System.out.println("\rTopics indexed finished total count: " + i);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Construct lucene IndexWriter
     * @param indexPath
     * @param update
     * @return
     * @throws IOException
     */
    private static IndexWriter createIndexWriter(File indexPath, boolean update) throws IOException {
        System.out.println("Indexing to directory '" + indexPath + "'...");

        Directory dir = FSDirectory.open(indexPath);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, analyzer);

        // Create a new index in the directory, removing any
        // previously indexed documents:
        if(update)
            iwc.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
        else
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        return new IndexWriter(dir, iwc);
    }

    /**
     * Indexes a single topic
     */
    static void indexTopic(IndexWriter writer, Topic topic) throws IOException {

        // make a new, empty document
        Document doc = new Document();

        Field pathField = new StringField(TopicField.ID.getName(), topic.getId(), Field.Store.YES);
        doc.add(pathField);

        if (topic.getTitle() != null)
            doc.add(new StringField(TopicField.TITLE.getName(), topic.getTitle(), Field.Store.YES));

        if (topic.getTypes() != null) {
            //create json from types
            String json = gson.toJson(topic.getTypes());
            doc.add(new StringField(TopicField.TYPES.getName(), json, Field.Store.YES));
        }

        if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
            // New index, so we just add the document (no old document can be there):
            writer.addDocument(doc);
        }
    }
}
