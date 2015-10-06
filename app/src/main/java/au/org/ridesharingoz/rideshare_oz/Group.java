package au.org.ridesharingoz.rideshare_oz;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ocunidee on 20/09/2015.
 */
public class Group {

    private String groupName;
    private String groupDescription;
    private String groupOwner;
    private String pinID;
    private Map<String, Boolean> events;
    private String category;
    private Boolean privateGroup;

    public Group(){}

    public Group(String groupName, String groupDescription, String category, String pinID, Boolean privateGroup){
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.category= category;
        this.pinID=pinID;
        this.privateGroup=privateGroup;
        this.groupOwner="";
        this.events= new HashMap<>();
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

    public Map<String, Boolean> getEvents() {
        return events;
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

    public void setEvents(Map<String, Boolean> events) {
        this.events = events;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getPrivateGroup() {
        return privateGroup;
    }

    public void setPrivateGroup(Boolean privateGroup) {
        this.privateGroup = privateGroup;
    }
}
