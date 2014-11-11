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

        //index dbpedia topic labes
        //indexDBPediaTopics();

        //index freebase topics
        indexFreebaseTopics();

        //index dbpedia categories
        //indexDBPediaCategories();

        //index freebase categories
        //indexFreebaseCategories();
    }

    private static void indexFreebaseCategories() {
        try {
            FileInputStream fis = new FileInputStream(Utils.getDataPath("sk/xjurcak/ir/freebase/data/generate_freebase_types").toFile());
            File indexFile = IndexPaths.createCategoryPath(IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);
            indexFile.mkdirs();

            ContentIndexer.index(new FreebaseCategoryReader(fis), indexFile);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void indexDBPediaCategories() {
        try {
            FileInputStream fis = new FileInputStream(Utils.getDataPath("sk/xjurcak/ir/dbpedia/data/category_labels_en.ttl").toFile());
            File indexFile = IndexPaths.createCategoryPath(IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
            indexFile.mkdirs();

            ContentIndexer.index(new DBPediaCategoryReader(fis), indexFile);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void indexDBPediaTopics() {
        try {
            FileInputStream fis = new FileInputStream(Utils.getDataPath("sk/xjurcak/ir/dbpedia/data/labels_en.ttl").toFile());
            File indexFile = IndexPaths.createTopicLabelPath(IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
            indexFile.mkdirs();

            TopicIndexer.index(new DBPediaLabelsReader(fis), indexFile);
            fis.close();

            indexFile = IndexPaths.createTopicArticleCategoriesPath(IndexPaths.Source.DBPEDIA, IndexPaths.Lang.EN);
            indexFile.mkdirs();
            fis = new FileInputStream("data/dbpedia/article_categories_en.ttl");
            TopicIndexer.index(new DBPediaArticleCategoryReader(fis), indexFile);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void indexFreebaseTopics() {
        try {
            FileInputStream fis = new FileInputStream("generate_freebase_topics");
            File indexFile = IndexPaths.createTopicPath(IndexPaths.Source.FREEBASE, IndexPaths.Lang.EN);
            indexFile.mkdirs();

            FreebaseTopicReader reader = new FreebaseTopicReader(fis);
            TopicIndexer.index(reader, indexFile);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
