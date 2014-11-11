package sk.xjurcak.ir.index;

/**
 * Created by xjurcak on 10/31/2014.
 */
public class Content {

    public final static Content NULL = new Content(null, null);

    private String id;

    private String title;

    public Content(String id, String title) {
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
