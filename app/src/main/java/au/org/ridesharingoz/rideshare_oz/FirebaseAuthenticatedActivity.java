package au.org.ridesharingoz.rideshare_oz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.Firebase;

public class FirebaseAuthenticatedActivity extends FirebaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase
        mFirebaseRef = new Firebase(FIREBASE);

        // Process authentication
        Bundle extras = getIntent().getExtras();
        String authToken;
        if (extras != null) {
            authToken = extras.getString(LoginActivity.AUTH_TOKEN_EXTRA);
        } else {
            //Log.w(TAG, "Users must be authenticated to do this activity. Redirecting to login activity.");
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
            return;
        }

    }

}

