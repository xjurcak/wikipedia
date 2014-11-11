package sk.xjurcak.ir.dbpedia.index;

import sk.xjurcak.ir.index.Content;
import sk.xjurcak.ir.index.ContentReader;

import java.io.InputStream;

/**
 * Created by xjurcak on 10/13/2014.
 */
public class DBPediaCategoryReader extends ContentReader {


    public DBPediaCategoryReader(InputStream stream) {
        super(stream);
    }

    @Override
    protected Content createContent(String line) {
        if(!line.startsWith("<http://dbpedia.org/resource/Category:") )
            return Content.NULL;

        int index = line.indexOf(">");
        String id = line.substring(29, index);

        //get eng
        String title = line.substring(index + 48, line.length() - 6);

        return new Content(id, title);
    }

    private static String getLanguage(String line, String lang){
        if(line.startsWith("null,"))
            return null;

        return line.substring(1, line.indexOf("\"" + lang + ", "));
    }
}
