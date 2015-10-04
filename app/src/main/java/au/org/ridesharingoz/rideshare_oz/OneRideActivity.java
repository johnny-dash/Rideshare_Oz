package au.org.ridesharingoz.rideshare_oz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Johnny on 22/09/2015.
 *
 */


public class OneRideActivity extends FirebaseAuthenticatedActivity {

    /* *************************************
    *               GENERAL               *
    ***************************************/

    //ArrayList<Pin> mappins;
    ArrayList<Pin> pins;

    EditText tx_seatNum;

    int listPosition;


    Button createOffRide;

    ListView addresslistview;

    private List<Map<String,String>> mData;


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
        setContentView(R.layout.activity_one_off_ride);

        /* *************************************
        *               Read pins              *
        ***************************************/

        Intent intent = getIntent();
        pins = (ArrayList<Pin>) intent.getSerializableExtra("pins");
        /* *************************************
        *               GENERAL               *
        ***************************************/
        tx_seatNum = (EditText) findViewById(R.id.SeatNum);

        mData = getDate();
        addresslistview = (ListView) findViewById(R.id.AddressList);
        MyAdapter adapter = new MyAdapter(this);
        addresslistview.setAdapter(adapter);

        createOffRide = (Button) findViewById(R.id.Create_Ride);
        createOffRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSubmit(mData);
            }
        });

        /* *************************************
        *          init of calender            *
        ***************************************/
        editdate = (EditText) findViewById(R.id.Date);
        editdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(OneRideActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private List<Map<String, String>> getDate() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
    /*
        for (Pin pin:pins){
            map.put("Address",String.valueOf(pin.getlatitude()));
            map.put("Time","Haven't been set");
            list.add(map);
        }
*/
        map.put("Address", "Unimelb");
        map.put("Time", "Haven't been set");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("Address", "604 Swanston street");
        map.put("Time", "Haven't been set");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("Address", "St Kilda");
        map.put("Time", "Haven't been set");
        list.add(map);

        return list;
    }

    public void dataSubmit(List<Map<String,String>> mData){

        //重复性检查，错误输入，少输入检查数据重写
        String DriverID = mAuthData.getUid();
        Firebase RideRef = mFirebaseRef.child("ride");
        Firebase PinRef = mFirebaseRef.child("pins");


        /* *************************************
        *          Store of Ride              *
        ***************************************/
        String seatNum = tx_seatNum.getText().toString();
        String date = editdate.getText().toString();

        Boolean seatNumcheck = seatNum.isEmpty();
        Boolean datecheck = date.isEmpty();

        if (!seatNumcheck&&!datecheck){
            Ride new_ride = new Ride("1",DriverID,Integer.parseInt(seatNum),date);
            RideRef.push().setValue(new_ride, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError != null) {
                        Toast.makeText(getApplicationContext(), "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println("Data could not be saved. " + firebaseError.getMessage());
                    } else {
                        Toast.makeText(getApplicationContext(), "Data saved successfully.", Toast.LENGTH_LONG).show();
                        System.out.println("Ride saved successfully.");
                    }
                }

            });
        }

        /* *************************************
        *          Store of pins              *
        ***************************************/

        String time ="";
        String address ="";
        for (Map<String,String> map : mData){

            for (Map.Entry<String,String> entry: map.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                if(key == "Address"){
                    address = value;
                }
                if (key == "Time"){
                    time = value;
                }
            }
            boolean Timecheck = time.isEmpty();
            boolean addresscheck = address.isEmpty();

            if (!Timecheck&&!addresscheck){
                Pin pin = new Pin("1",23.333,67.222,address,time,date);

                PinRef.push().setValue(pin, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError != null) {
                            Toast.makeText(getApplicationContext(), "Data could not be saved. " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("Data could not be saved. " + firebaseError.getMessage());
                        } else {
                            Toast.makeText(getApplicationContext(), "Data saved successfully.", Toast.LENGTH_LONG).show();
                            System.out.println("Pins saved successfully.");
                        }
                    }

                });
            }

        }



        }


                //@Override
                //protected void onListItemClick(ListView l, View v, int position, long id) {
                //    Log.v("MyListView4-click", (String) mData.get(position).get("Address"));

//    }


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
                myCalendar.set(Calendar.HOUR, hourOfDay);
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
        public String getItem(int arg0) {
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

            holder.Addressname.setText((String) mData.get(position).get("Address"));
            holder.setTimeView.setText((String) mData.get(position).get("Time"));
            holder.addTimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new TimePickerDialog(OneRideActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY),
                            myCalendar.get(Calendar.MINUTE), false).show();
                    listPosition = position;

                }

            });
            
            return convertView;
        }

        private void timeformat(int position) {

            String myFormat = "hh:mm"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Map<String, String> map = new HashMap<String, String>();
            map.put("Time", sdf.format(myCalendar.getTime()));
            map.put("Address", mData.get(position).get("Address"));
            mData.set(position, map);
            notifyDataSetChanged();
        }


    }
}