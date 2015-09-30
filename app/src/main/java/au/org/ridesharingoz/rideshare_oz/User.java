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
    private Map<String, Group> groups;
    private String licenseType;
    private Map<String, Ride> rides;
    private Map<String, Boolean> groupOwned;
    private int driverRating;
    private int nbdriverRatings;
    private int pasengerRating;




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

    public String getLastName() {
        return lastName;
    }

    public String getLicenseNb() {
        return licenseNb;
    }

    public String getPhoneNb() {
        return phoneNb;
    }

    public Map<String, Group> getGroups() {
        return groups;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public Map<String, Ride> getRides() {
        return rides;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLicenseNb(String licenseNb) {
        this.licenseNb = licenseNb;
    }

    public void setPhoneNb(String phoneNb) {
        this.phoneNb = phoneNb;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public void setGroups(Map<String, Group> groups) {
        this.groups = groups;
    }

    public void setRides(Map<String, Ride> rides) {
        this.rides = rides;
    }

    public Map<String, Boolean> getGroupOwned() {
        return groupOwned;
    }

    public void setGroupOwned(Map<String, Boolean> groupOwned) {
        this.groupOwned = groupOwned;
    }
}
