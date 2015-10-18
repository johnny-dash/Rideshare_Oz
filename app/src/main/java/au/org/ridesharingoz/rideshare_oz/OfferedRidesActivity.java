package au.org.ridesharingoz.rideshare_oz;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.net.MalformedURLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CheckedInputStream;


public class OfferedRidesActivity extends FirebaseAuthenticatedActivity {

    ArrayList<OfferedRide> offeredRideList = new ArrayList<OfferedRide>();
    List<Map> offeredRideBookings;
    ListView rideexpandablelistview;
    private int count1 = 2;
    private int count2 = 0;
    private int count3 = 0;
    private int count4 = 0;
    private int count5 = 0;
    private int countpins = 0;
    private int countBooking;
    private int countPendingBooking;
    private Boolean isGoingTo;
    Activity thisActivity = this;
    private OfferedExpandableListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offered_rides);

        createData();

        rideexpandablelistview = (ExpandableListView) findViewById(R.id.offeredRidesExpandableListview);


    }


    public void createData(){
        final List<Ride> rides = new ArrayList<>();
        Query goingtoridesnode = mFirebaseRef.child("goingtorides").orderByChild("driverID").equalTo(mAuthData.getUid());
        goingtoridesnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count1 -= 1;
                for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                    count1 += 1;
                    System.out.println("count1: " + count1);
                    List<String> bookingIDs = new ArrayList<>();
                    List<String> pendingBookingIDs = new ArrayList<>();
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
                    rides.add(ride);
                    getOtherPinID(ride, departurePinID, bookingIDs, pendingBookingIDs, rideID);
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
                    if (rideSnapshot.hasChild("bookings")) {
                        System.out.println("Has bookings");
                        DataSnapshot bookingsSnapshot = rideSnapshot.child("bookings");
                        for (DataSnapshot bookingID : bookingsSnapshot.getChildren()) {
                            bookingIDs.add(bookingID.getKey());
                        }
                    }
                    if (rideSnapshot.hasChild(("pendingJoinRequests"))) {
                        System.out.println("Has pending booking");
                        DataSnapshot pendingBookingsSnapshot = rideSnapshot.child("pendingJoinRequests");
                        for (DataSnapshot bookingID : pendingBookingsSnapshot.getChildren()) {
                            pendingBookingIDs.add(bookingID.getKey());
                        }
                    }
                    for (DataSnapshot pinID : rideSnapshot.child("pins").getChildren()) {
                        if (pinID.getValue().equals("arrival")) {
                            arrivalPinID = pinID.getKey();
                            System.out.println("arrivalPinID: " + arrivalPinID);
                        }
                    }
                    rides.add(ride);
                    getOtherPinID(ride, arrivalPinID, bookingIDs, pendingBookingIDs, rideID);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



    private void getOtherPinID(final Ride ride, final String pinID, final List<String> bookingIDs, final List<String> pendingBookingIDs, final String rideID){
        Query groupEventNode = mFirebaseRef;
        if (ride.getIsEvent()){
           groupEventNode  = mFirebaseRef.child("events").child(ride.getGroupEventID()).child("pinID");
        }
        else {
            groupEventNode = mFirebaseRef.child("groups").child(ride.getGroupEventID()).child("pinID");
        }
        groupEventNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String otherPinID = (String) dataSnapshot.getValue();
                List<String> pins = new ArrayList<>();
                if (ride.getType().equals("goingto")){
                    pins.add(0, otherPinID);
                    pins.add(1, pinID);
                }
                else if (ride.getType().equals("leavingfrom")){
                    pins.add(0, pinID);
                    pins.add(1, otherPinID);
                }
                getPinsAddresses(ride, pins, bookingIDs, pendingBookingIDs, rideID);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }




    private void getPinsAddresses(final Ride ride, List<String> pins, final List<String> bookingIDs, final List<String> pendingBookingIDs, final String rideID){
        final List<String> addresses = new ArrayList<>();
        System.out.println("I'm in getPinAddress");
        countpins = 0;
        Query pinnode0 = mFirebaseRef;
        Query pinnode1 = mFirebaseRef;
        if (ride.getType().equals("goingto")){
            isGoingTo = true;
            pinnode0 = mFirebaseRef.child("goingtopins").child(pins.get(0));
            pinnode1 = mFirebaseRef.child("fixedpins").child(pins.get(1));
        }
        else if (ride.getType().equals("leavingfrom")){
            isGoingTo = false;
            pinnode0 = mFirebaseRef.child("fixedpins").child(pins.get(0));
            pinnode1 = mFirebaseRef.child("leavingfrompins").child(pins.get(1));
        }
        pinnode0.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countpins += 1;
                addresses.add(0, (String) dataSnapshot.child("address").getValue());
                if (countpins == 2){
                    Map<String, Object> map = new HashMap<String, Object>();
                    OfferedRide offeredRide = new OfferedRide(addresses.get(0), addresses.get(1), isGoingTo, ride.getSeatNum(), rideID);
                    if (isGoingTo){
                        Timestamp arrivalTimestamp = ride.getTimestamp();
                        long departureLong = (long) dataSnapshot.child("timestamp").getValue();
                        Timestamp departureTimestamp = new Timestamp(departureLong);
                        String date = new SimpleDateFormat("dd/MM/yyyy").format(departureTimestamp);
                        String arrivalTime = new SimpleDateFormat("HH:mm").format(arrivalTimestamp);
                        String departureTime = new SimpleDateFormat("HH:mm").format(departureTimestamp);
                        offeredRide.setArrivalTime(arrivalTime);
                        offeredRide.setDate(date);
                        offeredRide.setDeparture(departureTime);
                    }
                    else{
                        Timestamp departureTimestamp = ride.getTimestamp();
                        String date = new SimpleDateFormat("dd/MM/yyyy").format(departureTimestamp);
                        String departureTime = new SimpleDateFormat("HH:mm").format(departureTimestamp);
                        offeredRide.setDeparture(departureTime);
                        offeredRide.setDate(date);
                    }
                    if (bookingIDs.size()>0 || pendingBookingIDs.size()>0){
                        getBookingsInfo(offeredRide, bookingIDs, pendingBookingIDs);
                    }
                    else{
                        count4 +=1;
                        offeredRideList.add(offeredRide);
                        if (count4 == count1){

                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        pinnode1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countpins += 1;
                addresses.add(1, (String) dataSnapshot.child("address").getValue());
                if (countpins == 2){
                    OfferedRide offeredRide = new OfferedRide(addresses.get(0), addresses.get(1), isGoingTo, ride.getSeatNum(), rideID);
                    if (isGoingTo){
                        Timestamp arrivalTimestamp = ride.getTimestamp();
                        long departureLong = (long) dataSnapshot.child("timestamp").getValue();
                        Timestamp departureTimestamp = new Timestamp(departureLong);
                        String date = new SimpleDateFormat("dd/MM/yyyy").format(departureTimestamp);
                        String arrivalTime = new SimpleDateFormat("HH:mm").format(arrivalTimestamp);
                        String departureTime = new SimpleDateFormat("HH:mm").format(departureTimestamp);
                        offeredRide.setArrivalTime(arrivalTime);
                        offeredRide.setDate(date);
                        offeredRide.setDeparture(departureTime);
                    }
                    else{
                        Timestamp departureTimestamp = ride.getTimestamp();
                        String date = new SimpleDateFormat("dd/MM/yyyy").format(departureTimestamp);
                        String departureTime = new SimpleDateFormat("HH:mm").format(departureTimestamp);
                        offeredRide.setDeparture(departureTime);
                        offeredRide.setDate(date);
                    }
                    if (bookingIDs.size()>0 || pendingBookingIDs.size()>0){
                        getBookingsInfo(offeredRide, bookingIDs, pendingBookingIDs);
                    }
                    else{
                        offeredRideList.add(offeredRide);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void getBookingsInfo(OfferedRide offeredRide, List<String> bookingIDs, List<String> pendingBookingIDs){
        count4 +=1;
        countBooking = bookingIDs.size();
        countPendingBooking = pendingBookingIDs.size();
        offeredRideBookings = new ArrayList<>();
        count2 = 0;
        if (countBooking>0) {
            for (String bookingID : bookingIDs) {
                count3 += 1;
                Map map = new HashMap();
                map.put("bookingNode", "bookings");
                map.put("bookingID", bookingID);
                getBookingDetails(map, offeredRide);
            }
        }
        if (countPendingBooking>0){
            for (String pendingBookingID : pendingBookingIDs){
                count3 += 1;
                Map map = new HashMap();
                map.put("bookingNode", "pendingbookings");
                map.put("bookingID", pendingBookingID);
                getBookingDetails(map, offeredRide);
            }
        }

    }



    private void getBookingDetails(final Map<String, Object> map, final OfferedRide offeredRide){
        Query bookingnode = mFirebaseRef.child((String) map.get("bookingNode")).child((String) map.get("bookingID"));
        bookingnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (offeredRide.getIsGoingTo()) {
                    Timestamp departureTimestamp = (Timestamp) dataSnapshot.child("departureTime").getValue();
                    String departureTime = new SimpleDateFormat("HH:mm").format(departureTimestamp);
                    map.put("departureTime", departureTime);
                }
                map.put("passengerID", dataSnapshot.child("userID").getValue());
                map.put("pinID", dataSnapshot.child("pinID").getValue());
                map.put("pinNode", dataSnapshot.child("rideType").getValue() + "pins");
                getPassengerName(map, offeredRide);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void getPassengerName(final Map<String, Object> map, final OfferedRide offeredRide){
        Query usernode = mFirebaseRef.child((String) map.get("passengerID"));
        usernode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                map.put("passengerName", (String) dataSnapshot.child("firstName").getValue() + dataSnapshot.child("lastName").getValue());
                map.put("rating", "Rating: " + dataSnapshot.child("rating").child("asPassenger").getValue() + " (" + dataSnapshot.child("rating").child("pRatingNb").getValue() + " ratings)");
                getPinAddress(map, offeredRide);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void getPinAddress(final Map<String, Object> map, final OfferedRide offeredRide){
        Query pinnode = mFirebaseRef.child((String) map.get("pinNode")).child((String) map.get("pinID"));
        pinnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count2 +=1;
                map.put("pinAddress", dataSnapshot.child("address").getValue());
                offeredRide.addBooking(map);
                if (count2 == countBooking+countPendingBooking && count4 == count1){
                    adapter = new OfferedExpandableListAdapter(thisActivity, offeredRideList);
                    rideexpandablelistview.setAdapter((ListAdapter) adapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
                holder.btn_accept = (Button) convertView.findViewById(R.id.acceptPassengerButton);
                holder.btn_profile = (Button) convertView.findViewById(R.id.profilePassengerButton);
                holder.btn_refuse = (Button) convertView.findViewById(R.id.refusePassengerButton);
                if (offeredRideBookings.get("bookingNode").equals("bookings")){
                    holder.btn_refuse.setVisibility(View.INVISIBLE);
                    holder.btn_accept.setVisibility(View.INVISIBLE);
                }
                holder.btn_refuse.setFocusable(false);
                holder.btn_accept.setFocusable(false);
                holder.btn_profile.setFocusable(false);
                nameTextview = (TextView) convertView.findViewById(R.id.passengerName);
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

                }
            });
            holder.btn_refuse.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {

                }
            });
            holder.btn_accept.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {

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
                seatnum.setText(offeredRide.getSeatNumber());
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
        protected Button btn_profile;
        protected Button btn_refuse;
        protected Button btn_accept;
    }


}


