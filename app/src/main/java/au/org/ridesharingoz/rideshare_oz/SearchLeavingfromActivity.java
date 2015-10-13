package au.org.ridesharingoz.rideshare_oz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import au.org.ridesharingoz.rideshare_oz.R;

public class SearchLeavingfromActivity extends FirebaseAuthenticatedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_leavingfrom);
    }

}
