package sk.xjurcak.ir.freebase.preparser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xjurcak on 10/25/2014.
 */
public class FreebaseTopic extends FreebaseObject {

    private List<String> types = new ArrayList<>();

    public void addType(String type){
        types.add(type);
    }

    public void persist(OutputStream fos) throws IOException {
        StringBuilder b = new StringBuilder();
        b.append(getId().substring(28, getId().length() - 1));
        b.append(Arrays.toString(getNames()));
        b.append("[");
            for(String s : types){
                b.append(s.substring(28, s.length() - 1));
                b.append(",");
            }
        b.append("]\n");
        fos.write(b.toString().getBytes());
    }
}
