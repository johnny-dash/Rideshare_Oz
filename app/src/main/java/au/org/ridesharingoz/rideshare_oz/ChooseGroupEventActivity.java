package au.org.ridesharingoz.rideshare_oz;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseExpandableListAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import au.org.ridesharingoz.rideshare_oz.R;

public class ChooseGroupEventActivity extends FirebaseAuthenticatedActivity {
    protected String noEvent = "Create a normal ride !";
    private ExpandableListView expandList;
    private InfoDetailsAdapter adapter;
    private Activity activity;
    private ArrayList<String> groupList;
    private ArrayList<ArrayList<String>> eventList;
    private ArrayList<ArrayList<String>> eventIDList;
    private ArrayList<String> eIDLists;
    private ArrayList<String> eNames;
    private int numberOfEvents;
    private ArrayList<Integer> endPoint;
    private int eCounter;
    private int endCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group_event);


        Firebase groupRef = new Firebase("https://flickering-inferno-6814.firebaseio.com/groups");
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        groupList = new ArrayList<String>();
                                                        eventIDList = new ArrayList<ArrayList<String>>();
                                                        eNames = new ArrayList<String>();
                                                        eventList = new ArrayList<ArrayList<String>>();

                                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                            Group group = postSnapshot.getValue(Group.class);
                                                            groupList.add(group.getGroupName());
                                                            // get HashMap < eventName, eventDate >
                                                            Map<String, Boolean> eventInfo = new HashMap<String, Boolean>();
                                                            final ArrayList<String> e = new ArrayList<String>();
                                                            if (postSnapshot.hasChild("events")) {

                                                                eventInfo = group.getEvents();
                                                                //e contains all eventIDs in one group


                                                                for (Map.Entry<String, Boolean> entry : eventInfo.entrySet()) {
                                                                    e.add(entry.getKey().toString());
                                                                }
                                                                eventIDList.add(e);
                                                            } else {
                                                                e.add("empty");
                                                                eventIDList.add(e);

                                                            }

                                                        }
                                                        endCounter = 0;
                                                        eCounter = 0;
                                                        endPoint = new ArrayList<Integer>();

                                                        //for each list in eventIDList, get eventNames
                                                        for (int i = 0; i < eventIDList.size(); i++) {
                                                            //get eventName for each event

                                                            eIDLists = eventIDList.get(i);

                                                            if (!eIDLists.get(0).equals("empty")) {
                                                                for (int j = 0; j < eIDLists.size(); j++) {
                                                                    numberOfEvents++;

                                                                    Firebase eventName = new Firebase("https://flickering-inferno-6814.firebaseio.com/events/" + eIDLists.get(j));
                                                                    eventName.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            eCounter += 1;
                                                                            Event event = dataSnapshot.getValue(Event.class);
                                                                            String eName = (String) event.getEventName();
                                                                            String eDate = (String) event.getEventDate();
                                                                            eNames.add("Event: " + eName + " | " + "Date:" + eDate);
                                                                            if (eCounter == endPoint.get(endCounter)) {
                                                                                eNames.add(noEvent);
                                                                                eventList.add(eNames);
                                                                                eNames = new ArrayList<String>();
                                                                                endCounter += 1;

                                                                            }
                                                                            if (eCounter == numberOfEvents) {
                                                                                adapter = new InfoDetailsAdapter(ChooseGroupEventActivity.this, groupList, eventList);
                                                                                expandList.setAdapter(adapter);
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(FirebaseError firebaseError) {
                                                                            System.out.println("The read failed: " + firebaseError.getMessage());
                                                                        }
                                                                    });
                                                                }
                                                            } else {
                                                                eNames.add(noEvent);
                                                                eventList.add(eNames);
                                                                eNames = new ArrayList<String>();
                                                            }
                                                            endPoint.add(numberOfEvents);
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(FirebaseError firebaseError) {
                                                        System.out.println("The read failed: " + firebaseError.getMessage());
                                                    }
                                                }

        );

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