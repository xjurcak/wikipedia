package sk.xjurcak.ir.freebase.index;

import sk.xjurcak.ir.index.Topic;
import sk.xjurcak.ir.index.TopicReader;

import java.io.FileInputStream;

/**
 * Created by xjurcak on 10/13/2014.
 */
public class FreebaseTopicReader extends TopicReader {


    public FreebaseTopicReader(FileInputStream stream) {
        super(stream);
    }

    @Override
    protected Topic createTopic(String line) {
        int titleStart = line.indexOf('[');
        String id = line.substring(0, titleStart).toLowerCase();

        //get eng
        String title = getLanguage(line, titleStart + 1, "@en");

        String types = line.substring(line.indexOf('[', titleStart + 1) + 1, line.length() - 2);
        types = types.replace(",", "<c>");
        return new Topic(id, title, types);
    }

    private static String getLanguage(String line, int start, String lang){
        if(line.startsWith("null,", start))
            return null;

        return line.substring(start + 1, line.indexOf("\"" + lang + ", "));
    }
}
