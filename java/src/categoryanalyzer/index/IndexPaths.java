package categoryanalyzer.index;

import java.io.File;

/**
 * Utility class for create paths to index
 */
public class IndexPaths {

    public enum Source{

        FREEBASE("freebase"),
        DBPEDIA("dbpedia");

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

    public static File createCategoryPath(File indexDir, Source source, Lang lang){
        return new File(indexDir.getPath() + "/" + source.getPath() + "/category/" + lang.getLang());
    }

    public static File createTopicPath(File indexDir, Source source, Lang lang){
        return new File(indexDir.getPath() + "/" + source.getPath() + "/topic/" + lang.getLang());
    }

    public static File createTopicLabelPath(File indexDir, Source source, Lang lang){
        return new File(indexDir.getPath() + "/" + source.getPath() + "/topic/labels/" + lang.getLang());
    }

    public static File createTopicArticleCategoriesPath(File indexDir, Source source, Lang lang){
        return new File(indexDir.getPath() + "/" + source.getPath() + "/topic/article_categories/" + lang.getLang());
    }

}
