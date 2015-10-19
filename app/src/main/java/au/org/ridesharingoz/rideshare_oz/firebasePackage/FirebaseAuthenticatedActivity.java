package au.org.ridesharingoz.rideshare_oz.firebasePackage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import au.org.ridesharingoz.rideshare_oz.userPackage.ActionChoiceActivity;
import au.org.ridesharingoz.rideshare_oz.userPackage.LoginActivity;
import au.org.ridesharingoz.rideshare_oz.groupsPackage.ManageMyGroupsActivity;
import au.org.ridesharingoz.rideshare_oz.ridesPackage.ManageMyRidesActivity;
import au.org.ridesharingoz.rideshare_oz.R;
import au.org.ridesharingoz.rideshare_oz.userPackage.ViewProfileActivity;

public class FirebaseAuthenticatedActivity extends FirebaseActivity {

    Firebase mUserRef;

    GoogleCloudMessaging gcm;
    final static String PROJECT_NUMBER = "523906066693";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase
        mFirebaseRef = new Firebase(FIREBASE);
        mAuthData    = mFirebaseRef.getAuth();

        // Process authentication
        if (mAuthData == null) {
            //Log.w(TAG, "Users must be authenticated to do this activity. Redirecting to login activity.");
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
        }

        // Process user reference
        mUserRef = mFirebaseRef.child("users").child(mAuthData.getUid());
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child("gcmID").getValue() == null) {
                    getRegId();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // If the user is authenticated, show the app menu for authenticated users
        if (this.mAuthData != null) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle all options in the app menu
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        else if (id == R.id.my_profile) {
            displayProfile();
            return true;
        }
        else if (id == R.id.home) {
            displayHome();
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

    // Unauthenticate from Firebase and from providers where necessary.
    private void logout() {
        if (this.mAuthData != null) {

            // Logout of Firebase
            mFirebaseRef.unauth();

            /* Logout of any of the Frameworks. This step is optional, but ensures the user is not logged into
             * Facebook/Google+ after logging out of Firebase. */
            if (this.mAuthData.getProvider().equals("facebook")) {
                // Logout from Facebook
                LoginManager.getInstance().logOut();
            }

            // Update authenticated user and show login buttons
            setAuthenticatedUser(null);

            // Go back to login activity
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
        }
    }

    // Displays the user's profile
    private void displayProfile() {
        Intent profileIntent = new Intent(getApplicationContext(), ViewProfileActivity.class);
        profileIntent.putExtra("uid", mAuthData.getUid());
        startActivity(profileIntent);
        finish();
    }

    // Displays the view from which the user can manage his/her ride
    private void displayRides() {
        Intent myRidesIntent = new Intent(getApplicationContext(), ManageMyRidesActivity.class);
        startActivity(myRidesIntent);
        finish();
    }

    // Displays the view from which the user can manage his/her groups
    private void displayGroups() {
        Intent myGroupsIntent = new Intent(getApplicationContext(), ManageMyGroupsActivity.class);
        startActivity(myGroupsIntent);
        finish();
    }

    // Displays the home view
    private void displayHome() {
        Intent homeIntent = new Intent(getApplicationContext(), ActionChoiceActivity.class);
        startActivity(homeIntent);
        finish();
    }

    // Get the Google Cloud Messaging registration ID and save it to Firebase (for notifications)
    public void getRegId() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    msg = gcm.register(PROJECT_NUMBER);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Map<String, Object> data = new HashMap<>();
                data.put("gcmID", msg);
                mUserRef.updateChildren(data);
            }
        }.execute(null, null, null);
    }

}
