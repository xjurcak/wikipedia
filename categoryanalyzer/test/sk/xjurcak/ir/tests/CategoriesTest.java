package sk.xjurcak.ir.tests;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.util.LuceneTestCase;
import sk.xjurcak.ir.index.*;

import java.io.File;

/**
 * Created by xjurcak on 10/13/2014.
 */
public class CategoriesTest extends LuceneTestCase {

    public static File indexDir = new File("indexes");



    public void testFreebaseSearch() throws ParseException {
        File indexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);
        Content[] categories = CategoriesSearcher.search(indexFile, "Connecticut");
        assertEquals(categories.length, 1);
        assertEquals(categories[0].getId(), "m.06nsh7j");
        assertEquals(categories[0].getTitle(), "An address to the freemen of Connecticut");

    }

    public void testDBPediaSearch() throws ParseException {
        File indexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
        Content[] categories = CategoriesSearcher.search(indexFile, "Connecticut");
        assertEquals(categories.length, 50);
        assertEquals(categories[0].getId(), "connecticut_lawyers");
        assertEquals(categories[0].getTitle(), "Connecticut lawyers");

    }

    public void testFreebaseTopicsSearch() throws ParseException {
        File indexFile = IndexPaths.createTopicPath(indexDir, IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);
        File categoriesIndexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);

        Topic[] topics = TopicSearcher.search(indexFile, TopicIndexer.TopicField.TITLE, "Isaac Newton");
        assertTrue(topics.length > 0);
        assertEquals(topics[0].getId(), "m.04vmxdn");
        assertEquals(topics[0].getTitle(), "Isaac Newton");

        for(Topic topic : topics){
            String[] split = topic.getTypesId();
            for(String id : split){
                assertEquals(CategoriesSearcher.search(categoriesIndexFile, id).length, 1);
            }
        }

    }

    public void testDBPediaTopicsSearch() throws ParseException {
        File topicLabelPath = IndexPaths.createTopicLabelPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
        File categoriesIndexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
        File indexFile = IndexPaths.createTopicArticleCategoriesPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);

        Topic[] topics = TopicSearcher.search(topicLabelPath, TopicIndexer.TopicField.TITLE, "\"Isaac Newton\"");
        assertTrue(topics.length > 0);

        for(Topic topic : topics){
            Topic[] topicTypes = TopicSearcher.search(indexFile, TopicIndexer.TopicField.ID, topic.getId());
            if(topicTypes.length > 0) {
                assertTrue(topicTypes.length == 1);

                String[] split = topicTypes[0].getTypesId();
                for (String id : split) {
                    assertEquals(CategoriesSearcher.search(categoriesIndexFile, id).length, 1);
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
