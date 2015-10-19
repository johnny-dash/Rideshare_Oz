package au.org.ridesharingoz.rideshare_oz.firebasePackage;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;

// Initialize Firebase with the application context. This must happen before the client is used

public class FirebaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
    }
}
