package au.org.ridesharingoz.rideshare_oz;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        mListView = (ListView)findViewById(R.id.listGroupToJoin);
        List<Map<String,String>> data = createData();
        System.out.println(data.toString());
        SimpleAdapter adapter = new SimpleAdapter(this, (List<Map<String, String>>) data,
                R.layout.listview_joingroup_itemdetails, new String[] { "groupName", "groupDescription", "fixedPoint" },
                new int[] { R.id.groupToJoinName, R.id.groupToJoinDescription, R.id.groupToJoinFixedPointAddress }); //{

            /*@Override
            public View getView (int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);

                ImageButton b=(ImageButton)v.findViewById(R.id.addButton);
                Map<String, String> group = ((Map<String, String>) getItem(position));
                final String groupName = group.get("groupName");
                final String groupID = group.get("groupID");
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        joinGroup(groupID);
                        Toast.makeText(getApplicationContext(),"You have joined the group: " + groupName,Toast.LENGTH_SHORT).show();
                    }
                });
                return v;
            }*/

        //};



        mListView.setAdapter(adapter);

    }



    public List<Map<String,String>> createData() {
        final List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Query usernode = mFirebaseRef.child("groups");
        usernode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map map = new HashMap();
                    Group group = postSnapshot.getValue(Group.class);
                    map.put("groupName", group.getGroupName());
                    map.put("groupDescription", group.getGroupDescription());
                    map.put("fixedPoint", group.getPinID());
                    //map.put("groupID", postSnapshot.getKey());
                    list.add(map);
                    System.out.println(group.getGroupName());

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read at usernode failed: " + firebaseError.getMessage());
            }
        });


       final Iterator<Map<String, String>> mapIterator = list.iterator();
       while (mapIterator.hasNext()) {
            final Map<String, String> map = mapIterator.next();
            String user = mAuthData.getUid();
            final String groupID = map.get("groupID");

            Query usergroupnode = mFirebaseRef.child("users").child(user).child("groupsJoined");
            usergroupnode.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(groupID)) {
                        mapIterator.remove();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read at usergroupnode failed: " + firebaseError.getMessage());
                }
            });

            String pinID = (String) map.get("fixedPoint");
            Query pinnode = mFirebaseRef.child("pins").child(pinID).child("address");
            pinnode.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String pinAddress = (String) dataSnapshot.getValue();
                    map.put("fixedPoint", pinAddress);
                    System.out.println(pinAddress);
                }


                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read at pinnode failed: " + firebaseError.getMessage());
                }
            });

        }
        System.out.println(list.toString());
        return list;
    }



    private void joinGroup(String groupID){
        String user = mAuthData.getUid();
        Map<String, Object> newJoinedGroup = new HashMap<String, Object>();
        newJoinedGroup.put(groupID, true);
        Firebase usergroupnode = mFirebaseRef.child("users").child(user).child("groupsJoined");
        usergroupnode.updateChildren(newJoinedGroup);
    }



}



