package krajetum.LTB.objects;

/**
 *
 *  This class is used to parse the lug_member.json file in order to get the members of the group
 *
 *  @version 1.0
 *  @since 1.0
 */
public class LUGMember {

    private String name;
    private boolean active;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
