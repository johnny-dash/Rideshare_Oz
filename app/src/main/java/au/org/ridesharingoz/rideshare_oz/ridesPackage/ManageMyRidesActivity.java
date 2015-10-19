package au.org.ridesharingoz.rideshare_oz.ridesPackage;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

        import au.org.ridesharingoz.rideshare_oz.R;
        import au.org.ridesharingoz.rideshare_oz.firebasePackage.FirebaseAuthenticatedActivity;

/**
 * Created by Ocunidee on 20/09/2015.
 */
public class ManageMyRidesActivity extends FirebaseAuthenticatedActivity {

    private Button joinedrides = null;
    private Button offeredrides = null;
    private Button toraterides = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_rides);

        joinedrides = (Button) findViewById(R.id.button_joinedrides);
        offeredrides = (Button) findViewById(R.id.button_offeredrides);
        toraterides = (Button) findViewById(R.id.button_toraterides);

        joinedrides.setOnClickListener(buttonListener);
        offeredrides.setOnClickListener(buttonListener);
        toraterides.setOnClickListener(buttonListener);
    }


    public View.OnClickListener buttonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.button_joinedrides:
                    intent = new Intent(ManageMyRidesActivity.this, JoinedRidesActivity.class);
                    break;
                case R.id.button_offeredrides:
                    intent = new Intent(ManageMyRidesActivity.this, OfferedRidesActivity.class);
                    break;
                case R.id.button_toraterides:
                    intent = new Intent(ManageMyRidesActivity.this, ToRateRidesActivity.class);
                    break;
            }
            startActivity(intent);
        }

    };
}
