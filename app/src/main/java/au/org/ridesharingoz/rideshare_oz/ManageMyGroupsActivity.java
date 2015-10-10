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
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Ocunidee on 20/09/2015.
 */
public class ManageMyGroupsActivity extends FirebaseAuthenticatedActivity{

    private Button createagroup;
    private ListView oListView;
    private ListView jListView;
    private SimpleAdapter adapterOwned;
    private SimpleAdapter adapterJoined;
    private int count1 = 1;
    private int count2 = 2;
    private int count3 = 3;
    private int count4 = 4;
    List<Map<String,Object>> dataOwned = new ArrayList<>();
    List<Map<String, Object>> dataJoinedMinusOwned = new ArrayList<>();
    Context thisContext = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_groups);
        oListView = (ListView)findViewById(R.id.listGroupsOwned);
        jListView = (ListView) findViewById(R.id.listGroupsJoined);
        createagroup = (Button) findViewById(R.id.button_createagroup);
        System.out.println("I'm here");
        createData();
    }



    public void createData() {

        final List<String> groupIDs = new ArrayList<>();
        System.out.println("Finally in createData");
        Query usergroupnode = mFirebaseRef.child("users").child(mAuthData.getUid()).child("groupsJoined");
        usergroupnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count1 -= 1;
                count2 -= 2;
                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    groupIDs.add(groupSnapshot.getKey());
                    System.out.println(groupSnapshot.getKey());
                    count1 += 1;
                    System.out.println("count1: " + count1);
                }


                final Iterator<String> groupIterator = groupIDs.iterator();
                while (groupIterator.hasNext()) {
                    final String groupID = groupIterator.next();


                    Query groupnode = mFirebaseRef.child("groups").child(groupID);
                    groupnode.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            count2 += 1;
                            System.out.println("count2: " + count2);


                            final Map map = new HashMap();
                            map.put("groupName", (String) dataSnapshot.child("groupName").getValue());
                            System.out.println(map.toString());
                            map.put("groupDescription", (String) dataSnapshot.child("groupDescription").getValue());
                            String pinID = (String) dataSnapshot.child("pinID").getValue();
                            map.put("fixedPoint", pinID);
                            map.put("groupID", groupID);
                            dataSnapshot.child("privateGroup").getValue().getClass();
                            if ((Boolean) dataSnapshot.child("privateGroup").getValue()) {
                                map.put("privateGroup", "Private");
                            }
                            else map.put("privateGroup", "Public");


                            Query pinnode = mFirebaseRef.child("fixedpins").child(pinID).child("address");
                            pinnode.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String pinAddress = (String) dataSnapshot.getValue();
                                    map.put("fixedPoint", pinAddress);
                                    count3 += 1;
                                    System.out.println("count3: " + count3);


                                    Query groupownednode = mFirebaseRef.child("users").child(mAuthData.getUid()).child("groupsOwned");
                                    groupownednode.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            count4 += 1;
                                            System.out.println("count4: " + count4);
                                            System.out.println("Final count: " + count1 + " " + count2 + " " + count3 + " " + count4);
                                            if (dataSnapshot.hasChild(groupID)) {
                                                System.out.println(map.toString());
                                                dataOwned.add(map);
                                            } else dataJoinedMinusOwned.add(map);


                                            if (count1 == count2 & count1 == count3 & count1 == count4) {
                                                System.out.println("When do I get in here?");


                                                adapterOwned = new SimpleAdapter(thisContext, (List<Map<String, Object>>) dataOwned,
                                                        R.layout.listview_viewgroupsowned_item, new String[]{"groupName", "groupDescription", "fixedPoint", "privateGroup"},
                                                        new int[]{R.id.groupOwnedName, R.id.groupOwnedDescription, R.id.groupOwnedFixedPointAddress, R.id.groupOwnedIsPrivate}) {

                                                    @Override
                                                    public View getView(int position, View convertView, ViewGroup parent) {
                                                        View v = super.getView(position, convertView, parent);

                                                        final ImageButton b = (ImageButton) v.findViewById(R.id.getDeatilsButton);
                                                        Map<String, String> group = ((Map<String, String>) getItem(position));
                                                        final String groupID = group.get("groupID");
                                                        final String groupName = group.get("groupName");
                                                        b.setOnClickListener(new View.OnClickListener() {

                                                            @Override
                                                            public void onClick(View arg0) {
                                                                Intent intent = new Intent(ManageMyGroupsActivity.this, GroupManagementPanelActivity.class);
                                                                intent.putExtra("groupID", groupID);
                                                                intent.putExtra("groupName",groupName);
                                                                startActivity(intent);
                                                            }
                                                        });
                                                        return v;
                                                    }

                                                };

                                                adapterJoined = new SimpleAdapter(thisContext, (List<Map<String, Object>>) dataJoinedMinusOwned,
                                                        R.layout.listview_viewgroupsjoined_item, new String[]{"groupName", "groupDescription", "fixedPoint", "privateGroup"},
                                                        new int[]{R.id.groupJoinedName, R.id.groupJoinedDescription, R.id.groupJoinedFixedPointAddress, R.id.groupJoinedIsPrivate});

                                                oListView.setAdapter(adapterOwned);
                                                jListView.setAdapter(adapterJoined);

                                                createagroup.setOnClickListener(buttonListener);


                                            }
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });

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
                count4 -=4;
                count3 -= 3;


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read at usernode failed: " + firebaseError.getMessage());
            }
        });

    }






    View.OnClickListener buttonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.button_createagroup:
                    intent = new Intent(ManageMyGroupsActivity.this, CreateAGroupActivity.class);
                    break;
            }
            startActivity(intent);
        }

    };


}
