package au.org.ridesharingoz.rideshare_oz;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.client.Firebase;

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
            return;
        }


        if (false) {
            Intent loginIntent = new Intent(getApplicationContext(), RegistrationInfomationActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

    }

}

