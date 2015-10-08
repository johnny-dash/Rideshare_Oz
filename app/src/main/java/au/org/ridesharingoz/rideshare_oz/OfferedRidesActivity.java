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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johnny on 8/10/2015.
 */


public class OfferedRidesActivity extends FirebaseAuthenticatedActivity {



    public class JoinedRidesActivity extends FirebaseAuthenticatedActivity {

        private List<Map<String,String>> mData;

        List<Ride> Ridelist;

        ListView ridelistview;


        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_joined_rides);
            mData = getDate();

            ridelistview = (ListView) findViewById(R.id.joinedRidelistview);
            MyAdapter myAdapter = new MyAdapter(this);
            ridelistview.setAdapter(myAdapter);




        }

        public void getdriverInfo(){



        }

        public void getpins(){

        }

        public void getRide(){
            // mFirebaseRef.child("")
        }


        public List<Map<String, String>> getDate(){
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            Map<String, String> map = new HashMap<String, String>();
            map.put("beginpoint","myhome");
            map.put("endpoint","unimelb");
            map.put("date","today");
            map.put("time","now");
            map.put("drivername","Johnny");
            list.add(map);

            Map<String, String> map1 = new HashMap<String, String>();
            map1.put("beginpoint","unimelb");
            map1.put("endpoint","st kilda");
            map1.put("date","tomorrow");
            map1.put("time","23:11");
            map1.put("drivername", "Gin");
            list.add(map1);

            return list;
        }


        public final class ViewHolder{
            public TextView beginpoint;
            public TextView endpoint;
            public TextView ridedate;
            public TextView ridetime;
            public TextView drivername;
            public Button btn_showmap;
            public Button btn_contact;
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

                    convertView = mInflater.inflate(R.layout.activity_joinedride_item,null);
                    holder.beginpoint = (TextView)convertView.findViewById(R.id.BeginningAddress);
                    holder.endpoint = (TextView)convertView.findViewById(R.id.FinishAddress);
                    holder.ridedate = (TextView)convertView.findViewById(R.id.DateInfo);
                    holder.ridetime = (TextView)convertView.findViewById(R.id.TimeInfo);
                    holder.drivername = (TextView) convertView.findViewById(R.id.DriverName);
                    holder.btn_contact = (Button) convertView.findViewById(R.id.contactDriver);
                    holder.btn_cancel = (Button) convertView.findViewById(R.id.cancelRide);
                    holder.btn_showmap = (Button) convertView.findViewById(R.id.showroute);
                    convertView.setTag(holder);
                }
                else {
                    holder = (ViewHolder)convertView.getTag();
                }

                holder.beginpoint.setText((String) mData.get(position).get("beginpoint"));
                holder.endpoint.setText((String) mData.get(position).get("endpoint"));
                holder.ridedate.setText((String) mData.get(position).get("date"));
                holder.ridetime.setText((String) mData.get(position).get("time"));
                holder.drivername.setText((String) mData.get(position).get("drivername"));
                return convertView;
            }



        }


    }

}
