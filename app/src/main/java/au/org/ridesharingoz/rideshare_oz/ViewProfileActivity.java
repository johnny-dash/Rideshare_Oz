package au.org.ridesharingoz.rideshare_oz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import au.org.ridesharingoz.rideshare_oz.R;

public class ViewProfileActivity extends FirebaseAuthenticatedActivity{
    private ListView listView;
    private RatingBar driver;
    private RatingBar passenger;
    private TextView driverRating;
    private TextView passengerRating;
    private Button edit;
    private String firstName;
    private String lastName;
    private String licenseNb;
    private String phoneNb;
    private List<String> data;
    private List<String> rawUserData;
    private float asDriver;
    private float asPassenger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        final String uid = getIntent().getStringExtra("uid");


        Firebase usernode = mFirebaseRef.child("users").child(uid);
        usernode.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = new ArrayList<String>();
                data.add("First Name: " + (String) dataSnapshot.child("firstName").getValue());
                data.add("Last Name: " + (String) dataSnapshot.child("lastName").getValue());
                if (dataSnapshot.hasChild("licenseNb")){
                    data.add("License Number: " + (String) dataSnapshot.child("licenseNb").getValue());
                    data.add("License Type: " + (String) dataSnapshot.child("licenseType").getValue());
                }
                if (uid.equals(mAuthData.getUid())){
                    System.out.println("Do I ever get in here?");
                    data.add("Phone Number: " + (String) dataSnapshot.child("phoneNb").getValue());
                }


                listView.setAdapter(new ArrayAdapter<>(ViewProfileActivity.this, android.R.layout.simple_expandable_list_item_1, data));

                if(dataSnapshot.hasChild("rating")){
                            Rating ratings = dataSnapshot.child("rating").getValue(Rating.class);
                            asDriver = ratings.getAsDriver();
                            asPassenger = ratings.getAsPassenger();
                            driver.setRating(asDriver);
                            passenger.setRating(asPassenger);

                }else {
                    driver.setRating(0.0f);
                    passenger.setRating(0.0f);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        listView = (ListView) findViewById(R.id.listView);
        //Need UserID from prevous activity
        listView.setEnabled(false);
        driverRating = (TextView) findViewById(R.id.textView21);
        driver = (RatingBar) findViewById(R.id.driverRating);
        passengerRating = (TextView) findViewById(R.id.textView22);
        passenger = (RatingBar) findViewById(R.id.passengerRating);


        edit = (Button) findViewById(R.id.edit_profile);

        if(uid.equals(mAuthData.getUid())) {
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewProfileActivity.this, RegistrationInfomationActivity.class);
                    startActivity(intent);
                }
            });

        }

        }


}
