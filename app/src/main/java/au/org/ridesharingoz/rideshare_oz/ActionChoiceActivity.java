package au.org.ridesharingoz.rideshare_oz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class ActionChoiceActivity extends FirebaseAuthenticatedActivity {

    private Button joingroup = null;
    private Button search = null;
    private Button offer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionchoice);

        joingroup = (Button) findViewById(R.id.button_joingroup);
        search = (Button) findViewById(R.id.button_searchride);
        offer = (Button) findViewById(R.id.button_offerride);

        joingroup.setOnClickListener(buttonListener);
        search.setOnClickListener(buttonListener);
        offer.setOnClickListener(buttonListener);
    }


    public View.OnClickListener buttonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.button_joingroup:
                    intent = new Intent(getApplicationContext(), JoinGroupActivity.class);
                    break;
                case R.id.button_searchride:
                    intent = new Intent(getApplicationContext(), ChooseGroupEventActivity.class);
                    intent.putExtra("from", "search");
                    break;
                case R.id.button_offerride:
                    intent = new Intent(getApplicationContext(), ChooseGroupEventActivity.class);
                    intent.putExtra("from", "offer");
                    break;
            }
            startActivity(intent);
        }

    };


}