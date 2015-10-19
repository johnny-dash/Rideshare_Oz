package au.org.ridesharingoz.rideshare_oz.groupsPackage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import au.org.ridesharingoz.rideshare_oz.R;
import au.org.ridesharingoz.rideshare_oz.dataPackage.Event;
import au.org.ridesharingoz.rideshare_oz.dataPackage.Pin;
import au.org.ridesharingoz.rideshare_oz.firebasePackage.FirebaseAuthenticatedActivity;
import au.org.ridesharingoz.rideshare_oz.mapsPackage.ChooseLocationActivity;

public class CreateEventActivity extends FirebaseAuthenticatedActivity {

    Button submit;
    Button goToMap;
    EditText eventNameText;
    EditText eventDescriptionText;
    EditText editStartDate;
    EditText editEndDate;
    int PIN_REQUEST = 1;
    Pin fixedPin = null;
    String pinID = null;
    String groupID;



    Calendar startCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH, monthOfYear);
            startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            endCalendar.set(Calendar.HOUR_OF_DAY, 00);
            endCalendar.set(Calendar.MINUTE, 00);
            endCalendar.set(Calendar.SECOND, 01);
            dateformat(editStartDate, startCalendar);
        }

    };

    Calendar endCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH, monthOfYear);
            endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            endCalendar.set(Calendar.HOUR_OF_DAY, 23);
            endCalendar.set(Calendar.MINUTE, 59);
            endCalendar.set(Calendar.SECOND, 59);
            dateformat(editEndDate, endCalendar);
        }
    };

    private void dateformat(EditText editText, Calendar calendar) {
//        String myFormat = "dd/MM/yyyy";
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        editText.setText(sdf.format(calendar.getTime()));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        groupID = getIntent().getStringExtra("groupID");


        submit = (Button) findViewById(R.id.btn_submit_event);
        goToMap = (Button) findViewById(R.id.createFixedPointAddressEvent);
        eventNameText = (EditText) findViewById(R.id.createEventName);
        eventDescriptionText = (EditText) findViewById(R.id.createEventDescription);
        editStartDate = (EditText) findViewById(R.id.createEventStartDate);
        editEndDate = (EditText) findViewById(R.id.createEventEndDate);


        /* *************************************
        *          StartDate button listener      *
        ***************************************/

        editStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventActivity.this, startDate, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        /* *************************************
        *          EndDate button listener      *
        ***************************************/

        editEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventActivity.this, endDate, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /* *************************************
        *          Go to Map button listener      *
        ***************************************/

        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPinFromMap = new Intent(CreateEventActivity.this, ChooseLocationActivity.class);
                getPinFromMap.putExtra("callingActivity", "CreateEventActivity");
                startActivityForResult(getPinFromMap, PIN_REQUEST);
            }
        });


        /* *************************************
        *      Submit  button listener      *
        ***************************************/

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSubmit();
            }
        });
    }





        /* *************************************
        *    Create Event and fixedpin on submit    *
        ***************************************/

    public void dataSubmit() {

        Firebase groupsRef = mFirebaseRef.child("events");

        String eventName = eventNameText.getText().toString();
        String eventDescription = eventDescriptionText.getText().toString();
        String startDate = editStartDate.getText().toString();
        String endDate = editEndDate.getText().toString();


        boolean emptyN = isEmptyEditText(eventName, eventNameText);
        boolean emptyD = isEmptyEditText(eventDescription, eventDescriptionText);
        boolean emptyS = isEmptyTime(startDate, editStartDate);
        boolean emptyE = isEmptyTime(endDate, editEndDate);




        //Data validation then pin and group creation in firebase and update of owner
        if (!emptyN & !emptyD & !emptyS & !emptyE & fixedPin != null) {
            String startDateTime = startDate+" "+"00:00:00.01";
            String endDateTime = endDate+" "+"23:59:59.00";
            Timestamp startTimeStamp =  Timestamp.valueOf(startDateTime);
            Timestamp endTimeStamp = Timestamp.valueOf(endDateTime);

            Firebase pinsRef = mFirebaseRef.child("fixedpins");
            Firebase uniqueID = pinsRef.push();
            uniqueID.setValue(fixedPin);
            pinID = uniqueID.getKey();
            Event event = new Event(eventName, eventDescription, startTimeStamp, endTimeStamp, pinID);
            Firebase eventUniqueID = groupsRef.push();
            eventUniqueID.setValue(event);
            String eventID = eventUniqueID.getKey();
            Firebase eventRef = mFirebaseRef.child("groups").child(groupID).child("events");
            Map<String, Object> newEventInGroup = new HashMap<>();
            newEventInGroup.put(eventID, true);
            eventRef.updateChildren(newEventInGroup);
            Toast.makeText(getApplicationContext(), "Event created successfully.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), GroupManagementPanelActivity.class);
            intent.putExtra("groupID", groupID);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Event cannot be created. Missing data.", Toast.LENGTH_LONG).show();
        }
    }

    //checks if text data has been entered
    private boolean isEmptyEditText(String editTextString, EditText editText) {
        if (TextUtils.isEmpty(editTextString)) {
            editText.setError("You need to fill this field");
            return true;
        } else return false;
    }


    //check if time has been setted
    private boolean isEmptyTime(String editTextString, EditText editText){

        if(editTextString.equals("") ){
            editText.setError("You need to set time");
            return true;
        } else return false;
    }

    //gets pin data back from the MapsActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                fixedPin = (Pin) data.getSerializableExtra("pin");
            }
        }


    }

}
