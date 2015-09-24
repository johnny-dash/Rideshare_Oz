package au.org.ridesharingoz.rideshare_oz;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.Arrays;

public class FirebaseAuthenticatedActivity extends FirebaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase
        mFirebaseRef = new Firebase(FIREBASE);
        mAuthData = mFirebaseRef.getAuth();

        // Process authentication
        if (mAuthData == null) {
            //Log.w(TAG, "Users must be authenticated to do this activity. Redirecting to login activity.");
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
        }

    }

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
        }
    }

    /* displays the user's profile */
    private void displayProfile() {
        Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(profileIntent);
        finish();
    }

    /* displays the view from which the user can manage his/her ride*/
    private void displayRides() {
        Intent myRidesIntent = new Intent(getApplicationContext(), ManageMyRidesActivity.class);
        startActivity(myRidesIntent);
        finish();
    }

    /* displays the view from which the user can manage his/her groups */
    private void displayGroups() {
        Intent myGroupsIntent = new Intent(getApplicationContext(), ManageMyGroupsActivity.class);
        startActivity(myGroupsIntent);
        finish();
    }

}

