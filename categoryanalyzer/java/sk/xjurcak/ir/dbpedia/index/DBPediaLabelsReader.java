package sk.xjurcak.ir.dbpedia.index;

import sk.xjurcak.ir.index.Content;
import sk.xjurcak.ir.index.ContentReader;
import sk.xjurcak.ir.index.Topic;
import sk.xjurcak.ir.index.TopicReader;

import java.io.InputStream;

/**
 * Created by xjurcak on 10/13/2014.
 */
public class DBPediaLabelsReader extends TopicReader {


    public DBPediaLabelsReader(InputStream stream) {
        super(stream);
    }

    @Override
    protected Topic createTopic(String line) {
        if(!line.startsWith("<http://dbpedia.org/resource/") )
            return Topic.NULL;

        int index = line.indexOf(">");
        String id = line.substring(29, index);

        //get eng
        String title = line.substring(index + 48, line.length() - 6);

        return new Topic(id, title, null);
    }

    private static String getLanguage(String line, String lang){
        if(line.startsWith("null,"))
            return null;

        return line.substring(1, line.indexOf("\"" + lang + ", "));
    }
}
