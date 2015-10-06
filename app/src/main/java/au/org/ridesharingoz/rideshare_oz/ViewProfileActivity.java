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
    private float asDriver;
    private float asPassenger;
    private String uID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        //Need user ID
        uID = "13350bdf-9afa-45d4-9a87-35e307973b05";

        Firebase userRef = new Firebase("https://flickering-inferno-6814.firebaseio.com/users/" + uID );
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = new ArrayList<String>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    data.add(userSnapshot.getKey() + " : " + userSnapshot.getValue());
                }
//check data
//                for (int i = 0; i < data.size(); i++) {
//                    System.out.println(data.get(i));
//                }

                /*Haven't check if the user in one ride
                  set the phone number invisible
                 */

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

        if(uID.equals(mAuthData.getUid()) ) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
