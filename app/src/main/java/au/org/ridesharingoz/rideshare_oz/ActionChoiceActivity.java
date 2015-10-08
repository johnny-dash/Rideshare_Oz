package au.org.ridesharingoz.rideshare_oz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class ActionChoiceActivity extends FirebaseAuthenticatedActivity {

    private Button joingroup = null;
    private Button search = null;
    private Button offer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionchoice);

        joingroup = (Button) findViewById(R.id.button_joingroup);


        joingroup.setOnClickListener(buttonListener);

        Query usergroupnode = mFirebaseRef.child("users").child("groupsJoined");
        usergroupnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    search = (Button) findViewById(R.id.button_searchride);
                    offer = (Button) findViewById(R.id.button_offerride);
                    search.setVisibility(View.VISIBLE);
                    offer.setVisibility(View.VISIBLE);
                    search.setOnClickListener(buttonListener);
                    offer.setOnClickListener(buttonListener);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

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