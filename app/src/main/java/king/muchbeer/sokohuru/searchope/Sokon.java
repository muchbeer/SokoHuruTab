package king.muchbeer.sokohuru.searchope;

/**
 * Created by muchbeer on 8/16/2015.
 */
public class Sokon {

    private final String mText;
    private String xname;
    private String xprice;
    private String xdesc;
    private String xcontact;
    private String xlocation;
    private String xusername;
    private String xpid;
    private String xcreated;
    private int xid;
    private String xtitle;
    private String ximage;

    public Sokon(String text,
                 String name,
                 String price,
                 String desc,
                 String contact,
                 String location,
                 String username,
                 String created,
                String image) {
        mText = text;
        xname = name;
        xprice=price;
        xdesc=price;
        xcontact=contact;
        xlocation=location;
        xusername=username;
        xcreated=created;
        ximage = image;

    }

    public String getText() {
        return mText;
    }

    public int getId() {
        return xid;
    }

    public void setId(int id) {
        this.xid = id;
    }

    public String getTitle() {
        return xtitle;
    }

    public void setTitle(String title) {
        this.xtitle = title;
    }

    public String getName() {
        return xname;
    }

    public void setName(String name) {
        this.xname = name;
    }

    public String getPrice() {
        return xprice;
    }

    public void setPrice(String price) {
        this.xprice = price;
    }

    public String getDesc() {
        return xdesc;
    }

    public void setDesc(String desc) {
        this.xdesc = desc;
    }

    public String getContact() {
        return xcontact;
    }

    public void setContact(String contact) {
        this.xcontact = contact;
    }

    public String getLocation() {
        return xlocation;
    }

    public void setLocation(String location) {
        this.xlocation = location;
    }

    public String getUsername() {
        return xusername;
    }

    public void setUsername(String username) {
        this.xusername = username;
    }

    public String getCreated() {
        return xcreated;
    }

    public void setCreated(String created) {
        this.xcreated = created;
    }
    public String getImage() {
        return ximage;
    }

    public void setImage(String image) {
        this.ximage= image;
    }

}
