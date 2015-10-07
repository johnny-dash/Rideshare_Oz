package au.org.ridesharingoz.rideshare_oz;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinGroupActivity extends FirebaseAuthenticatedActivity {


    private ListView mListView;
    private SimpleAdapter adapter;
    private int count1 = 1;
    private int count2 = 2;
    private int count3 = 3;
    List<Map<String,Object>> data;
    Context thisContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        mListView = (ListView)findViewById(R.id.listGroupToJoin);
        createData();
    }



    public void createData() {
        final List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Query groupnode = mFirebaseRef.child("groups");
        groupnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count1 -= 1;
                count2 -= 2;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map map = new HashMap();
                    Group group = postSnapshot.getValue(Group.class);
                    map.put("groupName", group.getGroupName());
                    map.put("groupDescription", group.getGroupDescription());
                    map.put("fixedPoint", group.getPinID());
                    map.put("groupID", postSnapshot.getKey());
                    map.put("privateGroup", group.getPrivateGroup());
                    list.add(map);
                    count1 += 1;
                    System.out.println("count1: " + count1);
                }

                final List<String> groupsAlreadyJoined = new ArrayList();
                final Iterator<Map<String, Object>> mapIterator = list.iterator();
                while (mapIterator.hasNext()) {
                    final Map<String, Object> map = mapIterator.next();
                    String user = mAuthData.getUid();
                    final String groupID = (String) map.get("groupID");
                    final String pinID =  (String) map.get("fixedPoint");

                    Query usergroupnode = mFirebaseRef.child("users").child(user).child("groupsJoined");
                    usergroupnode.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            count2 += 1;
                            System.out.println("count2: " + count2);
                            if (dataSnapshot.hasChild(groupID)) {
                                groupsAlreadyJoined.add(groupID);
                            }
                            Query pinnode = mFirebaseRef.child("fixedpins").child(pinID).child("address");
                            pinnode.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String pinAddress = (String) dataSnapshot.getValue();
                                    map.put("fixedPoint", pinAddress);
                                    count3 += 1;
                                    System.out.println("count3: " + count3);
                                    System.out.println("Final count: " + count1 + " " + count2 + " " + count3);

                                    if (count1 == count2 & count1 == count3) {
                                        System.out.println("When do I get in here?");
                                        data = list;
                                        adapter = new SimpleAdapter(thisContext, (List<Map<String, Object>>) data,
                                                R.layout.listview_joingroup_itemdetails, new String[]{"groupName", "groupDescription", "fixedPoint"},
                                                new int[]{R.id.groupToJoinName, R.id.groupToJoinDescription, R.id.groupToJoinFixedPointAddress}) {

                                            @Override
                                            public View getView(int position, View convertView, ViewGroup parent) {
                                                View v = super.getView(position, convertView, parent);

                                                final ImageButton b = (ImageButton) v.findViewById(R.id.addButton);
                                                Map<String, String> group = ((Map<String, String>) getItem(position));
                                                final String groupName = group.get("groupName");
                                                final String groupID = group.get("groupID");
                                                b.setOnClickListener(new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View arg0) {
                                                        if (groupsAlreadyJoined.contains(groupID)) {
                                                            Toast.makeText(getApplicationContext(), "You are already a member of the group: " + groupName, Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            if ((boolean)map.get("privateGroup")) {
                                                                sendJoinRequest(groupID);
                                                                Toast.makeText(getApplicationContext(), "A request to join has been sent to the group owner of: " + groupName, Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                joinGroup(groupID);
                                                                Toast.makeText(getApplicationContext(), "You have joined the group: " + groupName, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                });
                                                return v;
                                            }

                                        };
                                        mListView.setAdapter(adapter);

                                    }
                                }


                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    System.out.println("The read at pinnode failed: " + firebaseError.getMessage());
                                }
                            });

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read at usergroupnode failed: " + firebaseError.getMessage());
                        }
                    });

                }

                count3 -= 3;


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read at usernode failed: " + firebaseError.getMessage());
            }
        });

    }



    private void joinGroup(String groupID){
        String user = mAuthData.getUid();
        Map<String, Object> newJoinedGroup = new HashMap<String, Object>();
        newJoinedGroup.put(groupID, true);
        Firebase usergroupnode = mFirebaseRef.child("users").child(user).child("groupsJoined");
        usergroupnode.updateChildren(newJoinedGroup);
    }

    private void sendJoinRequest(String groupID){
        String user = mAuthData.getUid();
        Map<String, Object> joinRequest = new HashMap<>();
        joinRequest.put(user, true);
        Firebase grouprequestnode = mFirebaseRef.child("groups").child(groupID).child("pendingJoinRequests");
        grouprequestnode.updateChildren(joinRequest);
    }



}



