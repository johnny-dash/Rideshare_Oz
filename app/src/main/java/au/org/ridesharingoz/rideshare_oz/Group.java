package au.org.ridesharingoz.rideshare_oz;

import java.util.Map;

/**
 * Created by Ocunidee on 20/09/2015.
 */
public class Group {

    private String groupName;
    private String groupDescription;
    private String groupOwner;
    private String pinID;
    private Map<String, Boolean> event;
    private String category;
    private Boolean privateGroup;

    public Group(){}

    public Group(String groupName, String groupDescription, String category, String pinID, Boolean privateGroup){
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.category= category;
        this.pinID=pinID;
        this.privateGroup=privateGroup;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public String getPinID() {
        return pinID;
    }

    public Map<String, Boolean> getEvent() {
        return event;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public void setPinID(String pinID) {
        this.pinID = pinID;
    }

    public void setEvent(Map<String, Boolean> event) {
        this.event = event;
    }
}
