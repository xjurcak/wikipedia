package sk.xjurcak.ir.freebase.preparser;

import java.io.IOException;

/**
 * Created by xjurcak on 10/31/2014.
 */
public class RDFFileProcessor {
    protected static void readLines(BufferedReaderChanged gis, RDFLineProcessor processor) throws IOException {
        String line;
        double i = 0;
        while(( line = gis.readLine()) != null){
            i++;

            String[] split = line.split("\t");

            if(split.length != 4)
                continue;

            if(i%1000000 == 0)
                System.out.println("GZip line number: " + i );
            processor.processLine(split);


        }
    }
}
