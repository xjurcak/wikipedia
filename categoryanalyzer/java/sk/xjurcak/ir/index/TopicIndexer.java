package sk.xjurcak.ir.index;

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
 * Created by xjurcak on 10/31/2014.
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

    public static void index(TopicReader reader, File indexFile) {
        index(reader, indexFile, false);
    }

    private static void index(TopicReader reader, File indexDir, boolean update) {
        try {
            Topic topic;
            IndexWriter iw = createIndexWriter(indexDir, update);

            int i = 0;

            while ((topic = reader.nextTopic()) != null) {
                i++;
                if (topic.getId() != null && (topic.getTitle() != null || topic.getTypes() != null)) {
                    indexTopic(iw, topic);
                    if (i % 1000 == 0) {
                        System.out.println("Topics indexed count: " + i);
                    }
                }
            }
            iw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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

        // Optional: for better indexing performance, if you
        // are indexing many documents, increase the RAM
        // buffer.  But if you do this, increase the max heap
        // size to the JVM (eg add -Xmx512m or -Xmx1g):
        //
        // iwc.setRAMBufferSizeMB(256.0);

        return new IndexWriter(dir, iwc);
    }

    /**
     * Indexes a single document
     */
    static void indexTopic(IndexWriter writer, Topic topic) throws IOException {

        // make a new, empty document
        Document doc = new Document();

        Field pathField = new StringField(TopicField.ID.getName(), topic.getId(), Field.Store.YES);
        doc.add(pathField);

        if (topic.getTitle() != null)
            doc.add(new StringField(TopicField.TITLE.getName(), topic.getTitle(), Field.Store.YES));

        if (topic.getTypes() != null) {
            doc.add(new StringField(TopicField.TYPES.getName(), topic.getTypes(), Field.Store.YES));
        }

        if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
            // New index, so we just add the document (no old document can be there):
            writer.addDocument(doc);
        } else {
//            // Existing index (an old copy of this document may have been indexed) so
//            // we use updateDocument instead to replace the old one matching the exact
//            // path, if present:
//            if (topic.getTitle() != null)
//                writer.updateDocValues(new Term("id", topic.getId()), new TextField("title", topic.getTitle(), Field.Store.YES));
//
//            if (topic.getTypes() != null) {
//                writer.updateBinaryDocValue(new Term("id", topic.getId()), "types", new BytesRef(topic.getTypes().getBytes()));
//            }
        }
    }
}
