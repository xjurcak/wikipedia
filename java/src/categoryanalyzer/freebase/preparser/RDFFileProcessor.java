package categoryanalyzer.freebase.preparser;

import java.io.IOException;

/**
 * Process rdf file.
 */
public class RDFFileProcessor {

    /**
     * Read all lines in rdf line. Each line are split by \t character. If line contains 4 strings (valid triplet) precess it by processor.
     * @param gis Input reader.
     * @param processor Line processor
     * @throws IOException
     */
    public static void readLines(BufferedReaderChanged gis, RDFLineProcessor processor) throws IOException {
        String line;
        double i = 0;
        while(( line = gis.readLine()) != null){
            i++;

            String[] split = line.split("\t");

            if(split.length != 4)
                continue;

            if(i%100000 == 0) {
                System.out.print("\rGZip line number: " + i);
            }
            processor.processLine(split);

        }
    }
}
