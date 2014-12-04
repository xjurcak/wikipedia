package sk.xjurcak.ir;

import sk.xjurcak.ir.dbpedia.index.DBPediaArticleCategoryReader;
import sk.xjurcak.ir.dbpedia.index.DBPediaCategoryReader;
import sk.xjurcak.ir.dbpedia.index.DBPediaLabelsReader;
import sk.xjurcak.ir.freebase.index.FreebaseCategoryReader;
import sk.xjurcak.ir.freebase.index.FreebaseTopicReader;
import sk.xjurcak.ir.index.ContentIndexer;
import sk.xjurcak.ir.index.IndexPaths;
import sk.xjurcak.ir.index.TopicIndexer;
import sk.xjurcak.ir.utils.Utils;

import java.io.*;

/**
 * Created by xjurcak on 10/31/2014.
 */
public class IndexApplication {

    public static void main(String[] args){

        String usage = "This aplication create indexes for DPBedia and Freebase article categories. Indexes are stored in INDEX_PATH directory. Use sk.xjurcak.Application for categories comparison. \n"
                + "Usage: \n"
                + "sk.xjurcak.ir.Application -article TITLE [-index INDEX_PATH]\n"
                + "[-index INDEX_PATH]: Path to indexes stored bz IndexApplication"
                + "[-freebaseTopics FREEBASE_TOPIC_DATA_PATH]"
                + "[-freebaseTypes FREEBASE_TYPES_DATA_PATH]";


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

        //index dbpedia topic labes
        //indexDBPediaTopics();

        //index freebase topics
        indexFreebaseTopics(indexDir);

        //index dbpedia categories
        //indexDBPediaCategories();

        //index freebase categories
        //indexFreebaseCategories();
    }

    private static void indexFreebaseCategories(File indexDir) {
        try {
            FileInputStream fis = new FileInputStream("data/freebase/generate_freebase_types");
            File indexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);
            indexFile.mkdirs();

            ContentIndexer.index(new FreebaseCategoryReader(fis), indexFile);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void indexDBPediaCategories(File indexDir) {
        try {
            FileInputStream fis = new FileInputStream(Utils.getDataPath("sk/xjurcak/ir/dbpedia/data/category_labels_en.ttl").toFile());
            File indexFile = IndexPaths.createCategoryPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
            indexFile.mkdirs();

            ContentIndexer.index(new DBPediaCategoryReader(fis), indexFile);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void indexDBPediaTopics(File indexDir) {
        try {
            FileInputStream fis = new FileInputStream("data/dbpedia/labels_en.ttl");
            File indexFile = IndexPaths.createTopicLabelPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
            indexFile.mkdirs();

            TopicIndexer.index(new DBPediaLabelsReader(fis), indexFile);
            fis.close();

            indexFile = IndexPaths.createTopicArticleCategoriesPath(indexDir, IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
            indexFile.mkdirs();
            fis = new FileInputStream("data/dbpedia/article_categories_en.ttl");
            TopicIndexer.index(new DBPediaArticleCategoryReader(fis), indexFile);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void indexFreebaseTopics(File indexDir) {
        try {
            FileInputStream fis = new FileInputStream("generate_freebase_topics");
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
