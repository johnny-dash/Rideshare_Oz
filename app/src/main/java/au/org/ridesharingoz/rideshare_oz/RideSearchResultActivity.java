package au.org.ridesharingoz.rideshare_oz;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import au.org.ridesharingoz.rideshare_oz.R;

public class RideSearchResultActivity extends FirebaseAuthenticatedActivity {
    private List<Map<String,String>> mData;
    String type;

    ListView searchresult_listview;
    ArrayList<String> pinIDs = new ArrayList();

    int count1 = 1;
    int count2 = 0;
    int count3 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        final Bundle bundle = getIntent().getExtras();

        if (bundle!= null){
            pinIDs = bundle.getStringArrayList("pins");
            type = bundle.getString("Type");
        }
        if (type.equals("goingto")) {
            mData = getDateGoingto();
        }
        if(type.equals("leavingfrom")){
            mData = getDateLeavingfrom();
        }
        searchresult_listview = (ListView) findViewById(R.id.SearchedResult);




    }
    private List<Map<String,String>> getDateLeavingfrom(){
        Firebase PinRef = mFirebaseRef.child("leavingfrompins");
        final Firebase RideRef = mFirebaseRef.child("leavingfromrides");
        final Firebase UserRef = mFirebaseRef.child("users");
        final MyAdapter adapter = new MyAdapter(this);
        final List<Map<String, String>> list = new ArrayList<Map<String, String>>();



        count1 -= 1;
        for(final String pinID :pinIDs){
            PinRef.child(pinID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Map<String, String> map = new HashMap<String, String>();
                    count1 +=1;
                    Pin pin = dataSnapshot.getValue(Pin.class);
                    map.put("pinID",pinID);
                    map.put("address", pin.getaddress());
                    map.put("rideID",pin.getrideID());
                    RideRef.child(pin.getrideID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //
                            count2 += 1;
                            Timestamp timestamp = new Timestamp(Long.parseLong(dataSnapshot.child("timestamp").getValue().toString()));
                            String date = new SimpleDateFormat("dd/MM/yyyy").format(timestamp);
                            String time = new SimpleDateFormat("HH:mm").format(timestamp);
                            map.put("date", date);
                            map.put("time", time);
                            map.put("departuretime",timestamp.toString());
                            String driverID = (String) dataSnapshot.child("driverID").getValue();
                            map.put("arrivaltime",dataSnapshot.child("timestamp").getValue().toString());
                            map.put("driverID",driverID);
                            UserRef.child(driverID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //
                                    count3 += 1;
                                    String drivername = (String) dataSnapshot.child("firstName").getValue() + " " + (String) dataSnapshot.child("lastName").getValue();
                                    map.put("drivername", drivername);
                                    if (dataSnapshot.hasChild("rating")) {
                                        String rate = dataSnapshot.child("rating").getValue().toString();
                                        map.put("rating", rate);
                                    } else {
                                        map.put("rating", "This driver has not been rated.");
                                    }
                                    if (count1 == count3) {
                                        searchresult_listview.setAdapter(adapter);
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                    list.add(map);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }

        return list;
    }


    private List<Map<String, String>> getDateGoingto(){
        Firebase PinRef = mFirebaseRef.child("goingtopins");
        final Firebase RideRef = mFirebaseRef.child("goingtorides");
        final Firebase UserRef = mFirebaseRef.child("users");
        final MyAdapter adapter = new MyAdapter(this);
        final List<Map<String, String>> list = new ArrayList<Map<String, String>>();



        count1 -= 1;
        for(final String pinID :pinIDs){
            PinRef.child(pinID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Map<String, String> map = new HashMap<String, String>();
                    count1 +=1;
                    Pin pin = dataSnapshot.getValue(Pin.class);
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(pin.getTimestamp());
                    String time = new SimpleDateFormat("HH:mm").format(pin.getTimestamp());
                    map.put("pinID",pinID);
                    map.put("date", date);
                    map.put("time", time);
                    map.put("departuretime",pin.getTimestamp().toString());
                    map.put("address", pin.getaddress());
                    map.put("rideID",pin.getrideID());
                    RideRef.child(pin.getrideID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //
                            count2 += 1;
                            String driverID = (String) dataSnapshot.child("driverID").getValue();
                            map.put("arrivaltime",dataSnapshot.child("timestamp").getValue().toString());
                            map.put("driverID",driverID);
                            UserRef.child(driverID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //
                                    count3 += 1;
                                    String drivername = (String) dataSnapshot.child("firstName").getValue() + " " + (String) dataSnapshot.child("lastName").getValue();
                                    map.put("drivername", drivername);
                                    if (dataSnapshot.hasChild("rating")) {
                                        String rate = dataSnapshot.child("rating").getValue().toString();
                                        map.put("rating", rate);
                                    } else {
                                        map.put("rating", "This driver has not been rated.");
                                    }
                                    if (count1 == count3) {
                                        searchresult_listview.setAdapter(adapter);
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                    list.add(map);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }

        return list;
    }

    public final class ViewHolder{
        public TextView resultdate;
        public TextView resulttime;
        public TextView resultaddress;
        public TextView resultdrivername;
        public TextView resultdriverRate;
        public TextView resultridetype;
        public Button showdriverprofile;
        public Button joinride;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;


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

                convertView = mInflater.inflate(R.layout.activity_searched_result_item,null);
                holder.resultdate = (TextView)convertView.findViewById(R.id.result_date);
                holder.resulttime = (TextView)convertView.findViewById(R.id.result_time);
                holder.resultaddress = (TextView)convertView.findViewById(R.id.result_address);
                holder.resultdrivername = (TextView)convertView.findViewById(R.id.result_drivername);
                holder.resultdriverRate = (TextView)convertView.findViewById(R.id.result_driverrate);
                holder.resultridetype = (TextView)convertView.findViewById(R.id.result_ridetype);
                holder.showdriverprofile = (Button)convertView.findViewById(R.id.btn_view_driver);
                holder.joinride = (Button)convertView.findViewById(R.id.btn_joinride);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.resultdate.setText((String) mData.get(position).get("date"));
            holder.resulttime.setText((String) mData.get(position).get("time"));
            holder.resultaddress.setText((String) mData.get(position).get("address"));
            holder.resultdrivername.setText((String) mData.get(position).get("drivername"));
            holder.resultdriverRate.setText((String) mData.get(position).get("rating"));
            holder.resultridetype.setText(type);
            holder.joinride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinRide(position);
                }
            });
            holder.showdriverprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewprofile(position);
                }
            });


            return convertView;
        }


        public void joinRide(int position){
            Firebase bookingID = mFirebaseRef.child("pendingbookings").push();
            String bookingUnique = bookingID.getKey();
            Map<String,Object> pendingBookingId = new HashMap<>();
            pendingBookingId.put(bookingUnique, true);

            Firebase userjoinrequest = mFirebaseRef.child("users").child(mData.get(position).get("driverID")).child("pendingJoinRequests");
            userjoinrequest.updateChildren(pendingBookingId);
            Firebase ridejoinrequest = mFirebaseRef.child("goingtorides").child(mData.get(position).get("rideID")).child("pendingJoinRequests");
            ridejoinrequest.updateChildren(pendingBookingId);

            long figure_arrial = Long.parseLong(mData.get(position).get("arrivaltime"));
            Timestamp departuretime = Timestamp.valueOf(mData.get(position).get("departuretime"));
            Timestamp arrivaltime = new Timestamp(figure_arrial);
            Booking booking = new Booking(mData.get(position).get("rideID"),
                    type,
                    mData.get(position).get("pinID"),
                    departuretime ,
                    mAuthData.getUid(),
                    arrivaltime);
            bookingID.setValue(booking, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    Toast.makeText(getApplicationContext(), "You have send request to driver", Toast.LENGTH_LONG).show();
                }
            });

        }

        public void viewprofile(int position){
            Intent intent = new Intent(RideSearchResultActivity.this,ViewProfileActivity.class);
            intent.putExtra("uid",mData.get(position).get("driverID"));
            startActivity(intent);
        }


    }


}
