package au.org.ridesharingoz.rideshare_oz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Ocunidee on 20/09/2015.
 */
public class ManageMyGroupsActivity extends FirebaseAuthenticatedActivity{

    private Button createagroup = null;
    private Button joinedgroups = null;
    private Button ownedgroups = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_rides);

        createagroup = (Button) findViewById(R.id.button_createagroup);


        createagroup.setOnClickListener(buttonListener);
        joinedgroups.setOnClickListener(buttonListener);
        ownedgroups.setOnClickListener(buttonListener);
    }


    public View.OnClickListener buttonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.button_createagroup:
                    intent = new Intent(ManageMyGroupsActivity.this, CreateAGroupActivity.class);
                    break;
            }
            startActivity(intent);
        }

    };

}
