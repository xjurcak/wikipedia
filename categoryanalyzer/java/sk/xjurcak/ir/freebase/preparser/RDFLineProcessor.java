package sk.xjurcak.ir.freebase.preparser;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by xjurcak on 10/31/2014.
 */
public abstract class RDFLineProcessor {
    protected OutputStream fos;

    public RDFLineProcessor(OutputStream fos) {
        this.fos = fos;
    }

    public abstract void processLine(String... split) throws IOException;
}
