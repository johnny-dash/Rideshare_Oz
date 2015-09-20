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

public class RegularRideActivity extends MapsActivity {

    /* *************************************
    *               GENERAL               *
    ***************************************/

    RadioGroup daysRadioGroup;
    RadioGroup typRadioGroup;

    ListView addresslistview;

    CheckBox Monday;
    CheckBox Tuesday;
    CheckBox Wednesday;
    CheckBox Thursday;
    CheckBox Friday;
    CheckBox Saturday;
    CheckBox Sunday;

    Button createOffRide;

    String trye;
    int listPosition;

    private List<Map<String,Object>> mData;

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
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateformat();
        }

    };

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
            myCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            timeformat();

        }
    };

    private void dateformat() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void timeformat() {

        String myFormat = "hh:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittime.setText(sdf.format(myCalendar.getTime()));
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_ride);

        /* *************************************
        *               GENERAL               *
        ***************************************/

        mData = getDate();
        addresslistview = (ListView) findViewById(R.id.RAddressList);
        MyAdapter adapter = new MyAdapter(this);
        addresslistview.setAdapter(adapter);
        /* *************************************
        *          init of calender            *
        ***************************************/
        editdate = (EditText) findViewById(R.id.Date);
        editdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegularRideActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
/*
        edittime = (EditText) findViewById(R.id.Time);
        edittime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(RegularRideActivity.this,time,myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE),false).show();
            }
        });
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_oneoff_ride, menu);
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

}
