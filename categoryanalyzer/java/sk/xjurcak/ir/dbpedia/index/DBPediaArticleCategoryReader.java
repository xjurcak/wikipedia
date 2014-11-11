package sk.xjurcak.ir.dbpedia.index;

import sk.xjurcak.ir.index.Topic;
import sk.xjurcak.ir.index.TopicReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xjurcak on 10/13/2014.
 */
public class DBPediaArticleCategoryReader extends TopicReader {

    Topic currentTopic = null;

    public DBPediaArticleCategoryReader(InputStream stream) {
        super(stream);
    }

    @Override
    public Topic nextTopic() {
        try {
            String currentLine;
            while((currentLine = getStream().readLine()) != null){
                String[] split = parseLine(currentLine);
                if(split == null)
                    continue;

                if(currentTopic == null){
                    currentTopic = new Topic(split[0], null, split[1] + "<c>");
                } else if ( !currentTopic.getId().equals(split[0])){
                    Topic res = currentTopic;
                    currentTopic = new Topic(split[0], null, split[1] + "<c>");
                    return res;
                } else {
                    currentTopic.addType(split[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Topic res = currentTopic;
        currentTopic = null;
        return res;
    }

    private String[] parseLine(String line) {
        if (!line.startsWith("<http://dbpedia.org/resource/"))
            return null;

        String[] result = new String[2];
        int index = line.indexOf(">");
        result[0] = line.substring(29, index);
        result[1] = line.substring(index + 66, line.length() - 3);
        return result;
    }

    @Override
    protected Topic createTopic(String line) {
        return null;
    }
}
