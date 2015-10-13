package au.org.ridesharingoz.rideshare_oz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import au.org.ridesharingoz.rideshare_oz.R;

public class OneRideLeavingfromActivity extends FirebaseAuthenticatedActivity {
    String groupname;
    String eventname;
    String type;

    ArrayList<Pin> pins;
    List<String> address = new ArrayList<>();

    ListView addresslistview;

    Button createOffRide;

    EditText editseat;

    /* *************************************
    *          init of calender            *
    ***************************************/
    EditText editdate;
    EditText edittime;

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

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            timeformat();
        }
    };



    private void dateformat() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        editdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void timeformat() {

        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        edittime.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_ride_leavingfrom);

        final Bundle bundle = getIntent().getExtras();

        if (bundle!= null){
            groupname = bundle.getString("Group");
            eventname = bundle.getString("Event");
            type = "leavingfrom";
        }

        /* *************************************
        *               Read pins              *
        ***************************************/

        Intent intent = getIntent();
        pins = (ArrayList<Pin>) intent.getSerializableExtra("pins");

        /* *************************************
        *               GENERAL               *
        ***************************************/
        editseat = (EditText) findViewById(R.id.leave_SeatNum);

        createOffRide = (Button) findViewById(R.id.Leave_Create_Ride);
        createOffRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRide();
            }
        });


        address = getaddress(pins);
        addresslistview = (ListView) findViewById(R.id.Leave_AddressList);
        addresslistview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getaddress(pins)));
        /* *************************************
        *          init of calender            *
        ***************************************/
        editdate = (EditText) findViewById(R.id.Leave_Date);
        editdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(OneRideLeavingfromActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edittime = (EditText) findViewById(R.id.Leave_Time);
        edittime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(OneRideLeavingfromActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();
            }
        });



    }

    public void CreateRide(){
        String DriverID = mAuthData.getUid();
        Firebase RideRef = mFirebaseRef.child("leavingfromrides");
        Firebase PinRef = mFirebaseRef.child("leavingfrompins");
        String rideID = "";


        /* *************************************
        *          Store of Ride              *
        ***************************************/
        String seatNum = editseat.getText().toString();
        String date = editdate.getText().toString();
        String time = edittime.getText().toString();

        Boolean seatNumcheck = seatNum.isEmpty();
        Boolean datecheck = date.isEmpty();
        Boolean timecheck = time.isEmpty();

        if (!seatNumcheck&&!datecheck&&!timecheck){
            Ride new_ride = new Ride(DriverID,
                    Integer.parseInt(seatNum),
                    date,
                    time,
                    groupname,
                    eventname,
                    type);
            Firebase rideUniqueID = RideRef.push();
            rideID = rideUniqueID.getKey();
            rideUniqueID.setValue(new_ride, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError != null) {
                        Toast.makeText(getApplicationContext(), "Ride could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println("Data could not be saved. " + firebaseError.getMessage());
                    } else {
                        Toast.makeText(getApplicationContext(), "Ride create successfully.", Toast.LENGTH_LONG).show();
                        System.out.println("Ride saved successfully.");
                    }
                }

            });
        }


        /* *************************************
        *          Store of pins              *
        ***************************************/


        for (Pin pin:pins){
            if (!seatNumcheck&&!datecheck&&!timecheck){
                Pin savedpin = new Pin(rideID,
                        pin.getlongitude(),
                        pin.getlatitude(),
                        pin.getaddress(),
                        null,
                        groupname,
                        eventname,
                        type);

                PinRef.push().setValue(savedpin, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError != null) {
                            //Toast.makeText(getApplicationContext(), "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("Data could not be saved. " + firebaseError.getMessage());
                        } else {
                            //Toast.makeText(getApplicationContext(), "Data saved successfully.", Toast.LENGTH_LONG).show();
                            System.out.println("Pins saved successfully.");
                        }
                    }

                });
            }
        }
    }

    public List<String> getaddress(List<Pin> pins){
        List<String> address = new ArrayList<>();
        for(Pin pin:pins){
            address.add(pin.getaddress());
        }
        return address;
    }


}
