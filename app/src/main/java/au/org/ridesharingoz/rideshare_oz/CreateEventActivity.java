package au.org.ridesharingoz.rideshare_oz;

import android.app.DatePickerDialog;
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
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import au.org.ridesharingoz.rideshare_oz.R;

public class CreateEventActivity extends FirebaseAuthenticatedActivity {

    private Button create_event_button;
    private ListView groupList;
    private TextView EventName;
    private TextView Date;
    private EditText editDate;
    private EditText editEventName;
    private String eventName;
    private ArrayList<String> data;


    /* *************************************
      *          init of calender            *
      ***************************************/
    EditText editdate;

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

        editdate.setText(sdf.format(myCalendar.getTime()));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        create_event_button = (Button) findViewById(R.id.create_event_button);
        groupList = (ListView)findViewById(R.id.groupList);
        Date = (TextView)findViewById(R.id.textView25);
        EventName = (TextView) findViewById(R.id.editEventName);
        editEventName = (EditText)findViewById(R.id.editEventName);


        editDate = (EditText) findViewById(R.id.editDate);
        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        groupList.setAdapter(new ArrayAdapter<>(CreateEventActivity.this, android.R.layout.simple_expandable_list_item_1, data));

    }


}
