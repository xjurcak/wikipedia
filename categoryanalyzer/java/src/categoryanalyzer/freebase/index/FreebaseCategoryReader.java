package categoryanalyzer.freebase.index;

import categoryanalyzer.freebase.preparser.FreebaseType;
import categoryanalyzer.index.Category;
import categoryanalyzer.index.CategoryReader;

import java.io.FileInputStream;

/**
 * Created by xjurcak on 10/13/2014.
 */
public class FreebaseCategoryReader extends CategoryReader {


    public FreebaseCategoryReader(FileInputStream stream) {
        super(stream);
    }

    @Override
    protected Category createContent(String line) {
        FreebaseType type = FreebaseType.fromString(line);
        //todo multilanguage
        String name = type.getNames()[0];
        if(name != null)
            name = name.substring(1, name.length()-4);
        return new Category(type.getId(), name);
    }
}
