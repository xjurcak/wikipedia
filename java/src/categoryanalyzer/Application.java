package categoryanalyzer;


import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.spell.LuceneLevenshteinDistance;
import categoryanalyzer.index.*;

import java.io.File;

/**
 * Created by xjurcak on 10/31/2014.
 */
public class Application {

    public static void main(String[] args){

        String usage = "This aplication compare DPBedia and Freebase categories. Indexes created by IndexApplication in INDEX_PATH directory are used for comparison. \n"
                + "Usage: \n"
                + "sk.xjurcak.ir.Application -article TITLE [-index INDEX_PATH]\n"
                + "-article TITLE: DBPedia article name to compare."
                + "[-index INDEX_PATH]: Path to indexes stored bz IndexApplication";


        String indexPath = "indexes";
        String article = null;
//        boolean create = true;
        for(int i=0;i<args.length;i++) {
            if ("-index".equals(args[i])) {
                indexPath = args[i+1];
                i++;
            }

            if ("-article".equals(args[i])) {
                article = args[i+1];
                i++;
            }

            if ("-help".equals(args[i])) {
                System.out.println(usage);
                i++;
                System.exit(1);
            }
        }

        if(article == null){
            System.err.println("ERROR: -article parameter missing. You must specify article name.");
            System.exit(1);
        }

        File indexDir = new File(indexPath);
        if(!indexDir.isDirectory()){
            System.err.println("ERROR: Index path: \""+ indexDir.getAbsolutePath() +"\" is not valid index directory created by sk.xjurcak.ir.IndexApplication. You must create index first or select another INDEX_PATH directory.");
            System.exit(1);
        }


        File topicLabelPath = IndexPaths.createTopicLabelPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
        File freebaseTopicPath = IndexPaths.createTopicPath(indexDir, IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);

        Topic[] topics = new Topic[0];
        try {
            topics = TopicSearcher.search(topicLabelPath, TopicIndexer.TopicField.TITLE, article);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(topics.length == 0){
            System.out.println("Article with name "+ article + " has not been found in DbPedia articles.");
            System.exit(1);
        }

        for(Topic topic : topics){
            try {
                Category[] dbPediaTopicCategories = getDBPediaCategoriesLabels(indexDir, topic.getId());
                printDbPediaTopic(topic, dbPediaTopicCategories);

                Topic[] freebaseTopics = TopicSearcher.search(freebaseTopicPath, TopicIndexer.TopicField.TITLE, topic.getTitle());

                if(freebaseTopics.length == 0){
                    System.out.println("Nothing to compare. Article with name "+ topic.getTitle() + " has not been found in Freebase topics.");
                    System.exit(1);
                }

                compareWithFreebaseTopics(indexDir, topic, dbPediaTopicCategories, freebaseTopics);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private static void compareWithFreebaseTopics(File indexDir, Topic dbPediaTopic, Category[] dbPediaTopicCategories, Topic[] freebaseTopics) throws ParseException {
        File categoriesIndexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);
        for(Topic topic : freebaseTopics){
            Category[] fCat = getContents(categoriesIndexFile, topic);
            compareWithFreebaseTopic(dbPediaTopic, dbPediaTopicCategories, topic, fCat);
        }
    }

    private static void compareWithFreebaseTopic(Topic dbPediaTopic, Category[] dbPediaTopicCategories, Topic freebaseTopic, Category[] freebaseTopicCategories) {
        System.out.println("==================================== Comparison start: dbPedia article with id: " + dbPediaTopic.getId() + " and freebase topic with id : " + freebaseTopic.getId()  + "==================================\n\n");

        int iteration = Math.max(dbPediaTopicCategories.length, freebaseTopicCategories.length);
        for (int i = 0; i < iteration; i++) {
            String dbCat = "";
            if(i < dbPediaTopicCategories.length && dbPediaTopicCategories[i] != null){
                dbCat = dbPediaTopicCategories[i].getTitle();
            }

            String fCat = "";
            if(i < freebaseTopicCategories.length && freebaseTopicCategories[i] != null){
                fCat = freebaseTopicCategories[i].getTitle();
            }
            if(!dbCat.isEmpty() || !fCat.isEmpty())
                System.out.printf("%-50.50s  %-50.50s%n", dbCat, fCat);
        }
        System.out.printf("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");

        LuceneLevenshteinDistance lld = new LuceneLevenshteinDistance();
        for (int i = 0; i < dbPediaTopicCategories.length; i++) {

            int bestMatch = 0;
            float bestScore = Float.MIN_VALUE;
            if(dbPediaTopicCategories[i] == null)
                continue;

            for (int j = 0; j < freebaseTopicCategories.length; j++) {
                float distance = lld.getDistance(dbPediaTopicCategories[i].getTitle(), freebaseTopicCategories[j].getTitle());
                if(bestScore < distance){
                    bestScore = distance;
                    bestMatch = j;
                }
            }
            System.out.printf("%-50.50s  %-50.50s %-50.50s%n", dbPediaTopicCategories[i].getTitle(), freebaseTopicCategories[bestMatch].getTitle(), bestScore);
        }

        System.out.println("==================================== Comparison finish: dbPedia article with id: " + dbPediaTopic.getId() + " and freebase topic with id : " + freebaseTopic.getId()  + "==================================\n\n\n\n");
    }

    private class Match{
        String dbpediaCat;
        String freebaseCat;
        float distance;
    }

    private static Category[] getDBPediaCategoriesLabels(File indexDir, String topicId) throws ParseException {
        File indexFile = IndexPaths.createTopicArticleCategoriesPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
        File categoriesIndexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);

        Topic[] topicTypes = TopicSearcher.search(indexFile, TopicIndexer.TopicField.ID, topicId);

        if (topicTypes.length > 0) {

            return getContents(categoriesIndexFile, topicTypes[0]);
        } else {
            return new Category[0];
        }
    }

    private static Category[] getContents(File categoriesIndexFile, Topic topicType) throws ParseException {
        Category[] result = new Category[topicType.getTypesId().size()];

        int i = 0;
        for (String type : topicType.getTypesId()) {
            Category[] categories = CategoriesSearcher.search(categoriesIndexFile, type);
            if (categories.length > 0) {
                result[i] = categories[0];
            } else {
                System.out.println("ERROR: Does not find label for category id: " + type);
            }
            i++;
        }
        return result;
    }

    private static final void printDbPediaTopic(Topic topic, Category[] types) throws ParseException {
        System.out.println("==================================== Start ARTICLE: " + topic.getTitle() + "==================================");
        if(types.length > 0){
            for (Category type : types){
                if(type != null)
                    System.out.println(type.getTitle());
            }
        } else {
            System.out.println("Article does not contain any categories.");

        }
        System.out.println("==================================== End ARTICLE: " + topic.getTitle() + "==================================");
    }
}
