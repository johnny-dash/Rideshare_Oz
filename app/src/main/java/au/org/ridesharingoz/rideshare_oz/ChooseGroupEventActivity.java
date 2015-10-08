package au.org.ridesharingoz.rideshare_oz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class ChooseGroupEventActivity extends FirebaseAuthenticatedActivity {
    protected String noEvent = "Create a normal ride !";
    private ExpandableListView expandList;
    private InfoDetailsAdapter adapter;
    private Activity activity;
    private Map<String, Boolean> groupIDmap;
    private ArrayList<String> groupIDList;
    private ArrayList<String> groupList;
    private Map<String, Boolean> eventIDmap;
    private ArrayList<ArrayList<String>> eventList;
    private ArrayList<Map<String, Boolean>> eventIDList = new ArrayList<Map<String, Boolean>>();
    private ArrayList<String> eNames;
    private int numberOfEvents;
    private ArrayList<Integer> endPoint;
    private int eCounter;
    private int endCounter;
    private int count1 = 1;
    private int count2 = 2;
    private int count3 = 3;



    private void createData() {

        Query usernode = mFirebaseRef.child("users").child("f241c0fa-7f37-404c-8a58-4ad6a8f555cd");
        usernode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count1 -= 1;
                count2 -= 2;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // get groupID List
                    groupIDmap = (Map<String, Boolean>) dataSnapshot.child("groupsJoined").getValue();

                    System.out.println(groupIDmap);
                    count1 += 1;
                    System.out.println("count1: " + count1);
                }

                groupIDList = new ArrayList<String>();
                for (Map.Entry<String, Boolean> entry : groupIDmap.entrySet()) {
                    groupIDList.add(entry.getKey().toString());
                    System.out.println(groupIDList);

                    groupList = new ArrayList<String>();

                    Query groupnode = mFirebaseRef.child("groups").child(entry.getKey());
                    groupnode.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            count2 += 1;
                            System.out.println("count2: " + count2);

                            groupList.add((String) dataSnapshot.child("groupName").getValue());
                            System.out.println(groupList);
                            eventIDmap = (Map<String, Boolean>) dataSnapshot.child("events").getValue();
                            eventIDList.add(eventIDmap);
                            eventList = new ArrayList<ArrayList<String>>();

                            eNames = new ArrayList<String>();
                            eCounter = 0;
                            for (Map.Entry<String, Boolean> entry : eventIDmap.entrySet()) {


                                Query pinnode = mFirebaseRef.child("events").child(entry.getKey());
                                pinnode.addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        eNames.add(dataSnapshot.child("eventName").getValue() + " | " + dataSnapshot.child("eventDate").getValue());
                                        count3 += 1;

                                        System.out.println("count3: " + count3);
                                        System.out.println("Final count: " + count1 + " " + count2 + " " + count3);

                                        System.out.println(eventIDmap);
                                        if (eNames.size() == eventIDList.get(eCounter).size()) {
                                            eNames.add(noEvent);
                                            eventList.add(eNames);
                                            eNames = new ArrayList<String>();
                                            eCounter += 1;
                                            System.out.println("eventList id : " + eventList);
                                        }


                                        if (count2 == count3) {


                                            adapter = new InfoDetailsAdapter(ChooseGroupEventActivity.this, groupList, eventList);
                                            expandList.setAdapter(adapter);

                                        }
                                    }


                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                        System.out.println("The read at pinnode failed: " + firebaseError.getMessage());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read at usergroupnode failed: " + firebaseError.getMessage());
                        }
                    });

                    System.out.println(eventList);
                }
                count3 -= 3;
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read at usernode failed: " + firebaseError.getMessage());
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group_event);
        final String from = getIntent().getStringExtra("from");
        createData();
        activity = this;
        expandList = (ExpandableListView)

                findViewById(R.id.el_list);


        expandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener()

                                           {
                                               /**
                                                * child click method
                                                */
                                               @SuppressWarnings("unchecked")
                                               @Override
                                               public boolean onChildClick(
                                                       ExpandableListView parent,
                                                       View v,
                                                       int groupPosition,
                                                       int childPosition,
                                                       long id) {
                                                   // get group data
                                                   Object Group = adapter.getGroup(groupPosition);

                                                   // get event data
                                                   String childMap = (String) adapter.getChild(
                                                           groupPosition,
                                                           childPosition);

                                                   if (childMap.toString().equals(noEvent)) {
                                                       // Create a normal ride ---> Choose type
                                                       Intent GroupAndEvent1 = new Intent(ChooseGroupEventActivity.this, ChooseTypeRideActivity.class);
                                                       Bundle bundle1 = new Bundle();
                                                       bundle1.putString("Group", adapter.getGroup(groupPosition).toString());
                                                       bundle1.putString("Event", childMap.toString());
                                                       bundle1.putString("from", from);
                                                       GroupAndEvent1.putExtras(bundle1);
                                                       startActivity(GroupAndEvent1);
                                                   } else {
                                                       int startIndex = childMap.indexOf(" ");
                                                       int endIndex = childMap.indexOf(" |");
                                                       String eventName = childMap.substring(startIndex + 1, endIndex);
                                                       System.out.print(eventName);
                                                       //Choose an event ---> Directly go to map, choose pick-up points
                                                       Intent GroupAndEvent2 = new Intent(ChooseGroupEventActivity.this, MapsActivity.class);
                                                       Bundle bundle2 = new Bundle();
                                                       bundle2.putString("Group", adapter.getGroup(groupPosition).toString());
                                                       bundle2.putString("Event", eventName);
                                                       bundle2.putString("from", from);
                                                       GroupAndEvent2.putExtras(bundle2);
                                                       startActivity(GroupAndEvent2);
                                                   }

                                                   // output log
                                                   Log.d("SampleActivity", "Group: " + Group);
                                                   Log.d("SampleActivity", "Event: " + childMap);

                                                   return false;
                                               }

                                           }

        );


    }

    public class InfoDetailsAdapter extends BaseExpandableListAdapter {


        Activity activity;
        List<String> groupList;
        ArrayList<ArrayList<String>> eventList;

        public InfoDetailsAdapter(Activity a, List<String> groupList,
                                  ArrayList<ArrayList<String>> eventList) {
            activity = a;
            this.groupList = groupList;
            this.eventList = eventList;
        }

        //getTextView
        TextView getTextView() {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 100);
            TextView textView = new TextView(ChooseGroupEventActivity.this);
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setPadding(36, 0, 0, 0);
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            return textView;
        }


        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return eventList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return eventList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            LinearLayout ll = new LinearLayout(ChooseGroupEventActivity.this);
            ll.setOrientation(LinearLayout.VERTICAL);
//             ImageView logo = new ImageView(ExpandableList.this);
//             logo.setImageResource(logos[groupPosition]);
//             logo.setPadding(50, 0, 0, 0);
//             ll.addView(logo);
            TextView textView = getTextView();
            textView.setTextColor(Color.BLUE);
            textView.setText(getGroup(groupPosition).toString());
            ll.addView(textView);
            ll.setPadding(100, 10, 10, 10);
            return ll;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            LinearLayout ll = new LinearLayout(ChooseGroupEventActivity.this);
            ll.setOrientation(LinearLayout.VERTICAL);
//             ImageView generallogo = new ImageView(TestExpandableListView.this);
//             generallogo.setImageResource(generallogos[groupPosition][childPosition]);
//             ll.addView(generallogo);
            TextView textView = getTextView();
            textView.setText(getChild(groupPosition, childPosition).toString());
            ll.addView(textView);
            return ll;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }
}