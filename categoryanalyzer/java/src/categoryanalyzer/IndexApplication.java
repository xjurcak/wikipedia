package categoryanalyzer;

import categoryanalyzer.dbpedia.index.DBPediaArticleCategoryReader;
import categoryanalyzer.dbpedia.index.DBPediaCategoryReader;
import categoryanalyzer.dbpedia.index.DBPediaLabelsReader;
import categoryanalyzer.index.*;
import categoryanalyzer.freebase.index.FreebaseCategoryReader;
import categoryanalyzer.freebase.index.FreebaseTopicReader;
import categoryanalyzer.index.CategoryIndexer;

import java.io.*;

/**
 * Created by xjurcak on 10/31/2014.
 */
public class IndexApplication {

    public static void main(String[] args){

        String usage = "This aplication create indexes for DPBedia and Freebase article categories. Indexes are stored in INDEX_PATH directory. Use sk.xjurcak.Application for categories comparison. \n"
                + "Usage: \n"
                + "[-index INDEX_PATH]: Path to indexes stored by IndexApplication\n"
                + "[-freebaseTopics FREEBASE_TOPIC_DATA_PATH]\n"
                + "[-freebaseTypes FREEBASE_TYPES_DATA_PATH]\n"
                + "[-dbpediaCategoryLabels DBPEDIA_CATEGORYLABEL_DATA_PATH]\n"
                + "[-dbpediaArticleCategories DBPEDIA_ARTICLECATEGORIES_DATA_PATH]\n"
                + "[-dbpediaLabels DBPEDIA_LABELS_DATA_PATH]\n";


        String indexPath = "indexes";
        String freebaseTypes = null;
        String freebaseTopics = null;
        String dbpediaCategoryLabels = null;
        String dbpediaArticleCategories = null;
        String dbpediaLabels = null;

//        boolean create = true;
        for(int i=0;i<args.length;i++) {
            if ("-index".equals(args[i])) {
                indexPath = args[i+1];
                i++;
            }

            if ("-help".equals(args[i])) {
                System.out.println(usage);
                i++;
                System.exit(1);
            }

            if ("-freebaseTypes".equals(args[i])) {
                freebaseTypes = args[i+1];
                i++;
            }

            if ("-freebaseTopics".equals(args[i])) {
                freebaseTopics = args[i+1];
                i++;
            }

            if ("-dbpediaCategoryLabels".equals(args[i])) {
                dbpediaCategoryLabels = args[i+1];
                i++;
            }

            if ("-dbpediaArticleCategories".equals(args[i])) {
                dbpediaArticleCategories = args[i+1];
                i++;
            }

            if ("-dbpediaLabels".equals(args[i])) {
                dbpediaLabels = args[i+1];
                i++;
            }
        }

        File indexDir = new File(indexPath);

        //index dbpedia topic labes
        if(dbpediaArticleCategories == null && dbpediaLabels != null){
            System.err.println("ERROR: DBPEDIA_ARTICLECATEGORIES_DATA_PATH missing. You must specify both DBPEDIA_ARTICLECATEGORIES_DATA_PATH and DBPEDIA_LABELS_DATA_PATH");
        } else if(dbpediaArticleCategories != null && dbpediaLabels == null){
            System.err.println("ERROR: DBPEDIA_LABELS_DATA_PATH missing. You must specify both DBPEDIA_ARTICLECATEGORIES_DATA_PATH and DBPEDIA_LABELS_DATA_PATH");
        } else if(dbpediaArticleCategories != null && dbpediaLabels != null) {
            File dbpediaArticleCategoriesFile = new File(dbpediaArticleCategories);
            File dbpediaLabelsFile = new File(dbpediaLabels);
            if(!dbpediaArticleCategoriesFile.exists())
            {
                System.err.println("ERROR: DBPEDIA_ARTICLECATEGORIES_DATA_PATH: " + dbpediaArticleCategoriesFile.getAbsolutePath() + " does not exist.");
            } else if (!dbpediaLabelsFile.exists()) {
                System.err.println("ERROR: DBPEDIA_LABELS_DATA_PATH: " + dbpediaLabelsFile.getAbsolutePath() + " does not exist.");
            }else{
                System.out.println("Start create dbpedia topics index");
                indexDBPediaTopics(indexDir, dbpediaArticleCategoriesFile, dbpediaLabelsFile);
                System.out.println("Finish create dbpedia topics index");
            }
        }

        //index freebase topics
        if(freebaseTopics != null) {
            File f = new File(freebaseTopics);
            if(!f.exists())
            {
                System.err.println("ERROR: FREEBASE_TOPICS_DATA_PATH: " + f.getAbsolutePath() + " does not exist.");
            } else {
                System.out.println("Start create freebase topics index");
                indexFreebaseTopics(indexDir, f);
                System.out.println("Finish create freebase topics index");
            }
        }

        //index dbpedia categories
        if(dbpediaCategoryLabels != null) {
            File f = new File(dbpediaCategoryLabels);
            if(!f.exists())
            {
                System.err.println("ERROR: DBPEDIA_CATEGORYLABEL_DATA_PATH: " + f.getAbsolutePath() + " does not exist.");
            } else {
                System.out.println("Start create dbpedia categories index");
                indexDBPediaCategories(indexDir, f);
                System.out.println("Finish create dbpedia categories index");
            }
        }

        //index freebase categories
        if(freebaseTypes != null) {
            File f = new File(freebaseTypes);
            if(!f.exists())
            {
                System.err.println("ERROR: FREEBASE_TYPES_DATA_PATH: " + f.getAbsolutePath() + " does not exist.");
            } else {
                System.out.println("Start create freebase categories index");
                indexFreebaseCategories(indexDir, f);
                System.out.println("Finish create freebase categories index");
            }
        }
    }

    private static void indexFreebaseCategories(File indexDir, File f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            File indexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);
            indexFile.mkdirs();

            CategoryIndexer.index(new FreebaseCategoryReader(fis), indexFile);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void indexDBPediaCategories(File indexDir, File f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            File indexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
            indexFile.mkdirs();

            CategoryIndexer.index(new DBPediaCategoryReader(fis), indexFile);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void indexDBPediaTopics(File indexDir, File dbpediaArticleCategoriesFile, File dbpediaLabelsFile) {
        try {
            FileInputStream fis = new FileInputStream(dbpediaLabelsFile);
            File indexFile = IndexPaths.createTopicLabelPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
            indexFile.mkdirs();

            TopicIndexer.index(new DBPediaLabelsReader(fis), indexFile);
            fis.close();

            indexFile = IndexPaths.createTopicArticleCategoriesPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
            indexFile.mkdirs();
            fis = new FileInputStream(dbpediaArticleCategoriesFile);
            TopicIndexer.index(new DBPediaArticleCategoryReader(fis), indexFile);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void indexFreebaseTopics(File indexDir, File input) {
        try {
            FileInputStream fis = new FileInputStream(input);
            File indexFile = IndexPaths.createTopicPath(indexDir, IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);
            indexFile.mkdirs();

            FreebaseTopicReader reader = new FreebaseTopicReader(fis);
            TopicIndexer.index(reader, indexFile);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
