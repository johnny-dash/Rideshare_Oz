package au.org.ridesharingoz.rideshare_oz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


import java.sql.Timestamp;
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


public class OneRideGoingtoActivity extends FirebaseAuthenticatedActivity {

    /* *************************************
    *               GENERAL               *
    ***************************************/

    //ArrayList<Pin> mappins;
    ArrayList<Pin> pins;

    EditText tx_seatNum;

    int listPosition;

    double latitude[] = new double[100];
    double longitude[] = new double[100];

    String groupEventID;
    String type;
    Boolean isEvent;

    Button createOffRide;

    ListView addresslistview;

    private List<Map<String,String>> mData;




    /* *************************************
    *          init of calender            *
    ***************************************/
    EditText editdate;
    EditText edit_arrivaltime;

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

    TimePickerDialog.OnTimeSetListener arrival_time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            arrival_timeformat();
        }
    };

    private void dateformat() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        editdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void arrival_timeformat() {

        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        edit_arrivaltime.setText(sdf.format(myCalendar.getTime()));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_off_goingto_ride);
        final Bundle bundle = getIntent().getExtras();

        if (bundle!= null){
            groupEventID = bundle.getString("ID");
            isEvent = bundle.getBoolean("isEvent");
            type = "goingto";
        }

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
        edit_arrivaltime = (EditText) findViewById(R.id.Arrival_Time);

        editdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(OneRideGoingtoActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edit_arrivaltime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(OneRideGoingtoActivity.this, arrival_time, myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();
            }
        });


    }

    private List<Map<String, String>> getDate() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        int index = 0;
        if (type.equals("leaving")){
            pins.remove(0);
        }


        for (Pin pin:pins){
            Map<String, String> map = new HashMap<String, String>();
            map.put("Address",String.valueOf(pin.getaddress()));
            map.put("Time","Haven't been set");
            latitude[index] = pin.getlatitude();
            longitude[index] = pin.getlongitude();
            index++;
            list.add(map);
        }

        return list;
    }

    public void dataSubmit(List<Map<String,String>> mData){

        //重复性检查，错误输入，少输入检查数据重写
        String DriverID = mAuthData.getUid();
        Firebase RideRef = mFirebaseRef.child("goingtorides");
        Firebase PinRef = mFirebaseRef.child("goingtopins");
        String rideID = "";
        Firebase rideUniqueID = RideRef.push();
        rideID = rideUniqueID.getKey();


        /* *************************************
        *          Store of Ride              *
        ***************************************/
        String seatNum = tx_seatNum.getText().toString();
        String date = editdate.getText().toString();
        String arrival = edit_arrivaltime.getText().toString();

        Boolean seatNumcheck = seatNum.isEmpty();
        Boolean datecheck = date.isEmpty();
        String ridedate = date+" "+arrival+":00.00";
        Timestamp ridets =  Timestamp.valueOf(ridedate);


        if (!seatNumcheck&&!datecheck){
            Ride new_ride = new Ride(DriverID,
                    Integer.parseInt(seatNum),
                    ridets,
                    null,
                    groupEventID,
                    isEvent,
                    type);

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

        String time ="";
        String address ="";
        int index = 0;



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
            String datetime = date+" "+time+":00.00";
            Timestamp myts =  Timestamp.valueOf(datetime);
            if (!Timecheck&&!addresscheck){
                Pin pin = new Pin(rideID,
                        longitude[index],
                        latitude[index],
                        address,
                        myts,
                        groupEventID,
                        isEvent,
                        type);
                Firebase PinKey = PinRef.push();
                String PinID = PinKey.getKey();
                Map<String,Object> pinid = new HashMap<>();
                pinid.put(PinID,true);
                RideRef.child(rideID).child("pins").updateChildren(pinid);
                PinKey.setValue(pin, new Firebase.CompletionListener() {
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




            index=index+1;

            Intent intent = new Intent(OneRideGoingtoActivity.this,ActionChoiceActivity.class);
            startActivity(intent);
            finish();
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
                    new TimePickerDialog(OneRideGoingtoActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY),
                            myCalendar.get(Calendar.MINUTE), true).show();
                    listPosition = position;

                }

            });

            return convertView;
        }

        private void timeformat(int position) {

            String myFormat = "HH:mm"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            Map<String, String> map = new HashMap<String, String>();
            map.put("Time", sdf.format(myCalendar.getTime()));
            map.put("Address", mData.get(position).get("Address"));
            mData.set(position, map);
            notifyDataSetChanged();
        }


    }
}