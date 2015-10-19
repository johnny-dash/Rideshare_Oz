package au.org.ridesharingoz.rideshare_oz.ridesPackage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import au.org.ridesharingoz.rideshare_oz.R;
import au.org.ridesharingoz.rideshare_oz.dataPackage.Ride;
import au.org.ridesharingoz.rideshare_oz.firebasePackage.FirebaseAuthenticatedActivity;
import au.org.ridesharingoz.rideshare_oz.userPackage.ViewProfileActivity;


public class OfferedRidesActivity extends FirebaseAuthenticatedActivity {

    ArrayList<OfferedRide> offeredRideList = new ArrayList<OfferedRide>();
    ExpandableListView rideexpandablelistview;
    List<Map<String, Object>> rides;
    private int count1 = 2;
    private int count2 = 0;
    private int count3 = 0;
    private int count4 = 0;
    private int count6 = 0;
    private int countpin0 = 0;
    private int countpin1 = 0;
    private int countTotalDriverBookings;
    private int countBooking;
    private int countRides = 5;
    private int countPendingBooking;
    private int countTotalBooking;
    private int countRidesThatHaveBookings;
    private int countPassengerNames;
    private List<Map> bookingList;
    private Boolean isGoingTo;
    String arrivalTimeBis = "";
    String departureTimeBis = "";
    String dateBis = "";
    Activity thisActivity = this;
    private OfferedExpandableListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offered_rides);

        createData();

        rideexpandablelistview = (ExpandableListView) findViewById(R.id.offeredRidesExpandableListview);

        rideexpandablelistview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                parent.expandGroup(groupPosition);
                return false;
            }
        });


    }


    public void createData(){
        rides = new ArrayList<Map<String, Object>>();
        Query goingtoridesnode = mFirebaseRef.child("goingtorides").orderByChild("driverID").equalTo(mAuthData.getUid());
        goingtoridesnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count1 -= 1;
                countRides -=5;
                for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                    count1 += 1;
                    System.out.println("count1: " + count1);
                    List<String> bookingIDs = new ArrayList<>();
                    List<String> pendingBookingIDs = new ArrayList<>();
                    System.out.println((rideSnapshot.getValue().toString()));
                    System.out.println(rideSnapshot.getKey());
                    Ride ride = rideSnapshot.getValue(Ride.class);
                    String rideID = rideSnapshot.getKey();
                    if (rideSnapshot.hasChild("bookings")) {
                        System.out.println("Has bookings");
                        DataSnapshot bookingsSnapshot = rideSnapshot.child("bookings");
                        for (DataSnapshot bookingID : bookingsSnapshot.getChildren()){
                            bookingIDs.add(bookingID.getKey());
                        }
                    }
                    if (rideSnapshot.hasChild(("pendingJoinRequests"))) {
                        System.out.println("Has pending booking");
                        DataSnapshot pendingBookingsSnapshot = rideSnapshot.child("pendingJoinRequests");
                        for (DataSnapshot bookingID : pendingBookingsSnapshot.getChildren()){
                            pendingBookingIDs.add(bookingID.getKey());
                        }
                    }


                    String departurePinID = "";
                    for (DataSnapshot pinID : rideSnapshot.child("pins").getChildren()) {
                        if (pinID.getValue().equals("departure")) {
                            departurePinID = pinID.getKey();
                            System.out.println("departurePinID: " + departurePinID);
                        }
                    }
                    Map map = new HashMap();
                    map.put("ride", ride);
                    map.put("pinID", departurePinID);
                    map.put("bookingIDs", bookingIDs);
                    map.put("pendingBookingIDs", pendingBookingIDs);
                    map.put("rideID", rideID);
                    Query groupEventNode = mFirebaseRef;
                    if (ride.getIsEvent()) {
                        groupEventNode = mFirebaseRef.child("events").child(ride.getGroupEventID()).child("pinID");
                    } else {
                        groupEventNode = mFirebaseRef.child("groups").child(ride.getGroupEventID()).child("pinID");
                    }
                    map.put("groupEventNode", groupEventNode);
                    rides.add(map);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Query leavingfromrides = mFirebaseRef.child("leavingfromrides").orderByChild("driverID").equalTo(mAuthData.getUid());
        leavingfromrides.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count1 -=1;
                System.out.println("count1 in leavingfromridesnode: " + count1);
                for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                    count1 += 1;
                    System.out.println("count1: " + count1);
                    Ride ride = rideSnapshot.getValue(Ride.class);
                    String rideID = rideSnapshot.getKey();
                    String arrivalPinID = "";
                    List<String> bookingIDs = new ArrayList<>();
                    List<String> pendingBookingIDs = new ArrayList<>();
                    Map map = new HashMap();
                    if (rideSnapshot.hasChild("bookings")) {
                        System.out.println("Has bookings");
                        DataSnapshot bookingsSnapshot = rideSnapshot.child("bookings");
                        for (DataSnapshot bookingID : bookingsSnapshot.getChildren()) {
                            bookingIDs.add(bookingID.getKey());
                        }
                        map.put("bookingIDs", bookingIDs);
                    }
                    if (rideSnapshot.hasChild(("pendingJoinRequests"))) {
                        System.out.println("Has pending booking");
                        DataSnapshot pendingBookingsSnapshot = rideSnapshot.child("pendingJoinRequests");
                        for (DataSnapshot bookingID : pendingBookingsSnapshot.getChildren()) {
                            pendingBookingIDs.add(bookingID.getKey());
                        }
                        map.put("pendingBookingIDs", pendingBookingIDs);
                    }
                    for (DataSnapshot pinID : rideSnapshot.child("pins").getChildren()) {
                        if (pinID.getValue().equals("arrival")) {
                            arrivalPinID = pinID.getKey();
                            System.out.println("arrivalPinID: " + arrivalPinID);
                        }
                    }

                    map.put("ride", ride);
                    map.put("pinID", arrivalPinID);
                    map.put("rideID", rideID);
                    Query groupEventNode = mFirebaseRef;
                    if (ride.getIsEvent()) {
                        groupEventNode = mFirebaseRef.child("events").child(ride.getGroupEventID()).child("pinID");
                    } else {
                        groupEventNode = mFirebaseRef.child("groups").child(ride.getGroupEventID()).child("pinID");
                    }
                    map.put("groupEventNode", groupEventNode);
                    rides.add(map);

                }
                if(count1 == rides.size()) {
                    getOtherPinID();
                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



    private void getOtherPinID(){
        for (final Map map: rides) {

            final Ride ride = (Ride) map.get("ride");
            final String pinID = (String) map.get("pinID");
            final String rideID = (String) map.get("rideID");

            ((Query) map.get("groupEventNode")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    countRides += 1;
                    String otherPinID = (String) dataSnapshot.getValue();
                    List<String> pins = new ArrayList<>();
                    if (ride.getType().equals("goingto")) {
                        System.out.println("Do I get to pu the otherPin 1 in the pins list?");
                        pins.add(0, pinID);
                        pins.add(1, otherPinID);
                    } else if (ride.getType().equals("leavingfrom")) {
                        System.out.println("Do I get to pu the otherPin 0 in the pins list?");
                        pins.add(0, otherPinID);
                        pins.add(1, pinID);
                    }
                    System.out.println(pins.toString());
                    System.out.println("rideID: " + rideID);
                    map.put("pins", pins);
                    System.out.println("pins: " + pins.get(0) + pins.get(1));
                    System.out.println("Number of rides: " + countRides + "/" + rides.size());
                    if (countRides == rides.size()) {
                        getPinsAddresses();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }




    private void getPinsAddresses() {
        for(final Map map : rides) {
            countpin1 = 0;
            countpin0 = 0;
            System.out.println("I'm in getPinAddress");
            final Ride ride = (Ride) map.get("ride");
            final String pinID = (String) map.get("pinID");
            final List<String> bookingIDs = (List<String>) map.get("bookingIDs");
            final List<String> pendingBookingIDs = (List<String>) map.get("pendingBookingIDs");
            final String rideID = (String) map.get("rideID");
            final List<String> pins = (List<String>) map.get("pins");

            Query pinnode0 = mFirebaseRef;
            Query pinnode1a = mFirebaseRef;
            if (ride.getType().equals("goingto")) {
                isGoingTo = true;
                pinnode0 = mFirebaseRef.child("goingtopins").child(pins.get(0));
                pinnode1a = mFirebaseRef.child("fixedpins").child(pins.get(1));
            } else if (ride.getType().equals("leavingfrom")) {
                isGoingTo = false;
                pinnode0 = mFirebaseRef.child("fixedpins").child(pins.get(0));
                pinnode1a = mFirebaseRef.child("leavingfrompins").child(pins.get(1));
            }
            final Query pinnode1 = pinnode1a;

            pinnode0.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    countpin0 += 1;
                    System.out.println("countpins, pinnode0: " + countpin0 + " " + countpin1);
                    String address0 = (String) dataSnapshot.child("address").getValue();
                    String arrivalTime = "";
                    String date;
                    String departureTime;
                    if (isGoingTo) {
                        Timestamp arrivalTimestamp = ride.getTimestamp();
                        long departureLong = (long) dataSnapshot.child("timestamp").getValue();
                        Timestamp departureTimestamp = new Timestamp(departureLong);
                        date = new SimpleDateFormat("dd/MM/yyyy").format(departureTimestamp);
                        System.out.println(date);
                        arrivalTime = new SimpleDateFormat("HH:mm").format(arrivalTimestamp);
                        System.out.println(arrivalTime);
                        departureTime = new SimpleDateFormat("HH:mm").format(departureTimestamp);
                        System.out.println(departureTime);
                        map.put("arrivalTime", arrivalTime);
                    } else {
                        Timestamp departureTimestamp = ride.getTimestamp();
                        date = new SimpleDateFormat("dd/MM/yyyy").format(departureTimestamp);
                        departureTime = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(departureTimestamp);
                    }
                    map.put("date", date);
                    map.put("address0", address0);
                    map.put("departureTime", departureTime);
                    map.put("isGoingTo", isGoingTo);


                    pinnode1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("countpins, pinnode1: " + countpin0 + " " + countpin1);
                            String address1 = (String) dataSnapshot.child("address").getValue();
                            OfferedRide offeredRide = new OfferedRide((String) map.get("address0"), address1, (Boolean) map.get("isGoingTo"), ride.getSeatNum(), rideID);
                            if (map.containsKey("arrivalTime")) {
                                offeredRide.setArrivalTime((String) map.get("arrivalTime"));
                            }
                            offeredRide.setDate((String) map.get("date"));
                            offeredRide.setDepartureTime((String) map.get("departureTime"));
                            //if (countpin0 == countpin1) {
                                //if (bookingIDs.size() > 0 || pendingBookingIDs.size() > 0) {
                                //    getBookingsInfo(offeredRide, bookingIDs, pendingBookingIDs);
                                //} else {
                                    count4 += 1;
                                    System.out.println("count4 : " + count4);
                                    map.put("offeredRide", offeredRide);
                                    System.out.println("the ride doesn't have a booking");
                                    if (count4 == rides.size()) {
                                        getBookingsInfo();
                                        //System.out.println("This is where the adapter is set");
                                        //adapter = new OfferedExpandableListAdapter(thisActivity, offeredRideList);
                                        //rideexpandablelistview.setAdapter(adapter);
                                    }
                                }

                            //}
                       // }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
    }



    private void getBookingsInfo(/*OfferedRide offeredRide, List<String> bookingIDs, List<String> pendingBookingIDs*/){
        System.out.println("I'm in getBookingInfo");
        for (Map map : rides) {
            countBooking =0;
            countPendingBooking = 0;
            count3 += 1;
            bookingList = new ArrayList<>();
            if (map.containsKey("bookingIDs")) {
                countBooking = ((List) map.get("bookingIDs")).size();
            }
            if (map.containsKey("pendingBookingIDs")){
                countPendingBooking = ((List) map.get("pendingBookingIDs")).size();
            }
            List<Map> offeredRideBookings = new ArrayList<>();

            if (countBooking > 0) {
                for (String bookingID : (List<String>) map.get("bookingIDs")) {
                    Map mapBooking = new HashMap();
                    System.out.println("bookingID: " + bookingID);
                    mapBooking.put("bookingNode", "bookings");
                    mapBooking.put("bookingID", bookingID);
                    offeredRideBookings.add(mapBooking);
                }

            }
            if (countPendingBooking > 0) {
                for (String pendingBookingID : (List<String>) map.get("pendingBookingIDs")) {
                    Map mapBooking = new HashMap();
                    System.out.println("pendingBookingID: " + pendingBookingID);
                    mapBooking.put("bookingNode", "pendingbookings");
                    mapBooking.put("bookingID", pendingBookingID);
                    offeredRideBookings.add(mapBooking);
                }
            }
            if (countBooking>0 || countPendingBooking>0) {
                map.put("offeredRideBookings", offeredRideBookings);
            }
        }
        if (count3 == rides.size()) {
            getBookingDetails();
        }
    }



    private void getBookingDetails(/*final Map map*/){
        System.out.println("I'm in getBookingDetails");
        countTotalDriverBookings = 0;
        countRidesThatHaveBookings = 0;
        for (final Map map : rides) {
            if (map.containsKey("offeredRideBookings")) {
                countRidesThatHaveBookings += 1;
                countTotalDriverBookings += ((List) map.get("offeredRideBookings")).size();
                System.out.println("countTotalDriverBookings: " + countTotalDriverBookings);
                System.out.println("nb of rides that have bookings: " + countRidesThatHaveBookings);
                for (final Map booking : (List<Map>) map.get("offeredRideBookings")) {
                    countTotalBooking = 0 ;
                    System.out.println("offeredRideBookings size: " + ((List) map.get("offeredRideBookings")).size());
                    Query bookingnode = mFirebaseRef.child((String) booking.get("bookingNode")).child((String) booking.get("bookingID"));
                    System.out.println("bookingID: " + (String) booking.get("bookingID"));
                    bookingnode.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println(booking.toString());
                            countTotalBooking += 1;
                            System.out.println("countTotalBooking: " + countTotalBooking);
                            if (((OfferedRide) map.get("offeredRide")).getIsGoingTo()) {
                                System.out.println("Do I get in the condition if?");
                                Timestamp departureTimestamp = (Timestamp) dataSnapshot.child("departureTime").getValue();
                                String departureTime = new SimpleDateFormat("HH:mm").format(departureTimestamp);
                                booking.put("departureTime", departureTime);
                            }
                            booking.put("passengerID", dataSnapshot.child("userID").getValue());
                            booking.put("pinID", dataSnapshot.child("pinID").getValue());
                            booking.put("pinNode", dataSnapshot.child("rideType").getValue() + "pins");
                            ((OfferedRide) map.get("offeredRide")).addBooking(booking);

                            if (((List) map.get("offeredRideBookings")).size() == countTotalBooking) {
                                countTotalBooking = 0;
                                offeredRideList.add((OfferedRide) map.get("offeredRide"));
                                System.out.println("the ride is added to offered ride");
                                if (offeredRideList.size() == rides.size()) {
                                    getPassengerName();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
            else{

                offeredRideList.add((OfferedRide) map.get("offeredRide"));
                if (offeredRideList.size() == rides.size()){
                    adapter = new OfferedExpandableListAdapter(thisActivity, offeredRideList);
                    rideexpandablelistview.setAdapter(adapter);
                }
            }
        }
    }


    private void getPassengerName(/*final Map<String, Object> map, final OfferedRide offeredRide*/) {
        System.out.println("I'm in getPassengerName");
        for (final OfferedRide offeredRide : offeredRideList) {
            if (offeredRide.offeredRideBookings.size() > 0) {
                countPassengerNames = 0;

                for (final Map map : offeredRide.getOfferedRideBookings()) {
                    Query usernode = mFirebaseRef.child("users").child((String) map.get("passengerID"));
                    usernode.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            countPassengerNames += 1;
                            count6 += 1;
                            map.put("passengerName", (String) dataSnapshot.child("firstName").getValue() + dataSnapshot.child("lastName").getValue());
                            map.put("rating", "Rating: " + dataSnapshot.child("rating").child("asPassenger").getValue() + " (" + dataSnapshot.child("rating").child("pRatingNb").getValue() + " ratings)");
                            System.out.println("passenger name: " + (String) dataSnapshot.child("firstName").getValue() + dataSnapshot.child("lastName").getValue());
                            System.out.println("Rating: " + dataSnapshot.child("rating").child("asPassenger").getValue() + " (" + dataSnapshot.child("rating").child("pRatingNb").getValue() + " ratings)");
                            if (countPassengerNames == offeredRide.getOfferedRideBookings().size()){
                                countPassengerNames = 0;
                                if (count6 == countTotalDriverBookings) {
                                    getPinAddress();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        }
    }

    private void getPinAddress(/*final Map<String, Object> map, final OfferedRide offeredRide*/){
        System.out.println("I'm in getPinAddress");
        for (final OfferedRide offeredRide : offeredRideList) {
            if (offeredRide.getOfferedRideBookings().size()>0) {
                for (final Map map : offeredRide.getOfferedRideBookings()) {
                    Query pinnode = mFirebaseRef.child((String) map.get("pinNode")).child((String) map.get("pinID"));
                    pinnode.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            count2 += 1;
                            map.put("pinAddress", dataSnapshot.child("address").getValue());
                            System.out.println(dataSnapshot.child("address").getValue());
                            if (count2 == countTotalDriverBookings && count4 == rides.size()) {
                                System.out.println("This is when the adapter is set");
                                adapter = new OfferedExpandableListAdapter(thisActivity, offeredRideList);
                                adapter.notifyDataSetChanged();
                                rideexpandablelistview.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        }
    }







    class OfferedExpandableListAdapter extends BaseExpandableListAdapter {

        private final ArrayList<OfferedRide> items;
        public LayoutInflater inflater;
        public Activity activity;
        private final int[] EMPTY_STATE_SET = {};
        private final int[] GROUP_EXPANDED_STATE_SET =
                {android.R.attr.state_expanded};
        private final int[][] GROUP_STATE_SETS = {
                EMPTY_STATE_SET, // 0
                GROUP_EXPANDED_STATE_SET // 1
        };

        public int[][] getGROUP_STATE_SETS() {
            return GROUP_STATE_SETS;
        }

        public OfferedExpandableListAdapter(Activity act, ArrayList<OfferedRide> items) {
            activity = act;
            this.items = items;
            inflater = act.getLayoutInflater();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).offeredRideBookings.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }


        @Override
        public View getChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final Map<String, String> offeredRideBookings = (Map<String, String>) getChild(groupPosition, childPosition);


            ChildViewHolder holder = null;
            TextView nameTextview;
            TextView addressTimeTextView;
            TextView ratingTextView;
            if (convertView == null) {
                holder = new ChildViewHolder();
                convertView = inflater.inflate(R.layout.expandablelistview_offeredrides_itemdetails, null);
                holder.btn_accept = (ImageButton) convertView.findViewById(R.id.acceptPassengerButton);
                holder.btn_profile = (ImageButton) convertView.findViewById(R.id.profilePassengerButton);
                holder.btn_refuse = (ImageButton) convertView.findViewById(R.id.refusePassengerButton);
                if (offeredRideBookings.get("bookingNode").equals("bookings")){
                    holder.btn_refuse.setVisibility(View.INVISIBLE);
                    holder.btn_accept.setVisibility(View.INVISIBLE);
                }
                holder.btn_refuse.setFocusable(false);
                holder.btn_accept.setFocusable(false);
                holder.btn_profile.setFocusable(false);
                nameTextview = (TextView) convertView.findViewById(R.id.passengerNameOffered);
                addressTimeTextView = (TextView) convertView.findViewById(R.id.bookingPlace);
                ratingTextView = (TextView) convertView.findViewById(R.id.passengerBookingRating);
                nameTextview.setText(offeredRideBookings.get("passengerName"));
                addressTimeTextView.setText(offeredRideBookings.get("departureTime") + "  " + offeredRideBookings.get("pinAddress"));
                ratingTextView.setText(offeredRideBookings.get("rating"));

                convertView.setTag(holder);

            } else {
                holder = (ChildViewHolder) convertView.getTag();
            }

            holder.btn_profile.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    Map offeredRideBookings = (Map) adapter.getChild(groupPosition, childPosition);
                    Intent intent = new Intent(OfferedRidesActivity.this, ViewProfileActivity.class);
                    intent.putExtra("uid", (String) offeredRideBookings.get("passengerID"));
                    startActivity(intent);
                }
            });
            holder.btn_refuse.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    Map offeredRideBookings = (Map) adapter.getChild(groupPosition, childPosition);

                    Toast.makeText(getApplicationContext(), "The request was refused.", Toast.LENGTH_LONG).show();
                }
            });
            holder.btn_accept.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    Map offeredRideBookings = (Map) adapter.getChild(groupPosition, childPosition);
                    Toast.makeText(getApplicationContext(), "The request was accepted.", Toast.LENGTH_LONG).show();
                }
            });
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return items.get(groupPosition).offeredRideBookings.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            super.onGroupCollapsed(groupPosition);
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            super.onGroupExpanded(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            TextView beginpoint = null;
            TextView endpoint = null;
            TextView ridedate = null;
            TextView ridedeparturetime = null;
            TextView ridearrivaltime = null;
            TextView seatnum = null;
            ImageView arrow = null;
            ParentViewHolder holder = null;

            if (convertView == null) {
                holder = new ParentViewHolder();
                convertView = inflater.inflate(R.layout.expandablelistview_offeredrides_item, null);
                holder.btn_cancel = (Button) convertView.findViewById(R.id.offered_cancelRide);
                holder.btn_showmap = (Button) convertView.findViewById(R.id.offered_showroute);
                OfferedRide offeredRide = (OfferedRide) getGroup(groupPosition);
                holder.btn_cancel.setFocusable(false);
                holder.btn_showmap.setFocusable(false);
                beginpoint = (TextView) convertView.findViewById(R.id.offered_BeginningAddress);
                endpoint = (TextView) convertView.findViewById(R.id.offered_FinishAddress);
                ridedate = (TextView) convertView.findViewById(R.id.dateOfferedInfo);
                ridedeparturetime = (TextView) convertView.findViewById(R.id.timeDepartureOfferedInfo);
                ridearrivaltime = (TextView) convertView.findViewById(R.id.timeArrivalOfferedInfo);
                arrow = (ImageView) convertView.findViewById(R.id.timeArrowConfirmed);
                seatnum = (TextView) convertView.findViewById(R.id.seatNum);
                beginpoint.setText(offeredRide.getDeparture());
                endpoint.setText(offeredRide.getArrival());
                ridedate.setText(offeredRide.getDate());
                ridedeparturetime.setText(offeredRide.getDepartureTime());
                seatnum.setText(String.valueOf(offeredRide.getSeatNumber()));
                if (offeredRide.getIsGoingTo()){
                    arrow.setVisibility(View.VISIBLE);
                    ridearrivaltime.setText(offeredRide.getArrivalTime());
                }
                ((CheckedTextView) beginpoint).setChecked(isExpanded);
                View ind = convertView.findViewById(R.id.offered_explist_indicator);
                if( ind != null ) {
                    ImageView indicator = (ImageView)ind;
                    if( getChildrenCount( groupPosition ) == 0 ) {
                        indicator.setVisibility( View.INVISIBLE );
                    } else {
                        indicator.setVisibility( View.VISIBLE );
                        int stateSetIndex = ( isExpanded ? 1 : 0) ;
                        Drawable drawable = indicator.getDrawable();
                        drawable.setState(getGROUP_STATE_SETS()[stateSetIndex]);
                    }
                }
                convertView.setTag(holder);
            }
            else{
                holder = (ParentViewHolder) convertView.getTag();
            }
            holder.btn_showmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                }
            });
            holder.btn_cancel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {

                }
            });

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

    }

    class OfferedRide {

        public String departure;
        public String arrival;
        public String date;
        public String departureTime;
        public String arrivalTime;
        public Boolean isGoingTo;
        public int seatNumber;
        public String rideID;
        public final List<Map> offeredRideBookings = new ArrayList<>();

        public OfferedRide(String departure, String arrival, Boolean isGoingTo, int seatNumber, String rideID) {
            this.departure = departure;
            this.arrival = arrival;
            this.isGoingTo = isGoingTo;
            this.seatNumber = seatNumber;
            this.rideID = rideID;
        }

        public void addBooking(Map booking){
            offeredRideBookings.add(booking);
        }


        public String getDeparture() {
            return departure;
        }

        public void setDeparture(String departure) {
            this.departure = departure;
        }

        public String getRideID() {
            return rideID;
        }

        public void setRideID(String rideID) {
            this.rideID = rideID;
        }

        public String getArrival() {
            return arrival;
        }

        public void setArrival(String arrival) {
            this.arrival = arrival;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public void setDepartureTime(String departureTime) {
            this.departureTime = departureTime;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

        public Boolean getIsGoingTo() {
            return isGoingTo;
        }

        public void setIsGoingTo(Boolean isGoingTo) {
            this.isGoingTo = isGoingTo;
        }

        public int getSeatNumber() {
            return seatNumber;
        }

        public void setSeatNumber(int seatNumber) {
            this.seatNumber = seatNumber;
        }

        public List<Map> getOfferedRideBookings() {
            return offeredRideBookings;
        }
    }


    static class ParentViewHolder {
        protected Button btn_showmap;
        protected Button btn_cancel;
    }

    static class ChildViewHolder {
        protected ImageButton btn_profile;
        protected ImageButton btn_refuse;
        protected ImageButton btn_accept;
    }


}


