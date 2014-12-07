package categoryanalyzer.dbpedia.index;

import categoryanalyzer.index.Topic;
import categoryanalyzer.index.TopicReader;

import java.io.InputStream;

/**
 * Create topics stream from dbpedia label_en.ttl file
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

        return new Topic(id, title);
    }

    private static String getLanguage(String line, String lang){
        if(line.startsWith("null,"))
            return null;

        return line.substring(1, line.indexOf("\"" + lang + ", "));
    }
}
