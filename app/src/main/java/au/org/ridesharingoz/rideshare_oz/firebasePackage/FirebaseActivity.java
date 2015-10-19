package au.org.ridesharingoz.rideshare_oz.firebasePackage;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import au.org.ridesharingoz.rideshare_oz.userPackage.ActionChoiceActivity;
import au.org.ridesharingoz.rideshare_oz.userPackage.RegistrationInfomationActivity;

public class FirebaseActivity extends ActionBarActivity {

    // The Firebase project URL
    public static final String FIREBASE = "https://flickering-inferno-6814.firebaseio.com";

    protected Firebase mFirebaseRef;
    protected AuthData mAuthData;

    // Once a user is logged in, take the mAuthData provided from Firebase and "use" it
    protected void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {

            // Hide all the login buttons and show a provider specific status text
            String name = null;
            if (authData.getProvider().equals("facebook")) {
                name = (String) authData.getProviderData().get("displayName");
            } else if (authData.getProvider().equals("anonymous")
                    || authData.getProvider().equals("password")) {
                name = authData.getUid();
            }
            if (name != null) {
                Toast.makeText(getApplicationContext(),
                        "Logged in as " + name + " (" + authData.getProvider() + ")",
                        Toast.LENGTH_LONG).show();
            }

            // If user information has not been set, then start the user off there
            Query userNode = mFirebaseRef.child("users");
            userNode.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(mAuthData.getUid())) {
                        Intent personalDetail = new Intent(getApplicationContext(),
                                RegistrationInfomationActivity.class);
                        personalDetail.putExtra("callingActivity", "FirebaseActivity");
                        startActivity(personalDetail);

                    } else {
                        Intent actionChoice = new Intent(getApplicationContext(),
                                ActionChoiceActivity.class);
                        startActivity(actionChoice);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });


        }
        this.mAuthData = authData;
        supportInvalidateOptionsMenu();
    }

}
