package au.org.ridesharingoz.rideshare_oz;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import au.org.ridesharingoz.rideshare_oz.R;

public class CreateEventActivity extends FirebaseAuthenticatedActivity {

    Button submit;
    Button goToMap;
    EditText eventNameText;
    EditText eventDescriptionText;
    int PIN_REQUEST = 1;
    Pin fixedPin = null;
    String pinID = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        submit = (Button) findViewById(R.id.btn_submit_event);
        goToMap = (Button) findViewById(R.id.createFixedPointAddressEvent);
        eventNameText = (EditText) findViewById(R.id.createEventName);
        eventDescriptionText = (EditText) findViewById(R.id.createEventDescription);



        /* *************************************
        *          Go to Map button listener      *
        ***************************************/

        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPinFromMap = new Intent(CreateEventActivity.this, MapsActivity.class);
                getPinFromMap.putExtra("from", "CreateEventActivity");
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
        *    Create Group and Pin on submit    *
        ***************************************/

    public void dataSubmit() {

        Firebase groupsRef = mFirebaseRef.child("events");

        String eventName = eventNameText.getText().toString();
        String eventDescription = eventDescriptionText.getText().toString();
        //Timestamp eventStartDate = new Timestamp();
        //Timestamp eventEndDate = new Timestamp();

        boolean emptyN = isEmptyEditText(eventName, eventNameText);
        boolean emptyD = isEmptyEditText(eventDescription, eventDescriptionText);




        //Data validation then pin and group creation in firebase and update of owner
        if (!emptyN & !emptyD & fixedPin != null) {
            Firebase pinsRef = mFirebaseRef.child("fixedpins");
            Firebase uniqueID = pinsRef.push();
            uniqueID.setValue(fixedPin);
            pinID = uniqueID.getKey();
           // Event event = new Event(eventName, eventDescription, eventStartDate, eventEndDate, pinID);
            Firebase eventUniqueID = groupsRef.push();
            //eventUniqueID.setValue(event);
            String groupID = eventUniqueID.getKey();
            Toast.makeText(getApplicationContext(), "Event created successfully.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), ManageMyGroupsActivity.class);
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
