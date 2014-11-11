package sk.xjurcak.ir.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by xjurcak on 10/31/2014.
 */
public class Utils {
    /**
     * Gets a resource from the test's classpath as {@link java.nio.file.Path}. This method should only
     * be used, if a real file is needed. To get a stream, code should prefer
     */
    public static Path getDataPath(String name) throws IOException {
        try {
            return Paths.get(ClassLoader.getSystemClassLoader().getResource(name).toURI());
        } catch (Exception e) {
            throw new IOException("Cannot find resource: " + name);
        }
    }
}
