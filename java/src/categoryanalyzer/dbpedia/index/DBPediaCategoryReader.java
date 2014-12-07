package categoryanalyzer.dbpedia.index;

import categoryanalyzer.index.Category;
import categoryanalyzer.index.CategoryReader;

import java.io.InputStream;

/**
 * Create category stream from dbpedia category_label_en.ttl file
 */
public class DBPediaCategoryReader extends CategoryReader {


    public DBPediaCategoryReader(InputStream stream) {
        super(stream);
    }

    @Override
    protected Category createContent(String line) {
        if(!line.startsWith("<http://dbpedia.org/resource/Category:") )
            return Category.NULL;

        int index = line.indexOf(">");
        String id = line.substring(29, index);

        //get eng
        String title = line.substring(index + 48, line.length() - 6);

        return new Category(id, title);
    }

    private static String getLanguage(String line, String lang){
        if(line.startsWith("null,"))
            return null;

        return line.substring(1, line.indexOf("\"" + lang + ", "));
    }
}
