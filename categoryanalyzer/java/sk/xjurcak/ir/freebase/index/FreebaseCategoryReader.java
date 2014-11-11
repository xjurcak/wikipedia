package sk.xjurcak.ir.freebase.index;

import sk.xjurcak.ir.index.Content;
import sk.xjurcak.ir.index.ContentReader;

import java.io.FileInputStream;

/**
 * Created by xjurcak on 10/13/2014.
 */
public class FreebaseCategoryReader extends ContentReader {


    public FreebaseCategoryReader(FileInputStream stream) {
        super(stream);
    }

    @Override
    protected Content createContent(String line) {
        int titleStart = line.indexOf('[');
        String id = line.substring(0, titleStart).toLowerCase();

        //get eng
        String title = getLanguage(line.substring(titleStart + 1), "@en");

        return new Content(id, title);
    }

    private static String getLanguage(String line, String lang){
        if(line.startsWith("null,"))
            return null;

        return line.substring(1, line.indexOf("\"" + lang + ", "));
    }
}
