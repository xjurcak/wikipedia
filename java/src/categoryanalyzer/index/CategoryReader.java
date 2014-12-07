package categoryanalyzer.index;

import java.io.*;

/**
 * Created by xjurcak on 10/13/2014.
 */
public abstract class CategoryReader {

    private BufferedReader mStream;

    public CategoryReader(InputStream stream) {
        mStream = new BufferedReader(new InputStreamReader(stream));
    }

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

    protected abstract Category createContent(String line);
}
