package sk.xjurcak.ir.freebase.preparser;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.*;
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
}
