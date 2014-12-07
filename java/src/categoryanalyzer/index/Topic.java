package categoryanalyzer.index;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class for Freebase topic or DBPedia article.
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
    
    public List<String> getTypes(){
        return types;
    }

    public void addType(String s) {
        if(types == null)
            types = new ArrayList<>();
        types.add(s);
    }
}
