package au.org.ridesharingoz.rideshare_oz;

/**
 * Created by Johnny Mao on 15/9/3.
 */
public class User {
    private String username;
    private String password;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private User() {
    }

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
