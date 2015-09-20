package au.org.ridesharingoz.rideshare_oz;

import com.firebase.client.Firebase;

import java.util.Map;

/**
 * Created by Ocunidee on 20/09/2015.
 */
public class User {

    private String firstName;
    private String lastName;
    private int licenseNb;
    private String email;
    private Map<String, Group> groups;
    private String licenseType;
    private Map<String, Ride> rides;

    Firebase ref = new Firebase("https://flickering-inferno-6814.firebaseio.com/");

    public User() {}

    public User(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    /*Firebase alanRef = ref.child("users").child("alanisawesome");
    User alan = new User("Alan", "Turing", "aturing@gmail.com");
    alanRef.setValue(alan);*/

}
