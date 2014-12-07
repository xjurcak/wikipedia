package categoryanalyzer.index;

import java.io.*;

/**
 * Abstract stream of Topics used by TopicIndexer to create index.
 */
public abstract class TopicReader {

    public BufferedReader getStream() {
        return mStream;
    }

    private BufferedReader mStream;

    public TopicReader(InputStream stream) {
        mStream = new BufferedReader(new InputStreamReader(stream));
    }

    /**
     * Read next topic form stream.
     * @return topic or null if end of file reached.
     */
    public Topic nextTopic(){
        String line = null;
        try {
            line = mStream.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(line == null)
            return null;
        return createTopic(line);
    }

    /**
     * Override this to create topic from line read from stream.
     * @param line
     * @return
     */
    protected abstract Topic createTopic(String line);
}
