package au.org.ridesharingoz.rideshare_oz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Ocunidee on 08/10/2015.
 */
public class GroupManagementPanelActivity extends FirebaseAuthenticatedActivity{


    private Button createEvent;
    private ListView requestsListView;
    private ListView eventsListView;
    private TextView groupNameTextView;
    private SimpleAdapter adapterRequests;
    private SimpleAdapter adapterEvents;
    private int countRequests = 1;
    private int countEvents = 2;
    private int count1 = 1;
    private int count2 = 2;
    private int count3 = 3;
    private int count4 = 4;
    private int count5 = 5;
    List<Map<String,Object>> dataRequests = new ArrayList<>();
    List<Map<String, Object>> dataEvents = new ArrayList<>();
    Context thisContext = this;
    String groupID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management_panel);
        requestsListView = (ListView)findViewById(R.id.listGroupsRequests);
        eventsListView = (ListView) findViewById(R.id.listGroupsEvents);
        createEvent = (Button) findViewById(R.id.button_createEvent);
        groupNameTextView = (TextView) findViewById(R.id.groupName);
        System.out.println("I'm here");
        groupID = getIntent().getStringExtra("groupID");
        String groupName = getIntent().getStringExtra("groupName");
        groupNameTextView.setText(groupName);
        createData(groupID);
    }


    public void createData(String groupID) {
        getEventsData(groupID);
        getRequestsData(groupID);
    }


    View.OnClickListener buttonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.button_createEvent:
                    intent = new Intent(GroupManagementPanelActivity.this, CreateEventActivity.class);
                    intent.putExtra("groupID", groupID);
                    break;
            }
            startActivity(intent);
        }
    };


    private void getEventsData(String groupID) {
        final List<String> eventIDs = new ArrayList<>();
        System.out.println("Finally in getEventsData");
        Query eventsnode = mFirebaseRef.child("groups").child(groupID).child("events");
        eventsnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    count1 -= 1;
                    count2 -= 2;
                    count3 -= 3;
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        eventIDs.add(eventSnapshot.getKey());
                        System.out.println(eventSnapshot.getKey());
                        count1 += 1;
                        System.out.println("count1: " + count1);
                    }
                    getEventsDetails(eventIDs);
                }
                else {
                    System.out.println("Is there events children?");
                    countEvents = 0;
                    System.out.println(countEvents);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read at usernode failed: " + firebaseError.getMessage());
            }
        });
    }


    private void getEventsDetails(List<String> eventIDs) {
        final Iterator<String> eventsIterator = eventIDs.iterator();
        while (eventsIterator.hasNext()) {
            final String eventID = eventsIterator.next();
            Query eventnode = mFirebaseRef.child("events").child(eventID);
            eventnode.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count2 += 1;
                    System.out.println("count2: " + count2);
                    Event event = dataSnapshot.getValue(Event.class);
                    String startDate = new SimpleDateFormat("dd/MM/yyyy").format(event.getEventStartDate());
                    String endDate = new SimpleDateFormat("dd/MM/yyyy").format(event.getEventEndDate());
                    final Map map = new HashMap();
                    map.put("eventName", event.getEventName());
                    map.put("eventDescription", event.getEventDescription());
                    map.put("fixedPoint", event.getPinID());
                    map.put("startDate", startDate);
                    map.put("endDate", endDate);
                    map.put("eventID", eventID);

                    getEventAddress(map, event.getPinID());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read at usergroupnode failed: " + firebaseError.getMessage());
                }
            });
        }
    }


    private void getEventAddress(final Map map, String pinID) {

        Query pinnode = mFirebaseRef.child("fixedpins").child(pinID).child("address");
        pinnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pinAddress = (String) dataSnapshot.getValue();
                map.put("fixedPoint", pinAddress);
                count3 += 1;
                System.out.println("count3: " + count3);
                dataEvents.add(map);
                if (count1 == count2 & count1 == count3){
                    System.out.println("Or do I get in here actually?");
                    countEvents=0;
                    System.out.println("coutEvents: " + countEvents);
                    if (countRequests == countEvents){
                        setAdapters();
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read at pinnode failed: " + firebaseError.getMessage());
            }
        });
    }


    private void getRequestsData(String groupID) {
        Query requestsnode = mFirebaseRef.child("groups").child(groupID).child("pendingJoinRequests");
        requestsnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    List<String> userIDs = new ArrayList<String>();
                    for (DataSnapshot userIDDataSnapshot : dataSnapshot.getChildren()) {
                        count4 += 1;
                        System.out.println("count4: " + count4);
                        String userID = userIDDataSnapshot.getKey();
                        userIDs.add(userID);
                    }
                    getUserInfo(userIDs);
                    count4 -= 4;
                }
                else{
                    System.out.println("Is there requests children?");
                    countRequests = 0;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void getUserInfo(List<String> uids){
        count5 -=5;
        for (String uid : uids) {
            Query usernode = mFirebaseRef.child("users").child(uid);
            usernode.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count5 += 1;
                    System.out.println("count5: " + count5);
                    System.out.println("Final count: " + count1 + " " + count2 + " " + count3 + " " + count4 + " " + count5);
                    final Map map = new HashMap();
                    map.put("firstName", dataSnapshot.child("firstName").getValue());
                    map.put("lastName", dataSnapshot.child("lastName").getValue());
                    map.put("emailAddress", dataSnapshot.child("emailAddress").getValue());
                    map.put("userID", dataSnapshot.getKey());
                    dataRequests.add(map);
                    if (count4 == count5){
                        System.out.println("When do I get in here?");
                        countRequests = 0;
                        System.out.println("countRequests: " + countRequests);
                        if (countRequests == countEvents){
                            setAdapters();
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

    }


    private void setAdapters(){
        System.out.println("When do I get in adapters?");


        adapterRequests = new SimpleAdapter(thisContext, (List<Map<String, Object>>) dataRequests,
                R.layout.listview_grouppanel_request_item, new String[]{"firstName", "lastName", "emailAddress"},
                new int[]{R.id.firstNameRequest, R.id.lastNameRequest, R.id.emailAddressRequest}) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                final ImageButton profileButton = (ImageButton) view.findViewById(R.id.profileButton);
                final ImageButton acceptButton = (ImageButton) view.findViewById(R.id.acceptButton);
                final ImageButton refuseButton = (ImageButton) view.findViewById(R.id.refuseButton);
                Map<String, String> user = ((Map<String, String>) getItem(position));

                final String userID = user.get("userID");
                profileButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(GroupManagementPanelActivity.this, ViewProfileActivity.class);
                        intent.putExtra("uid", userID);
                        startActivity(intent);
                    }
                });
                acceptButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        acceptRequest(userID);
                        Toast.makeText(getApplicationContext(), "The user has been added to the group" , Toast.LENGTH_SHORT).show();
                    }
                });
                refuseButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        refuseRequest(userID);
                        Toast.makeText(getApplicationContext(), "The user's request has been declined" , Toast.LENGTH_SHORT).show();
                    }
                });
                return view;
            }

        };

        adapterEvents = new SimpleAdapter(thisContext, (List<Map<String, Object>>) dataEvents,
                R.layout.listview_grouppanel_event_item, new String[]{"eventName", "eventDescription", "fixedPoint", "startDate", "endDate"},
                new int[]{R.id.eventName, R.id.eventDescription, R.id.eventFixedPointAddress, R.id.eventStartDate, R.id.eventEndDate});

        requestsListView.setAdapter(adapterRequests);
        eventsListView.setAdapter(adapterEvents);

        createEvent.setOnClickListener(buttonListener);
    }


    private void acceptRequest(String userID){
        Map<String, Object> acceptedRequest = new HashMap<String, Object>();
        acceptedRequest.put(groupID, true);
        Firebase usergroupnode = mFirebaseRef.child("users").child(userID).child("groupsJoined");
        usergroupnode.updateChildren(acceptedRequest);
        Map<String, Object> notRequestAnymore = new HashMap<>();
        notRequestAnymore.put(userID, null);
        Firebase groupnode = mFirebaseRef.child("groups").child(groupID).child("pendingJoinRequests");
        groupnode.updateChildren(notRequestAnymore);
        updateRequestsViewed(userID);

    }

    private void refuseRequest(String userID){
        Map<String, Object> refusedRequest = new HashMap<>();
        refusedRequest.put(userID, null);
        Firebase grouprequestnode = mFirebaseRef.child("groups").child(groupID).child("pendingJoinRequests");
        grouprequestnode.updateChildren(refusedRequest);
        updateRequestsViewed(userID);
    }

    private void updateRequestsViewed(String userID){
        Iterator requestIterator = dataRequests.iterator();
        while (requestIterator.hasNext()){
            Map request = (Map) requestIterator.next();
            if (request.containsValue(userID)){
                System.out.println(request.toString());
                requestIterator.remove();
                break;
            }
        }
        adapterRequests.notifyDataSetChanged();
    }
}
