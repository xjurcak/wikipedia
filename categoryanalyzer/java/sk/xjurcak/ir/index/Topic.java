package sk.xjurcak.ir.index;

/**
 * Created by xjurcak on 11/1/2014.
 */
public class Topic {

    public final static Topic NULL = new Topic(null, null, null);

    private String id;

    private String title;

    private String types;

    public Topic(String id, String title, String types) {
        this.id = id;
        this.title = title;
        this.types = types;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTypes() {
        return types;
    }
    
    public String[] getTypesId(){
        return types.split("<c>");
    }

    public void addType(String s) {
        if(types == null){
            types = s + "<c>";
        } else {
            types += s +"<c>";
        }
    }
}
