package categoryanalyzer.index;

import java.io.*;

/**
 * Abstract stream of Categories used by CategoryIndexer to create index.
 */
public abstract class CategoryReader {

    private BufferedReader mStream;

    public CategoryReader(InputStream stream) {
        mStream = new BufferedReader(new InputStreamReader(stream));
    }

    /**
     * Read next category from stream.
     * @return category or null if end of file reached.
     */
    public Category nextCategory(){
        String line = null;
        try {
            line = mStream.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(line == null)
            return null;
        return createContent(line);
    }

    /**
     * Override this to create category from line read from stream.
     * @param line
     * @return
     */
    protected abstract Category createContent(String line);
}
