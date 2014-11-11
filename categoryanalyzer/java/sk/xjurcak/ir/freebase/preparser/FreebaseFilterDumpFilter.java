package sk.xjurcak.ir.freebase.preparser;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by xjurcak on 10/15/2014.
 */
public class FreebaseFilterDumpFilter extends RDFFileProcessor{

    public static void main(String[] args) {

        try {
            FileInputStream fis = new FileInputStream("c:\\Users\\xjurcak\\data\\freebase-rdf-2014-10-19-00-00.gz");
            GZIPInputStream gzipStream = new GZIPInputStream(fis);
            BufferedReaderChanged gis = new BufferedReaderChanged(new InputStreamReader(gzipStream));
            OutputStream fos = new FileOutputStream("generate_freebase_types_4");

            try {
                readLines(gis, new FreebaseTypeRDFLineProcessor(fos));
            } catch (Exception e){
                e.printStackTrace();
            }

            //close resources
            fos.close();
            fis.close();
            gzipStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class FreebaseTypeRDFLineProcessor extends RDFLineProcessor {
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
                    System.out.println("Type number writed: " + typeNumber);
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
}
