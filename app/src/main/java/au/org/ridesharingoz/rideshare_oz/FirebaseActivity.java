package au.org.ridesharingoz.rideshare_oz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class FirebaseActivity extends ActionBarActivity {

    public static final String FIREBASE = "https://flickering-inferno-6814.firebaseio.com";
    public static final String AUTH_TOKEN_EXTRA = "authToken";
    public static final String AUTH_PROVIDER_EXTRA = "authProvider";

    /* A reference to the Firebase */
    protected Firebase mFirebaseRef;

    /* Data from the authenticated user */
    protected AuthData mAuthData;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* If a user is currently authenticated, display a logout menu */
        if (this.mAuthData != null) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        else if (id == R.id.my_profile) {
            displayProfile();
            return true;
        }
        else if (id == R.id.my_groups) {
            displayGroups();
            return true;
        }
        else if (id == R.id.my_rides){
            displayRides();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Unauthenticate from Firebase and from providers where necessary.
     */
    private void logout() {
        if (this.mAuthData != null) {
            /* logout of Firebase */
            mFirebaseRef.unauth();
            /* Logout of any of the Frameworks. This step is optional, but ensures the user is not logged into
             * Facebook/Google+ after logging out of Firebase. */
            if (this.mAuthData.getProvider().equals("facebook")) {
                /* Logout from Facebook */
                LoginManager.getInstance().logOut();
            }
            /* Update authenticated user and show login buttons */
            setAuthenticatedUser(null);

            // Go back to login activity
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
            return;
        }
    }

    /**
     * Once a user is logged in, take the mAuthData provided from Firebase and "use" it.
     */
    protected void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {
            /* Hide all the login buttons */
            /* show a provider specific status text */
            String name = null;
            if (authData.getProvider().equals("facebook")) {
                name = (String) authData.getProviderData().get("displayName");
            } else if (authData.getProvider().equals("anonymous")
                    || authData.getProvider().equals("password")) {
                name = authData.getUid();
            } else {
                //Log.e(TAG, "Invalid provider: " + authData.getProvider());
            }
            if (name != null) {
                Toast.makeText(getApplicationContext(), "Logged in as " + name + " (" + authData.getProvider() + ")",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, ActionChoiceActivity.class);
                startActivity(intent);
            }
        } else {
            /* No authenticated user show all the login buttons */
        }
        this.mAuthData = authData;
        /* invalidate options menu to hide/show the logout button */
        supportInvalidateOptionsMenu();
    }

    /* displays the user's profile */
    private void displayProfile() {
        Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(profileIntent);
        return;
    }

    /* displays the view from which the user can manage his/her ride*/
    private void displayRides() {
        Intent myRidesIntent = new Intent(getApplicationContext(), ManageMyRidesActivity.class);
        startActivity(myRidesIntent);
        return;
    }

    /* displays the view from which the user can manage his/her groups */
    private void displayGroups() {
        Intent myGroupsIntent = new Intent(getApplicationContext(), ManageMyGroupsActivity.class);
        startActivity(myGroupsIntent);
        return;
    }

}
