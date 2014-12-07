package categoryanalyzer.freebase.preparser;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * Base class for freebase objects. This object can be store by persist method to output stream.
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

    /**
     * Write object as json to output stream. Override writeJson method to inject data in extended classes.
     * @param fos
     * @throws IOException
     */
    public void persist(OutputStream fos) throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter gson = new JsonWriter(stringWriter);
        gson.beginObject();

        //write types
        writeJson(gson);

        gson.endObject();
        gson.close();

        fos.write(stringWriter.toString().getBytes());
        fos.write('\n');
    }

    protected void writeJson(JsonWriter gson) throws IOException {

        //write id
        gson.name("id");
        gson.value(getId().substring(28, getId().length() - 1));

        //write names
        gson.name("names");
        gson.beginArray();

        for(String names : getNames()){
            gson.value(names);
        }

        gson.endArray();
    }

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
