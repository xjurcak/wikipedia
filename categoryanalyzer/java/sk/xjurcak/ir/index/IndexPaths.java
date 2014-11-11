package sk.xjurcak.ir.index;

import java.io.File;

/**
 * Created by xjurcak on 11/2/2014.
 */
public class IndexPaths {

    public enum Source{

        FREEBASE("indexes/freebase"),
        DBPEDIA("indexes/dbpedia");

        private String path;

        public String getPath() {
            return path;
        }

        Source(String path) {

            this.path = path;
        }
    }

    public enum Lang{
        EN("en");

        public String getLang() {
            return lang;
        }

        private String lang;

        Lang(String lang) {

            this.lang = lang;
        }
    }

    public static File createCategoryPath(Source source, Lang lang){
        return new File(source.getPath() + "/category/" + lang.getLang());
    }

    public static File createTopicPath(Source source, Lang lang){
        return new File(source.getPath() + "/topic/" + lang.getLang());
    }

    public static File createTopicLabelPath(Source source, Lang lang){
        return new File(source.getPath() + "/topic/labels/" + lang.getLang());
    }

    public static File createTopicArticleCategoriesPath(Source source, Lang lang){
        return new File(source.getPath() + "/topic/article_categories/" + lang.getLang());
    }

}
