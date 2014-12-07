package categoryanalyzer.freebase.preparser;

import java.io.IOException;
import java.io.OutputStream;

/**
* Use this class processor for preprocess freebase dump. This processor process freebase dump lines and create Topics. Topic is wrote to OutputStream.
*/
public class FreebaseTopicRDFLineProcessor extends RDFLineProcessor {
    private FreebaseTopic type;
    private boolean skip = false;
    private double typeNumber = 0;

    public FreebaseTopicRDFLineProcessor(OutputStream fos) {
        super(fos);
    }

    @Override
    public void processLine(String... split) throws IOException {
        if (!(type != null && split[0].equals(type.getId()))) {

            //we are on new subject so persist old if valid
            if (!skip && type != null && type.isValid()) {
                typeNumber++;
                type.persist(fos);
                System.out.print("\rTopic number writed: " + typeNumber);
            }

            //create new topic
            type = new FreebaseTopic();
            type.setId(split[0]);
            skip = true;
        }

        //check type
        if (split[1].equals("<http://rdf.freebase.com/ns/type.object.type>")) {
            if (split[2].equals("<http://rdf.freebase.com/ns/common.topic>")) {  //we can keep this object
                skip = false;
            }
            type.addType(split[2]);

        } else {
            //parse name
            if (split[1].equals("<http://rdf.freebase.com/ns/type.object.name>")) {
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
}
