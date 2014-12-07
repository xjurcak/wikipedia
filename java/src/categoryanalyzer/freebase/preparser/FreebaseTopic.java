package categoryanalyzer.freebase.preparser;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data class for freebase topic
 */
public class FreebaseTopic extends FreebaseObject {

    public List<String> getTypes() {
        return types;
    }

    private List<String> types = new ArrayList<>();

    public void addType(String type){
        types.add(type);
    }

    @Override
    protected void writeJson(JsonWriter gson) throws IOException {
        super.writeJson(gson);

        //write types
        gson.name("types");
        gson.beginArray();

        for(String s : types){
            gson.value(s.substring(28, s.length() - 1));
        }
        gson.endArray();
    }

    public static FreebaseTopic fromString(String line) {
        return new Gson().fromJson(line, FreebaseTopic.class);
    }
}
