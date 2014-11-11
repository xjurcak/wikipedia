package sk.xjurcak.ir.freebase.preparser;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by xjurcak on 10/31/2014.
 */
public abstract class FreebaseObject {
    private String id;
    private String[] names = new String [3];

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public void setEnglishName(String name){
        names[0] = name;
    }

    public void setSlovakName(String name){
        names[1] = name;
    }

    public void setFrenchName(String name){
        names[2] = name;
    }

    public abstract void persist(OutputStream fos) throws IOException;

    public boolean isValid() {
        if(id == null)
            return false;

        boolean valid = false;
        for (String name : names){
            if(name != null)
                valid = true;
        }
        return valid;
    }
}
