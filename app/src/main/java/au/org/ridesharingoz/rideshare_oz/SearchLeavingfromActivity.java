package au.org.ridesharingoz.rideshare_oz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import au.org.ridesharingoz.rideshare_oz.R;

public class SearchLeavingfromActivity extends FirebaseAuthenticatedActivity {

    Pin searchpin;

    List<Pin> pins = new ArrayList<Pin>();
    List<Ride> rides = new ArrayList<Ride>();

    List<String> rideID = new ArrayList<>();
    String groupname;
    String eventname;
    String type;

    EditText searchdate;
    EditText searchtime;

    Button btn_searchRide;

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

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            timeformat();
        }
    };


    private void dateformat() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        searchdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void timeformat(){
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        searchtime.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_leavingfrom);
        final Bundle bundle = getIntent().getExtras();

        if (bundle!= null){
            groupname = bundle.getString("Group");
            eventname = bundle.getString("Event");
            //type = bundle.getString("type");
            type = "leavingfrom";
        }


        searchdate = (EditText) findViewById(R.id.search_date);
        searchdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SearchLeavingfromActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        searchtime = (EditText) findViewById(R.id.search_time);
        searchtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(SearchLeavingfromActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
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
        getMenuInflater().inflate(R.menu.menu_search_leavingfrom, menu);
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
        mFirebaseRef.child("leavingfromrides").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ridesSnapshot : dataSnapshot.getChildren()) {
                    rides.add(ridesSnapshot.getValue(Ride.class));
                    rideID.add(ridesSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mFirebaseRef.child("leavingfrompins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ridesSnapshot : dataSnapshot.getChildren()) {
                    rides.add(ridesSnapshot.getValue(Ride.class));
                    rideID.add(ridesSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public List<Pin> checkLocation(List<Pin> pins){

        List<Pin> checkedpins = new ArrayList<Pin>();
        System.out.println("target");
        System.out.println("latitude:"+searchpin.getlatitude()+",longitude:"+ searchpin.getlongitude());
        for(Pin pin:pins){
            System.out.println("Result");
            System.out.println("latitude:"+pin.getlatitude()+",longitude:"+ pin.getlongitude());
            if(pin.getlatitude()>=searchpin.getlatitude()-0.025 & pin.getlatitude()<=searchpin.getlatitude()+0.025){
                if(pin.getlongitude()>=searchpin.getlongitude()-0.025 & pin.getlongitude()<=searchpin.getlongitude()+0.025){
                    checkedpins.add(pin);

                }
            }
        }
        return checkedpins;
    }

    public List<String> checkTime(List<Ride> rides) {
        List<String> checkedrides = new ArrayList<String>();
        String date = searchdate.getText().toString();
        String time = searchtime.getText().toString();
        int index = 0;
        try {
            Date checkdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date + " " + time);
            for (Ride ride : rides) {
                Date ridedate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(ride.getDate()+ " "+ ride.getTime());
                if(Math.abs(ridedate.getTime() - checkdate.getTime())/(3600*16) <= 30 ){
                    checkedrides.add(rideID.get(index));
                }
                index++;
            }

        }catch (ParseException ex){

        }
        return checkedrides;
    }

    public List<Pin> compare(List<String> rides, List<Pin> pins){
        List<Pin> checkpin = new ArrayList<Pin>();
        for(Pin pin:pins){
            for(String rideID:rides){
                if(pin.getrideID() == rideID){
                    checkpin.add(pin);
                }
            }
        }
        return checkpin;
    }

}



