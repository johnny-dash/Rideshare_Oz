package au.org.ridesharingoz.rideshare_oz;

import android.content.Context;
import android.os.Bundle;
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


        private List<Map<String,String>> mData;

        List<Ride> Ridelist = new ArrayList<Ride>();
        Ride ride;
        ListView ridelistview;

    public void getRide(){

        mFirebaseRef.child("rides").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                    if(rideSnapshot.child("driverID").getValue().equals(mAuthData.getUid())){
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

        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_offered_rides);
            mData = getDate();
            getRide();

            System.out.println(Ridelist.size());

            ridelistview = (ListView) findViewById(R.id.OfferedRideListview);
            MyAdapter myAdapter = new MyAdapter(this);
            ridelistview.setAdapter(myAdapter);

        }

        public void getpins(){

        }




        public List<Map<String, String>> getDate(){
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            Map<String, String> map = new HashMap<String, String>();
            for(Ride ride:Ridelist) {
                map.put("beginpoint", "myhome");
                map.put("endpoint", "unimelb");
                map.put("date", ride.getDate());
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

                    convertView = mInflater.inflate(R.layout.activity_offeredride_item,null);
                    holder.beginpoint = (TextView)convertView.findViewById(R.id.off_BeginningAddress);
                    holder.endpoint = (TextView)convertView.findViewById(R.id.off_FinishAddress);
                    holder.ridedate = (TextView)convertView.findViewById(R.id.off_DateInfo);
                    holder.seatnum = (TextView)convertView.findViewById(R.id.SeatNum);
                    holder.btn_cancel = (Button) convertView.findViewById(R.id.off_cancelRide);
                    holder.btn_showmap = (Button) convertView.findViewById(R.id.off_showroute);
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


