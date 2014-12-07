package categoryanalyzer.freebase.preparser;

import java.io.IOException;
import java.io.OutputStream;

/**
* Created by xjurcak on 12/4/2014.
*/
public class FreebaseTypeRDFLineProcessor extends RDFLineProcessor {
    private FreebaseType type;
    private boolean skip = false;
    private double typeNumber = 0;

    public FreebaseTypeRDFLineProcessor(OutputStream fos) {
        super(fos);
    }

    @Override
    public void processLine(String... split) throws IOException {
        //we are still on same object so collect another info
        if (type != null && split[0].equals(type.getId())) {

            if (skip)
                return;

        } else {
            if (!skip && type != null && type.isValid()) {
                typeNumber++;
                type.persist(fos);
                System.out.print("\rType number writed: " + typeNumber);
            }
            type = new FreebaseType();
            type.setId(split[0]);
            skip = false;
        }

        //check type
        if (split[1].equals("<http://rdf.freebase.com/ns/type.object.type>")) {
            if (split[2].equals("<http://rdf.freebase.com/ns/type.type>")) {  //we can keep this object
                skip = false;
            } else {
                skip = true;
            }
        } else if (split[1].equals("<http://rdf.freebase.com/ns/type.object.name>")) {
            if (split[2].endsWith("@en")) {
                type.setEnglishName(split[2]);
            }
            if (split[2].endsWith("@sk")) {
                type.setSlovakName(split[2]);
            }
            if (split[2].endsWith("@fr")) {
                type.setFrenchName(split[2]);
            }
        }
    }
}
