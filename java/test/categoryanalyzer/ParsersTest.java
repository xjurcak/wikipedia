package categoryanalyzer;

import categoryanalyzer.dbpedia.index.DBPediaArticleCategoryReader;
import categoryanalyzer.dbpedia.index.DBPediaCategoryReader;
import categoryanalyzer.dbpedia.index.DBPediaLabelsReader;
import categoryanalyzer.index.Category;
import categoryanalyzer.index.Topic;
import org.apache.lucene.util.LuceneTestCase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * DBPedia parsers test
 * Created by xjurcak on 12/8/2014.
 */
public class ParsersTest extends LuceneTestCase {

    /**
     * Test parsing Topics from input file article_categories_en.ttl file of dbpedia dump files.
     * This test search for article with Bratislava id and check if categories are parsed well.
     * @throws FileNotFoundException
     */
    public void testDBPediaArticleCategoryParser() throws FileNotFoundException {

        DBPediaArticleCategoryReader reader = new DBPediaArticleCategoryReader(new FileInputStream("data/dbpedia/article_categories_en.ttl"));
        Topic topic = null;
        while((topic = reader.nextTopic()) != null){

            if(topic.getId().equals("Bratislava")){

                //Bratislava topic was found check categories id
                assertEquals(4, topic.getTypes().size());
                assertEquals("Category:Bratislava", topic.getTypes().get(0));
                assertEquals("Category:Capitals_in_Europe", topic.getTypes().get(1));
                assertEquals("Category:Cities_and_towns_in_Slovakia", topic.getTypes().get(2));
                assertEquals("Category:Populated_places_on_the_Danube", topic.getTypes().get(3));
                break;
            }
        }

        //assert if we reach end of file and no article was founded
        assertNotNull(topic);
    }

    /**
     * Test parsing Topics from input file labels_en.ttl file of dbpedia dump files.
     * This test search for article with Bratislava id and check if label is parsed well.
     * @throws FileNotFoundException
     */
    public void testDBPediaLabelsParser() throws FileNotFoundException {

        DBPediaLabelsReader reader = new DBPediaLabelsReader(new FileInputStream("data/dbpedia/labels_en.ttl"));
        Topic topic = null;
        while((topic = reader.nextTopic()) != null){

            if(topic.getId() != null && topic.getId().equals("Bratislava")){

                //Bratislava topic was found check label
                assertEquals("Bratislava", topic.getTitle());
                break;
            }
        }

        //assert if we reach end of file and no article was founded
        assertNotNull(topic);
    }

    /**
     * Test parsing Category from input file category_labels_en.ttl file of dbpedia dump files.
     * This test search for category with "Category:Bratislava" and "Category:Capitals_in_Europe" id and check if label is parsed well.
     * @throws FileNotFoundException
     */
    public void testDBPediaCategoryLabelsParser() throws FileNotFoundException {

        DBPediaCategoryReader reader = new DBPediaCategoryReader(new FileInputStream("data/dbpedia/category_labels_en.ttl"));
        Category category = null;
        while((category = reader.nextCategory()) != null){

            if(category.getId() != null){
                if(category.getId().equals("Category:Bratislava")) {

                    //Bratislava category was found check category label
                    assertEquals("Bratislava", category.getTitle());
                    break;
                } else if(category.getId().equals("Category:Capitals_in_Europe")) {

                    //Capitals_in_Europe category was found check category label
                    assertEquals("Capitals in Europe", category.getTitle());
                    break;
                }
            }
        }

        //assert if we reach end of file and no category was founded
        assertNotNull(category);
    }

}
