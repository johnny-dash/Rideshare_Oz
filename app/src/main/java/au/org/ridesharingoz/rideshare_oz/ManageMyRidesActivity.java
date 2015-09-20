package au.org.ridesharingoz.rideshare_oz;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

/**
 * Created by Ocunidee on 20/09/2015.
 */
public class ManageMyRidesActivity extends FirebaseAuthenticatedActivity{

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
           /* switch (v.getId()) {
                case R.id.button_joingroup:
                    intent = new Intent(ManageMyRidesActivity.this, JoinGroupActivity.class);
                    break;
                case R.id.button_searchride:
                    //intent = new Intent(ActionChoiceActivity.this, SelectGroup.class);
                    break;
                case R.id.button_offerride:
                    intent = new Intent(ActionChoiceActivity.this, ChooseTypeRideActivity.class);
                    break;
            } */
            startActivity(intent);
        }

    };
}
