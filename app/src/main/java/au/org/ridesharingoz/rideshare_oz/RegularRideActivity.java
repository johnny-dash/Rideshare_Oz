package au.org.ridesharingoz.rideshare_oz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import au.org.ridesharingoz.rideshare_oz.R;

/**
 * Created by Johnny on 22/09/2015.
 *
 */

public class RegularRideActivity extends FirebaseAuthenticatedActivity {

    /* *************************************
    *               GENERAL               *
    ***************************************/

    RadioGroup daysRadioGroup;

    ListView addresslistview;

    CheckBox Monday;
    CheckBox Tuesday;
    CheckBox Wednesday;
    CheckBox Thursday;
    CheckBox Friday;
    CheckBox Saturday;
    CheckBox Sunday;

    Button createRegularRide;

    String trye;
    int listPosition;

    private List<Map<String,Object>> mData;

    /* *************************************
    *          init of calender            *
    ***************************************/
    EditText startdate;

    EditText enddate;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener cal_startdate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            startdateformat();
        }

    };

    DatePickerDialog.OnDateSetListener cal_enddate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            enddateformat();
        }

    };

   private void startdateformat() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void enddateformat() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        enddate.setText(sdf.format(myCalendar.getTime()));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_ride);

        /* *************************************
        *               GENERAL               *
        ***************************************/
        createRegularRide = (Button) findViewById(R.id.Create_regRide);
        createRegularRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datasubmit();
            }
        });

        mData = getDate();
        addresslistview = (ListView) findViewById(R.id.RAddressList);
        MyAdapter adapter = new MyAdapter(this);
        addresslistview.setAdapter(adapter);
        /* *************************************
        *          init of calender            *
        ***************************************/
        startdate = (EditText) findViewById(R.id.StartDate);
        startdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegularRideActivity.this, cal_startdate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        enddate = (EditText) findViewById(R.id.EndDate);
        enddate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegularRideActivity.this, cal_enddate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private List<Map<String, Object>> getDate() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Address", "Unimelb");
        map.put("Time","Haven't been set");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("Address", "604 Swanston street");
        map.put("Time","Haven't been set");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("Address", "St Kilda");
        map.put("Time","Haven't been set");
        list.add(map);

        return list;
    }

    public final class ViewHolder{
        public TextView Addressname;
        public TextView setTimeView;
        public Button addTimeButton;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                timeformat(listPosition);
            }
        };

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.listview_item,null);
                holder.Addressname = (TextView)convertView.findViewById(R.id.AddressName);
                holder.setTimeView = (TextView)convertView.findViewById(R.id.SetTimeView);
                holder.addTimeButton = (Button)convertView.findViewById(R.id.AddTime);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.Addressname.setText((String)mData.get(position).get("Address"));
            holder.setTimeView.setText((String)mData.get(position).get("Time"));
            holder.addTimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new TimePickerDialog(RegularRideActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY),
                            myCalendar.get(Calendar.MINUTE), false).show();
                    listPosition = position;

                }

            });

            return convertView;
        }

        private void timeformat(int position) {

            String myFormat = "hh:mm"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Time", sdf.format(myCalendar.getTime()));
            map.put("Address", mData.get(position).get("Address"));
            mData.set(position, map);
            notifyDataSetChanged();
        }

    }


    public void Datasubmit(){

    }
}
