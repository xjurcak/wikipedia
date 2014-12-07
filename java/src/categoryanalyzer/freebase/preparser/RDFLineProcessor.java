package categoryanalyzer.freebase.preparser;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Base class for process lines from rdf file. Each line are split by \t character.
 */
public abstract class RDFLineProcessor {
    protected OutputStream fos;

    public RDFLineProcessor(OutputStream fos) {
        this.fos = fos;
    }

    public abstract void processLine(String... split) throws IOException;
}
