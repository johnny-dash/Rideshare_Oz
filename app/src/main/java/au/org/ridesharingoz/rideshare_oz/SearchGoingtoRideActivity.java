package au.org.ridesharingoz.rideshare_oz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SearchGoingtoRideActivity extends FirebaseAuthenticatedActivity {


    Pin searchpin;
    Map<String,Pin> pins = new HashMap<>();
    Map<String,Pin> LocationcheckedPin = new HashMap<>();
    Map<String,Pin> TimecheckedPin = new HashMap<>();
    Map<String,Pin> GroupcheckedPin = new HashMap<>();

    String groupEventID;
    Boolean isEvent;
    String type;


    int PIN_REQUEST = 1;

    EditText searchdate;
    EditText searchtime;


    Button btn_searchRide;
    Button btn_gotomap;




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
        setContentView(R.layout.activity_search_goingto_ride);
        final Bundle bundle = getIntent().getExtras();

        if (bundle!= null){
            groupEventID = bundle.getString("ID");
            isEvent = bundle.getBoolean("isEvent");

            type = "goingto";
        }


        searchdate = (EditText) findViewById(R.id.search_date);
        searchdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SearchGoingtoRideActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        searchtime = (EditText) findViewById(R.id.search_time);
        searchtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(SearchGoingtoRideActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });



        btn_gotomap = (Button) findViewById(R.id.btn_gotomap);
        btn_gotomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPinFromMap = new Intent(SearchGoingtoRideActivity.this, ChooseLocationActivity.class);
                getPinFromMap.putExtra("callingActivity","SearchGoingtoRideActivity");
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

    public void searchRide(){


        mFirebaseRef.child("goingtopins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot pinsSnapshot : dataSnapshot.getChildren()) {
                    pins.put(pinsSnapshot.getKey(), pinsSnapshot.getValue(Pin.class));

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        LocationcheckedPin = checkLocation(pins);
        TimecheckedPin = checkTime(LocationcheckedPin);
        GroupcheckedPin = checkGroup(TimecheckedPin);
        for(Map.Entry<String, Pin> pin : GroupcheckedPin.entrySet()){

            System.out.println("pins have finded: "+pin.getValue().getaddress());
        }
        if(!GroupcheckedPin.isEmpty()) {
            Intent intent = new Intent(SearchGoingtoRideActivity.this, RideSearchResultActivity.class);
            ArrayList<String> pinID = new ArrayList();
            for(Map.Entry<String, Pin> pin : GroupcheckedPin.entrySet()){
                pinID.add(pin.getKey());
            }
            intent.putExtra("pins",pinID);
            startActivity(intent);
            finish();
        }
        else {

            Toast.makeText(getApplicationContext(), "Don't find any match pins. Please change the condition and search again: )", Toast.LENGTH_SHORT).show();
        }



    }


    public Map<String,Pin> checkLocation(Map<String,Pin> pins){

        Map<String,Pin> checkedpins = new HashMap<>();
        System.out.println("target");
        System.out.println("latitude:"+searchpin.getlatitude()+",longitude:"+ searchpin.getlongitude());
        for(Map.Entry<String, Pin> pin : pins.entrySet()){
            System.out.println("Result");
            System.out.println("latitude:"+pin.getValue().getlatitude()+",longitude:"+ pin.getValue().getlongitude());
            if(pin.getValue().getlatitude()>=searchpin.getlatitude()-0.05 & pin.getValue().getlatitude()<=searchpin.getlatitude()+0.05){
                if(pin.getValue().getlongitude()>=searchpin.getlongitude()-0.05 & pin.getValue().getlongitude()<=searchpin.getlongitude()+0.05){
                    checkedpins.put(pin.getKey(), pin.getValue());
                    System.out.println("Pass location check");
                }
            }
        }
        return checkedpins;
    }

    public Map<String,Pin> checkTime(Map<String,Pin> pins) {
        Map<String,Pin> checkedpins = new HashMap<>();
        String date = searchdate.getText().toString();
        String time = searchtime.getText().toString();
        try {
            Date checkdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date + " " + time);
            for (Map.Entry<String, Pin> pin : pins.entrySet()) {
                double test = Math.abs(pin.getValue().getTimestamp().getTime() - checkdate.getTime())/(3600*16);
                System.out.println(test);
                if(Math.abs(pin.getValue().getTimestamp().getTime() - checkdate.getTime())/(3600*16) <= 30 ){
                    checkedpins.put(pin.getKey(), pin.getValue());
                    System.out.println("pass time checked");
                }
            }

        }catch (ParseException ex){

        }
        return checkedpins;
    }

    public Map<String,Pin> checkGroup(Map<String,Pin> pins){
        Map<String,Pin> checkedpins = new HashMap<>();
        for (Map.Entry<String, Pin> pin : pins.entrySet()){
            if(pin.getValue().getGroupEventID().equals(groupEventID)){
                    checkedpins.put(pin.getKey(),pin.getValue());

            }
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
