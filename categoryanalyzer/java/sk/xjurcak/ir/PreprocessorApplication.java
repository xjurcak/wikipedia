package sk.xjurcak.ir;

import sk.xjurcak.ir.dbpedia.index.DBPediaArticleCategoryReader;
import sk.xjurcak.ir.dbpedia.index.DBPediaCategoryReader;
import sk.xjurcak.ir.dbpedia.index.DBPediaLabelsReader;
import sk.xjurcak.ir.freebase.index.FreebaseCategoryReader;
import sk.xjurcak.ir.freebase.index.FreebaseTopicReader;
import sk.xjurcak.ir.freebase.preparser.*;
import sk.xjurcak.ir.index.ContentIndexer;
import sk.xjurcak.ir.index.IndexPaths;
import sk.xjurcak.ir.index.TopicIndexer;
import sk.xjurcak.ir.utils.Utils;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by xjurcak on 10/31/2014.
 */
public class PreprocessorApplication {

    public enum Type{
        Topics,
        Types
    }
    
    public static void main(String[] args) {

        String usage = "This aplication preprocess Freebase data."
                + "Usage: \n"
                + "sk.xjurcak.ir.PreprocessorApplication [-type [topics|types]] -data DATA_PATH output\n"
                + "-type [topics|types]: topic for topics types for categories.\n"
                + "-data DATA_PATH: Path for freebase data\n"
                + "output : path of output file \n";


        String dataPath = null;
        String output = "generated_freebase_data";
        Type type = Type.Topics;
//        boolean create = true;
        String typeString = null;
        for (int i = 0; i < args.length; i++) {
            if ("-data".equals(args[i])) {
                dataPath = args[i + 1];
                i++;
            }

            if ("-help".equals(args[i])) {
                System.out.println(usage);
                i++;
                System.exit(1);
            }

            if ("-type".equals(args[i])) {
                typeString = args[i + 1];
                i++;
            }

            output = args[i];
        }

        if(typeString != null) {

            switch (typeString.toLowerCase()) {
                case "topics":
                    type = Type.Topics;
                    break;
                case "types":
                    type = Type.Types;
                    break;

                default:
                    System.err.println("ERROR: unknown -type parameter \"" + typeString + "\"");
                    System.exit(1);
            }
        }


        if (output == null) {
            System.err.println("ERROR: output parameter missing. You must specify output file name.");
            System.exit(1);
        }

        if (dataPath == null) {
            System.err.println("ERROR: DATA_PATH parameter missing. You must specify input file name.");
            System.exit(1);
        }

        try {
            FileInputStream fis = new FileInputStream(dataPath);
            GZIPInputStream gzipStream = new GZIPInputStream(fis);
            BufferedReaderChanged gis = new BufferedReaderChanged(new InputStreamReader(gzipStream));
            OutputStream fos = new FileOutputStream(output);

            try {
                if(type ==Type.Types)
                    RDFFileProcessor.readLines(gis, new FreebaseTypeRDFLineProcessor(fos));
                else
                    RDFFileProcessor.readLines(gis, new FreebaseTopicRDFLineProcessor(fos));
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
}
