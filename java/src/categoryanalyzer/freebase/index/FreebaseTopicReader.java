package categoryanalyzer.freebase.index;

import categoryanalyzer.freebase.preparser.FreebaseTopic;
import categoryanalyzer.index.Topic;
import categoryanalyzer.index.TopicReader;

import java.io.FileInputStream;

/**
 * Create topics stream from processed freebase topics
 */
public class FreebaseTopicReader extends TopicReader {


    public FreebaseTopicReader(FileInputStream stream) {
        super(stream);
    }

    @Override
    protected Topic createTopic(String line) {
        FreebaseTopic type = FreebaseTopic.fromString(line);
        //todo multilanguage
        String name = type.getNames()[0];
        if(name != null)
            name = name.substring(1, name.length()-4);

        return new Topic(type.getId(), name , type.getTypes());
    }
}
