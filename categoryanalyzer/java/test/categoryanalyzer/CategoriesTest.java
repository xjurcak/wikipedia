package categoryanalyzer;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.util.LuceneTestCase;
import categoryanalyzer.index.*;

import java.io.File;

/**
 * Created by xjurcak on 10/13/2014.
 */
public class CategoriesTest extends LuceneTestCase {

    public static File indexDir = new File("indexes");



    public void testFreebaseSearch() throws ParseException {
        File indexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);
        Category[] categories = CategoriesSearcher.search(indexFile, "base.birdconservation.topic");
        assertEquals(categories.length, 1);
        assertEquals(categories[0].getId(), "base.birdconservation.topic");
        assertEquals(categories[0].getTitle(), "Topic");

    }

    public void testDBPediaSearch() throws ParseException {
        File indexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
        Category[] categories = CategoriesSearcher.search(indexFile, "Isaac Newton");
        assertEquals(1, categories.length);
        assertEquals(categories[0].getId(), "connecticut_lawyers");
        assertEquals(categories[0].getTitle(), "Connecticut lawyers");

    }

    public void testFreebaseTopicsSearch() throws ParseException {
        File indexFile = IndexPaths.createTopicPath(indexDir, IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);
        File categoriesIndexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);

        Topic[] topics = TopicSearcher.search(indexFile, TopicIndexer.TopicField.TITLE, "Pretty Green Onions");
        assertTrue(topics.length > 0);
        assertEquals(topics[0].getId(), "m.01008xk_");
        assertEquals(topics[0].getTitle(), "Pretty Green Onions");

        for(Topic topic : topics){
            for(String id : topic.getTypesId()){
                assertEquals(CategoriesSearcher.search(categoriesIndexFile, id).length, 1);
            }
        }

    }

    public void testDBPediaTopicsSearch() throws ParseException {
        File topicLabelPath = IndexPaths.createTopicLabelPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
        File categoriesIndexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
        File indexFile = IndexPaths.createTopicArticleCategoriesPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);

        Topic[] topics = TopicSearcher.search(topicLabelPath, TopicIndexer.TopicField.TITLE, "Isaac Newton");
        assertTrue(topics.length > 0);

        for(Topic topic : topics){
            Topic[] topicTypes = TopicSearcher.search(indexFile, TopicIndexer.TopicField.ID, topic.getId());
            if(topicTypes.length > 0) {
                assertTrue(topicTypes.length == 1);

                for (String id : topicTypes[0].getTypesId()) {
                    Category[] categories = CategoriesSearcher.search(categoriesIndexFile, id);
                    assertEquals(categories.length, 1);
                }
            }
        }

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        assertTrue(indexDir.isDirectory());
    }
}
