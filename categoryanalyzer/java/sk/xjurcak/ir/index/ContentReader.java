package sk.xjurcak.ir.index;

import java.io.*;

/**
 * Created by xjurcak on 10/13/2014.
 */
public abstract class ContentReader {

    private BufferedReader mStream;

    public ContentReader(InputStream stream) {
        mStream = new BufferedReader(new InputStreamReader(stream));
    }

    public Content nextCategory(){
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

    protected abstract Content createContent(String line);
}
