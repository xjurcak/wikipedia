package sk.xjurcak.ir.index;

import java.io.FileInputStream;

/**
 * Created by xjurcak on 10/13/2014.
 */
public class DBPediaCategoryReader extends ContentReader {


    public DBPediaCategoryReader(FileInputStream stream) {
        super(stream);
    }

    @Override
    protected Content createContent(String line) {
        return null;
    }
}
