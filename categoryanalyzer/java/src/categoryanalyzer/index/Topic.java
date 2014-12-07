package categoryanalyzer.index;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xjurcak on 11/1/2014.
 */
public class Topic {

    public final static Topic NULL = new Topic(null, null);

    private String id;

    private String title;

    private List<String> types = null;

    public Topic(String id, String title, List<String> types) {
        this.id = id;
        this.title = title;
        this.types = types;
    }

    public Topic(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Topic(String id, String title, String type) {
        this.id = id;
        this.title = title;
        addType(type);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    
    public List<String> getTypesId(){
        return types;
    }

    public void addType(String s) {
        if(types == null)
            types = new ArrayList<>();
        types.add(s);
    }
}
