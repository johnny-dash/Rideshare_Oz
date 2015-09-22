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
    
}
