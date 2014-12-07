package categoryanalyzer.index;

import java.io.*;

/**
 * Created by xjurcak on 10/13/2014.
 */
public abstract class TopicReader {

    public BufferedReader getStream() {
        return mStream;
    }

    private BufferedReader mStream;

    public TopicReader(InputStream stream) {
        mStream = new BufferedReader(new InputStreamReader(stream));
    }

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

    protected abstract Topic createTopic(String line);
}
