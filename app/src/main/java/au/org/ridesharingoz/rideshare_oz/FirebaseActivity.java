package au.org.ridesharingoz.rideshare_oz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class FirebaseActivity extends ActionBarActivity {

    public static final String FIREBASE = "https://flickering-inferno-6814.firebaseio.com";

    /* A reference to the Firebase */
    protected Firebase mFirebaseRef;

    /* Data from the authenticated user */
    protected AuthData mAuthData;

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
            }
            if (name != null) {
                Toast.makeText(getApplicationContext(), "Logged in as " + name + " (" + authData.getProvider() + ")",
                        Toast.LENGTH_LONG).show();
            }


            Query usernode = mFirebaseRef.child("users");
            usernode.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(mAuthData.getUid())) {
                        Intent personaldetail = new Intent(getApplicationContext(), RegistrationInfomationActivity.class);
                        startActivity(personaldetail);
                    } else {
                        Intent personaldetail = new Intent(getApplicationContext(), ActionChoiceActivity.class);
                        startActivity(personaldetail);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });


        }
        this.mAuthData = authData;
        /* invalidate options menu to hide/show the logout button */
        supportInvalidateOptionsMenu();
    }

}
