package au.org.ridesharingoz.rideshare_oz;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.location.Geocoder;


import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class SearchRideActivity extends FirebaseAuthenticatedActivity {

    RadioGroup typRadioGroup;

    Pin searchpin;

    Pin[] pins = new Pin[1000];
    Pin[] LocationcheckedPin = new Pin[1000];
    Pin[] TimecheckedPin = new Pin[1000];
    int index = 0;

    int PIN_REQUEST = 1;

    EditText searchdate;
    EditText searchtime;


    Button btn_searchRide;
    Button btn_gotomap;

    String type;


    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateformat();
        }

    };



    private void dateformat() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        searchdate.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride);


        searchdate = (EditText) findViewById(R.id.search_date);
        searchdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SearchRideActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        searchtime = (EditText) findViewById(R.id.search_time);


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

        btn_gotomap = (Button) findViewById(R.id.btn_gotomap);
        btn_gotomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPinFromMap = new Intent(SearchRideActivity.this, MapsActivity.class);
                startActivityForResult(getPinFromMap, PIN_REQUEST);
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


        mFirebaseRef.child("pins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot pinsSnapshot : dataSnapshot.getChildren()) {

                    if (pinsSnapshot.child("date").getValue().toString().equals(searchdate.getText().toString())) {
                        pins[index] = pinsSnapshot.getValue(Pin.class);
                        System.out.println(pins[index].getaddress());
                        index++;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        System.out.println(pins.length);
/*


            LocationcheckedPin = checkLocation(pins);
            for(int i = 0;i<LocationcheckedPin.length;i++){
                //Toast.makeText(getApplicationContext(), "pins: "+pins[i], Toast.LENGTH_SHORT).show();
                System.out.println(LocationcheckedPin[i]);
            }
*/
            //Toast.makeText(getApplicationContext(), "There is no pin match, Please try other condition.", Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplicationContext(),"Latitude: "+location[0]+"Longitude:"+location[1],Toast.LENGTH_SHORT).show();
    }


    public Pin[] checkLocation(Pin[] pins){
        Pin[] checkedpins = new Pin[1000];
        int n = 0;
        System.out.println(pins[0].getlatitude());
        /*
        for(int i = 0;i<pins.length;i++){
            if(pins[i].getlatitude()>=searchpin.getlatitude()-0.001 & pins[i].getlatitude()<=searchpin.getlatitude()+0.001){
                if(pins[i].getlongitude()>=searchpin.getlongitude()-0.001 & pins[i].getlongitude()<=searchpin.getlongitude()+0.001){
                    checkedpins[n] = pins[i];
                    n++;
                }
            }
        }
        */
        return checkedpins;
    }

    public Pin[] checkTime(Pin[] pins,String time){
        return pins;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                searchpin = (Pin) data.getSerializableExtra("pin");
            }
        }


    }
}
