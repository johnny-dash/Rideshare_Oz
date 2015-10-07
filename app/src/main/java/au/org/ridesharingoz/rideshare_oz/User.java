package au.org.ridesharingoz.rideshare_oz;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.util.Map;

/**
 * Created by Ocunidee on 20/09/2015.
 */
public class User {

    private String firstName;
    private String lastName;
    private String licenseNb;
    private String phoneNb;
    private Map<String, Boolean> groupsJoined;
    private String licenseType;
    private Map<String, Boolean> ridesJoined;
    private Map<String, Boolean> ridesOffered;
    private Map<String, Boolean> groupsOwned;





    public User() {}

    public User(String firstName, String lastName, String phoneNb, String licenseNb, String licenseType){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNb = phoneNb;
        this.licenseNb = licenseNb;
        this.licenseType = licenseType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLicenseNb() {
        return licenseNb;
    }

    public void setLicenseNb(String licenseNb) {
        this.licenseNb = licenseNb;
    }

    public String getPhoneNb() {
        return phoneNb;
    }

    public void setPhoneNb(String phoneNb) {
        this.phoneNb = phoneNb;
    }

    public Map<String, Boolean> getGroupsJoined() {
        return groupsJoined;
    }

    public void setGroupsJoined(Map<String, Boolean> groupsJoined) {
        this.groupsJoined = groupsJoined;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public Map<String, Boolean> getRidesJoined() {
        return ridesJoined;
    }

    public void setRidesJoined(Map<String, Boolean> ridesJoined) {
        this.ridesJoined = ridesJoined;
    }

    public Map<String, Boolean> getRidesOffered() {
        return ridesOffered;
    }

    public void setRidesOffered(Map<String, Boolean> ridesOffered) {
        this.ridesOffered = ridesOffered;
    }

    public Map<String, Boolean> getGroupsOwned() {
        return groupsOwned;
    }

    public void setGroupsOwned(Map<String, Boolean> groupsOwned) {
        this.groupsOwned = groupsOwned;
    }


}
