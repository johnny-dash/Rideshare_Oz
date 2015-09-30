package au.org.ridesharingoz.rideshare_oz;

import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.location.Geocoder;


import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.util.List;


public class SearchRideActivity extends FirebaseAuthenticatedActivity {

    double[] location = new double[2];

    RadioGroup typRadioGroup;

    Pin[] pins;
    int index = 0;

    EditText searchdate;
    EditText searchtime;
    EditText searchaddress;

    Button btn_searchRide;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride);


        searchdate = (EditText) findViewById(R.id.search_date);
        searchtime = (EditText) findViewById(R.id.search_time);
        searchaddress = (EditText) findViewById(R.id.search_address);

        typRadioGroup = (RadioGroup) findViewById(R.id.SearchTypeRadioGroup);
        typRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.sGoingto) {
                    type = "going to";
                } else if (checkedId == R.id.sLeavingfrom) {
                    type = "leaving from";
                } else {
                    Toast.makeText(SearchRideActivity.this, "Please select a type!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_searchRide = (Button) findViewById(R.id.btn_search);
        btn_searchRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRide();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_ride, menu);
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

    public void searchRide(){
        String address = searchaddress.getText().toString();
        location = getLocationFromAddress(address);


        mFirebaseRef.child("pins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot pinsSnapshot:dataSnapshot.getChildren()){
                    if (pinsSnapshot.child("date").getValue() == searchdate.getText().toString())
                    pins[index]  = pinsSnapshot.getValue(Pin.class);
                    Toast.makeText(getApplicationContext(),"I get:"+pins[index],Toast.LENGTH_SHORT).show();
                    index ++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Toast.makeText(getApplicationContext(),"Latitude: "+location[0]+"Longitude:"+location[1],Toast.LENGTH_SHORT).show();
    }

    public double[] getLocationFromAddress(String strAddress) {
        double[] latitudeandlongtitude = new double[2];
        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            Address location = address.get(0);
            latitudeandlongtitude[0] = location.getLatitude();
            latitudeandlongtitude[1] = location.getLongitude();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return  latitudeandlongtitude;
    }

}
