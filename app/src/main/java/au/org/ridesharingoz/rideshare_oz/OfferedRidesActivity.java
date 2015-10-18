package au.org.ridesharingoz.rideshare_oz;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johnny on 8/10/2015.
 */


public class OfferedRidesActivity extends FirebaseAuthenticatedActivity {

    private List<Map<String,Object>> mData;
    List<Ride> Ridelist = new ArrayList<Ride>();
    Ride ride;
    ListView ridelistview;
    private int count1 = 1;
    private int count2 = 2;
    private int count3 = 0;
    private int count4 = 0;
    private int count5 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offered_rides);
        createData();
        mData = getDate();
        getRide();

        System.out.println(Ridelist.size());

        ridelistview = (ListView) findViewById(R.id.offeredRidesListview);
        MyAdapter myAdapter = new MyAdapter(this);
        ridelistview.setAdapter(myAdapter);

    }


    public void createData(){
        final List<Ride> rides = new ArrayList<>();
        Query goingtoridesnode = mFirebaseRef.child("goingtorides").orderByChild("driverID").equalTo(mAuthData.getUid());
        goingtoridesnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count1 -=1;
                for (DataSnapshot rideOffered : dataSnapshot.getChildren()){
                    rides.add(rideOffered.getValue(Ride.class));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void getRide(){

        mFirebaseRef.child("rides").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                    if (rideSnapshot.child("driverID").getValue().equals(mAuthData.getUid())) {
                        Ridelist.add(rideSnapshot.getValue(Ride.class));
                        ride = rideSnapshot.getValue(Ride.class);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }



        public void getpins(){

        }




        public List<Map<String, Object>> getDate(){
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            for(Ride ride:Ridelist) {
                map.put("beginpoint", "myhome");
                map.put("endpoint", "unimelb");
                //map.put("date", ride.getDate());
                //map.put("seat", Inride.getSeatNum());
                list.add(map);
            }


            return list;
        }


        public final class ViewHolder{
            public TextView beginpoint;
            public TextView endpoint;
            public TextView ridedate;
            public TextView seatnum;
            public Button btn_showmap;
            public Button btn_cancel;
            public TextView ridedeparturetime;
            public TextView ridearrivaltime;
        }

        public class MyAdapter extends BaseAdapter {
            private LayoutInflater mInflater;

            public MyAdapter(Context context){
                this.mInflater = LayoutInflater.from(context);
            }

            @Override
            public int getCount(){
                return mData.size();
            }

            @Override
            public String getItem(int arg0){
                return null;
            }

            @Override
            public long getItemId(int arg0){
                return 0;
            }


            @Override
            public View getView(final int position, View convertView, ViewGroup parent){
                ViewHolder holder = null;

                if(convertView == null){
                    holder = new ViewHolder();

                    convertView = mInflater.inflate(R.layout.listview_offeredrides_item,null);
                    holder.beginpoint = (TextView)convertView.findViewById(R.id.offered_BeginningAddress);
                    holder.endpoint = (TextView)convertView.findViewById(R.id.offered_FinishAddress);
                    holder.ridedate = (TextView)convertView.findViewById(R.id.dateOfferedInfo);
                    holder.ridedeparturetime = (TextView)convertView.findViewById(R.id.timeDepartureOfferedInfo);
                    holder.ridearrivaltime = (TextView) convertView.findViewById(R.id.timeArrivalOfferedInfo);
                    holder.seatnum = (TextView)convertView.findViewById(R.id.seatNum);
                    holder.btn_cancel = (Button) convertView.findViewById(R.id.offered_cancelRide);
                    holder.btn_showmap = (Button) convertView.findViewById(R.id.offered_showroute);
                    convertView.setTag(holder);
                }
                else {
                    holder = (ViewHolder)convertView.getTag();
                }

                holder.beginpoint.setText((String) mData.get(position).get("beginpoint"));
                holder.endpoint.setText((String) mData.get(position).get("endpoint"));
                holder.ridedate.setText((String) mData.get(position).get("date"));
                holder.seatnum.setText((String) mData.get(position).get("seat"));

                return convertView;
            }



        }


}


