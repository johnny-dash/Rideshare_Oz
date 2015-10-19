package au.org.ridesharingoz.rideshare_oz.ridesPackage;

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
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import au.org.ridesharingoz.rideshare_oz.R;
import au.org.ridesharingoz.rideshare_oz.dataPackage.Pin;
import au.org.ridesharingoz.rideshare_oz.firebasePackage.FirebaseAuthenticatedActivity;
import au.org.ridesharingoz.rideshare_oz.mapsPackage.ChooseLocationActivity;

public class SearchLeavingfromActivity extends FirebaseAuthenticatedActivity {

    Pin searchpin;
    int PIN_REQUEST = 1;
    Boolean time_flag = false;
    Boolean location_flag = false;

    ArrayList<String> pinIDArray = new ArrayList();
    String groupname;
    String eventname;
    String type;

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
        setContentView(R.layout.activity_search_leavingfrom);
        final Bundle bundle = getIntent().getExtras();

        if (bundle!= null){
            groupname = bundle.getString("Group");
            eventname = bundle.getString("Event");
            //type = bundle.getString("type");
            type = "leavingfrom";
        }


        searchdate = (EditText) findViewById(R.id.search_leaving_date);
        searchdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SearchLeavingfromActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        searchtime = (EditText) findViewById(R.id.search_leaving_time);
        searchtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(SearchLeavingfromActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });


        btn_searchRide = (Button) findViewById(R.id.btn_leaving_search);
        btn_searchRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRide();
            }
        });

        btn_gotomap = (Button) findViewById(R.id.btn_leaving_gotomap);
        btn_gotomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPinFromMap = new Intent(SearchLeavingfromActivity.this, ChooseLocationActivity.class);
                getPinFromMap.putExtra("callingActivity", "SearchLeavingfromActivity");
                startActivityForResult(getPinFromMap, PIN_REQUEST);
            }
        });
    }



    public void searchRide(){
        final Firebase RideRef = mFirebaseRef.child("leavingfromrides");
        final Firebase PinRef = mFirebaseRef.child("leavingfrompins");


        RideRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ridesSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("test"+ridesSnapshot.child("timestamp").getValue());
                    Timestamp checktimestamp = new Timestamp(Long.parseLong(ridesSnapshot.child("timestamp").getValue().toString()));
                    time_flag = checkTime(checktimestamp);
                    String rideID = ridesSnapshot.getKey();
                    if (time_flag) {
                        RideRef.child(rideID).child("pins").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot pinsSanpshot : dataSnapshot.getChildren()) {
                                    final String pinID = pinsSanpshot.getKey();
                                    PinRef.child(pinID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            location_flag = checkLocation(Double.parseDouble(dataSnapshot.child("latitude").getValue().toString()),
                                                                          Double.parseDouble(dataSnapshot.child("longitude").getValue().toString()));
                                            if(location_flag){
                                                System.out.println("pin has been searched");
                                                pinIDArray.add(dataSnapshot.getKey());
                                            }

                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                    if(!pinIDArray.isEmpty()){
                        Intent intent = new Intent(SearchLeavingfromActivity.this,RideSearchResultActivity.class);
                        intent.putExtra("pins",pinIDArray);
                        intent.putExtra("Type","leavingfrom");
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Did not find matched ride.Please change the condition and search again:)", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


    public Boolean checkLocation(double latitude, double longitude){

            if(latitude>=searchpin.getlatitude()-0.025 && latitude<=searchpin.getlatitude()+0.025){
                if(longitude>=searchpin.getlongitude()-0.025 & longitude<=searchpin.getlongitude()+0.025){
                    return true;

                }
            }

        return false;
    }

    public Boolean checkTime(Timestamp ridetime) {



            //Date checkdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date + " " + time);
            String ridedate = searchdate.getText().toString()+" "+searchtime.getText().toString()+":00.00";
            Timestamp checkdate =  Timestamp.valueOf(ridedate);
            System.out.println(Math.abs(ridetime.getTime() - checkdate.getTime())/(3600*16));
            if(Math.abs(ridetime.getTime() - checkdate.getTime())/(3600*16) <= 30 ){
                return true;

            }
        return false;
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



