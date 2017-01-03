package krajetum.LTB.objects;

import java.util.Date;

/**
 * Created by Lorenzo on 12/12/2016.
 */
public class BannedMember {

    public String name;
    public Date date;
    public BannedMember(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
