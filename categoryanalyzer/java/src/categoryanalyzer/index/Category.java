package categoryanalyzer.index;

/**
 * Created by xjurcak on 10/31/2014.
 */
public class Category {

    public final static Category NULL = new Category(null, null);

    private String id;

    private String title;

    public Category(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
