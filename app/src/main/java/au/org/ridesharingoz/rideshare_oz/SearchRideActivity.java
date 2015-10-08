package au.org.ridesharingoz.rideshare_oz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;



import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class SearchRideActivity extends FirebaseAuthenticatedActivity {

    RadioGroup typRadioGroup;

    Pin searchpin;

    List<Pin> pins = new ArrayList<Pin>();
    List<Pin> LocationcheckedPin = new ArrayList<Pin>();
    List<Pin> TimecheckedPin = new ArrayList<Pin>();


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
        searchtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(SearchRideActivity.this,time,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),true).show();
            }
        });



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


        //Query myquery = mFirebaseRef.child("pins").orderByChild("timestamp").startAt(start.getTime()).endAt(end.getTime());

        mFirebaseRef.child("pins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot pinsSnapshot : dataSnapshot.getChildren()) {

                    //if (pinsSnapshot.child("date").getValue().toString().equals(searchdate.getText().toString())) {
                        pins.add(pinsSnapshot.getValue(Pin.class));
                    //}
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



            LocationcheckedPin = checkLocation(pins);
            TimecheckedPin = checkTime(LocationcheckedPin);
            for(Pin pin:LocationcheckedPin){
                //Toast.makeText(getApplicationContext(), "pins: "+pins[i], Toast.LENGTH_SHORT).show();
                System.out.println("pins have finded: "+pin.getaddress());
            }


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

    public List<Pin> checkTime(List<Pin> pins) {
        List<Pin> checkedpins = new ArrayList<Pin>();
        String date = searchdate.getText().toString();
        String time = searchtime.getText().toString();
        try {
            Date checkdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date + " " + time);
            for (Pin pin : pins) {

                if(Math.abs(pin.getTimestamp().getTime() - checkdate.getTime())/(3600*16) <= 30 ){
                    checkedpins.add(pin);
                }
            }

        }catch (ParseException ex){

        }
        return checkedpins;
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
