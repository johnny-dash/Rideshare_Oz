package au.org.ridesharingoz.rideshare_oz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Johnny on 8/10/2015.
 */
public class JoinedRidesActivity extends FirebaseAuthenticatedActivity {

    private List<Map<String, String>> joinedData;
    private List<Map<String, String>> requestData;
    private MyAdapter requestAdapter;
    private ListView ridesJoinedListview;
    private int count1 = 1;
    private int count2 = 2;
    private int count3 = 0;
    private int count4 = 0;
    private int count5 = 0;
    private int count6 = 0;
    private int count7 = 0;
    private Context context = this;
    private MyAdapter joinedAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_rides);
        createData();
        ridesJoinedListview = (ListView) findViewById(R.id.joinedRidesListview);

    }

    public void createData() {
        final List<String> bookingMadeIDs = new ArrayList<>();
        Query bookingmadenode = mFirebaseRef.child("users").child(mAuthData.getUid());
        bookingmadenode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("bookingsMade").hasChildren()) {
                    count1 -= 1;
                    for (DataSnapshot bookingID : dataSnapshot.getChildren()) {
                        count1 += 1;
                        bookingMadeIDs.add(bookingID.getKey());
                        System.out.println("count1: " + count1);
                    }
                    getBookingInfo(bookingMadeIDs);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void getBookingInfo(List<String> bookingIDs) {
        count2 -= 2;
        for (final String bookingID : bookingIDs) {
            final Map<String, String> map = new HashMap<>();
            Query bookinginfonode = mFirebaseRef.child("bookings").child(bookingID);
            bookinginfonode.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count2 += 1;
                    Booking booking = dataSnapshot.getValue(Booking.class);
                    String departureTime = new SimpleDateFormat("HH:mm").format(booking.getDepartureTime());
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(booking.getDepartureTime());
                    String pinID = booking.getPinID();
                    String rideID = booking.getRideID();
                    String rideType = booking.getRideType();
                    map.put("bookingID", bookingID);
                    map.put("date", date);
                    map.put("departureTime", departureTime);
                    map.put("pinID", pinID);
                    map.put("rideID", rideID);
                    map.put("rideType", rideType);
                    if (rideType == "goingtorides") {
                        String arrivalTime = new SimpleDateFormat("HH:mm").format(booking.getArrivalTime());
                        map.put("arrivalTime", arrivalTime);
                    }
                    getDriverID(map, rideType);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }


    private void getDriverID(final Map map, String rideType) {

        Query ridenode = mFirebaseRef.child(rideType).child((String) map.get("rideID"));
        ridenode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count3 += 1;
                String userID = (String) dataSnapshot.child("driverID").getValue();
                Boolean isEvent = (Boolean) dataSnapshot.child("isEvent").getValue();
                String groupEventID = (String) dataSnapshot.child("groupEvent").getValue();
                map.put("driverID", userID);
                getDriverName(map, userID, isEvent, groupEventID);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    private void getDriverName(final Map map, String userID, final Boolean isEvent, final String groupEventID) {

        Query usernode = mFirebaseRef.child("users").child(userID);
        usernode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count4 += 1;
                String driverFirstName = (String) dataSnapshot.child("firstName").getValue();
                String driverLastName = (String) dataSnapshot.child("lastName").getValue();
                map.put("driverLastName", driverLastName);
                map.put("driverFirstName", driverFirstName);
                getFixedPinID(map, groupEventID, isEvent);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void getFixedPinID(final Map map, String groupEventID, Boolean isEvent) {
        Query groupeventnode = null;
        if (isEvent) {
            groupeventnode = mFirebaseRef.child("events").child(groupEventID);
        } else {
            groupeventnode = mFirebaseRef.child("groups").child(groupEventID);
        }
        groupeventnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count5 += 1;
                String pinID = (String) dataSnapshot.child("pinID").getValue();
                map.put("fixedPin", pinID);
                getFixedPinAddress(map, pinID);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void getFixedPinAddress(final Map map, String pinID) {
        Query fixedpointnode = mFirebaseRef.child("fixedpins").child(pinID);
        fixedpointnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count6 += 1;
                String address = (String) dataSnapshot.child("address").getValue();
                map.put("fixedPinAddress", address);
                getOtherPinAddress(map);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void getOtherPinAddress(final Map map) {
        String otherPinID = (String) map.get("pinID");
        String rideType = (String) map.get("rideType");
        Query otherpinnode = null;
        if (rideType == "goingtorides") {
            otherpinnode = mFirebaseRef.child("goingtopins").child(otherPinID);
        } else if (rideType == "leavingfromrides") {
            otherpinnode = mFirebaseRef.child("leavingfrompins").child(otherPinID);
        }
        otherpinnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count7 += 1;
                String address = (String) dataSnapshot.child("address").getValue();
                map.put("otherPinAddress", address);
                if (isDataReady()) {
                    joinedData.add(map);
                    joinedAdapter = new MyAdapter(context, joinedData, true);
                    requestAdapter = new MyAdapter(context requestData, false);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private Boolean isDataReady() {
        if (count1 == count2 && count1 == count3 && count1 == count4 && count1 == count5 && count1 == count6 && count1 == count7) {
            return true;
        } else return false;
    }





    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<Map<String, String>> adapterData;
        private Boolean isJoined;

        public MyAdapter(Context context, List adapterData, Boolean isJoined) {
            this.mInflater = LayoutInflater.from(context);
            this.adapterData = adapterData;
            this.isJoined = isJoined;
        }

        @Override
        public int getCount() {
            return adapterData.size();
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

            if (convertView == null) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.activity_joinedride_item, null);
                holder.beginpoint = (TextView) convertView.findViewById(R.id.departureAddress);
                holder.endpoint = (TextView) convertView.findViewById(R.id.arrivalAddress);
                holder.ridedate = (TextView) convertView.findViewById(R.id.dateInfo);
                holder.ridedeparturetime = (TextView) convertView.findViewById(R.id.timeDepartureInfo);
                holder.ridearrivaltime = (TextView) convertView.findViewById(R.id.timeArrivalInfo);
                holder.drivername = (TextView) convertView.findViewById(R.id.driversName);
                holder.btn_contact = (Button) convertView.findViewById(R.id.contactDriver);
                holder.btn_cancel = (Button) convertView.findViewById(R.id.cancelRide);
                holder.btn_showmap = (Button) convertView.findViewById(R.id.showRoute);
                holder.arrow = (ImageView) convertView.findViewById(R.id.timeArrow);

                if (adapterData.get(position).get("rideType") == "goingtorides") {
                    holder.arrow.setVisibility(View.VISIBLE);
                    holder.beginpoint.setText((String) adapterData.get(position).get("otherPinAddress"));
                    holder.endpoint.setText((String) adapterData.get(position).get("fixedPinAddress"));
                    holder.ridearrivaltime.setText((String) adapterData.get(position).get("arrivalTime"));

                } else if (adapterData.get(position).get("rideType") == "leavingfromrides") {
                    holder.beginpoint.setText((String) adapterData.get(position).get("fixedPinAddress"));
                    holder.endpoint.setText((String) adapterData.get(position).get("otherPinAddress"));
                }
                holder.ridedate.setText((String) adapterData.get(position).get("date"));
                holder.ridedeparturetime.setText((String) adapterData.get(position).get("departureTime"));
                holder.drivername.setText((String) adapterData.get(position).get("driverFirstName") + " " + (String) adapterData.get(position).get("driverLastName"));

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    String bookingID = adapterData.get(position).get("bookingID");
                    String rideID = adapterData.get(position).get("rideID");
                    String rideType = adapterData.get(position).get("rideType");
                    String driverID = adapterData.get(position).get("driverID");
                    deleteBooking(bookingID, rideID, rideType, false, isJoined);
                }
            });
            holder.btn_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(JoinedRidesActivity.this, ViewProfileActivity.class);
                    intent.putExtra("uid", (String) adapterData.get(position).get("driverID"));
                    intent.putExtra("showPhoneNumber", true);
                    startActivity(intent);
                }
            });
            holder.btn_showmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    //TODO set the right link to the map once it's designed
                    Intent intent = new Intent(JoinedRidesActivity.this, MapsActivity.class);
                    intent.putExtra("callingActivity", "JoinedRidesActivity");
                    List<String> pins = new ArrayList();
                    pins.add(adapterData.get(position).get("pinID"));
                    pins.add(adapterData.get(position).get("fixedPin"));
                    intent.putExtra("pins", (Serializable) pins);
                    startActivity(intent);
                }
            });
            return convertView;
        }

    }


    private void deleteBooking(String bookingID, String rideID, String rideType, Boolean isRequest, Boolean isJoined) {
        Map<String, Object> deleteBooking = new HashMap<>();
        deleteBooking.put(bookingID, null);
        Firebase bookingnode = mFirebaseRef.child("bookings").child(bookingID);
        Firebase usernode = null;
        if (isRequest) {
            usernode = mFirebaseRef.child("users").child(mAuthData.getUid()).child("pendingJoinRequests");
        } else {
            usernode = mFirebaseRef.child("users").child(mAuthData.getUid()).child("bookingsMade");

        }
        Firebase ridenode = mFirebaseRef.child(rideType).child(rideID).child("bookings");
        usernode.updateChildren(deleteBooking);
        bookingnode.removeValue();
        ridenode.updateChildren(deleteBooking);
        updateJoinedRidesDisplayed(bookingID, isJoined);
    }


    private void updateJoinedRidesDisplayed(String bookingID, Boolean isJoined) {
        Iterator bookingsIterator = joinedData.iterator();
        while (bookingsIterator.hasNext()) {
            Map booking = (Map) bookingsIterator.next();
            if (booking.containsValue(bookingID)) {
                System.out.println(booking.toString());
                bookingsIterator.remove();
                break;
            }
        }
        if (isJoined) {
            joinedAdapter.notifyDataSetChanged();
        } else {
            requestAdapter.notifyDataSetChange();
        }
    }

    public final class ViewHolder {
        public TextView beginpoint;
        public TextView endpoint;
        public TextView ridedate;
        public TextView ridedeparturetime;
        public TextView ridearrivaltime;
        public TextView drivername;
        public Button btn_showmap;
        public Button btn_contact;
        public Button btn_cancel;
        public ImageView arrow;
    }

}