package au.org.ridesharingoz.rideshare_oz.groupsPackage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.BaseExpandableListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.drawable.Drawable;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import au.org.ridesharingoz.rideshare_oz.ridesPackage.ChooseTypeRideActivity;
import au.org.ridesharingoz.rideshare_oz.R;
import au.org.ridesharingoz.rideshare_oz.dataPackage.Event;
import au.org.ridesharingoz.rideshare_oz.firebasePackage.FirebaseAuthenticatedActivity;


public class ChooseGroupEventActivity extends FirebaseAuthenticatedActivity {
    private ExpandableListView expandList;
    private Button chooseThisButton;
    private Activity activity;
    private List<String> groupIDs;
    private ArrayList<Map<String, Boolean>> eventIDList = new ArrayList<Map<String, Boolean>>();
    private int count1 = 1;
    private int count2 = 2;
    private int count3;
    private int countDataTranslation = 0;
    private int count4 = 0;
    private Map<String,Map> preAdapterData;
    private Map<String,Object> groupEventsData;
    private ExpandableListView listView;
    private Activity thisActivity = this;
    private MyExpandableListAdapter adapter;
    ArrayList<Item> adapterData;
    private Map<String, String> groupsToPin;
    private Map<String, String> eventsToPin;
    private Map<String, String> groupNameToGroupID;
    private Map<String, String> eventNameToEventID;
    private Boolean previousActivityActionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group_event);
        previousActivityActionType = getIntent().getBooleanExtra("isCreatingRide", false);
        System.out.println("actiontype is: " + previousActivityActionType);
        createData();
        listView = (ExpandableListView) findViewById(R.id.choose_group_list);
        preAdapterData = new HashMap<>();
        groupEventsData = new HashMap<>();
        adapterData = new ArrayList<>();
        groupsToPin = new HashMap<>();
        eventsToPin = new HashMap<>();
        groupNameToGroupID = new HashMap<>();
        eventNameToEventID = new HashMap<>();
    }



    private void createData() {
        Query usernode = mFirebaseRef.child("users").child(mAuthData.getUid()).child("groupsJoined");
        groupIDs = new ArrayList<>();
        usernode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count1 -= 1;
                count2 -= 2;
                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    // get groupID List
                    groupIDs.add(groupSnapshot.getKey());
                    System.out.println(groupIDs);
                    count1 += 1;
                    System.out.println("count1: " + count1);
                }
                getGroupInfo(groupIDs);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read at usernode failed: " + firebaseError.getMessage());
            }
        });

    }

    private void getGroupInfo(List<String> groupIDs) {

        for (final String groupID : groupIDs) {
            final Map<String, Object> map = new HashMap();
            Query groupnode = mFirebaseRef.child("groups").child(groupID);
            groupnode.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count2 += 1;
                    System.out.println("count2: " + count2);
                    String groupName = (String) dataSnapshot.child("groupName").getValue();
                    String groupPin = (String) dataSnapshot.child("pinID").getValue();
                    groupsToPin.put(groupID, groupPin);
                    groupNameToGroupID.put(groupName, groupID);
                    map.put("groupName", groupName);
                    map.put("isPrivate", (Boolean) dataSnapshot.child("privateGroup").getValue());
                    if (dataSnapshot.child("events").hasChildren()) {
                        System.out.println(groupName+ " " + groupID + " has events");
                        List<String> eventIDs = new ArrayList<>();
                        int countEvents = 0;
                        for (DataSnapshot eventDataSnapShot : dataSnapshot.child("events").getChildren()) {
                            countEvents += 1;
                            System.out.println("countEvents: " + countEvents);
                            String eventID = eventDataSnapShot.getKey();
                            eventIDs.add(eventID);
                        }
                        System.out.println("count2 nb " + count2 +" has " + countEvents);
                        map.put("groupEvents", eventIDs);
                        System.out.println(eventIDs.toString());
                    }
                    else {
                        System.out.println(groupName + " " + groupID + " doesn't have an event");

                    }
                    groupEventsData.put(groupID, map);
                    if (count1 == count2) {
                        getEventsInfo();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read at groupnode failed: " + firebaseError.getMessage());
                }
            });
        }
    }

    private void getEventsInfo(){
        System.out.println("When do I get here?");
        for (final String groupID : groupEventsData.keySet()) {
            final Map<String, Object> group = (HashMap) groupEventsData.get(groupID);
            if (group.containsKey("groupEvents")) {
                final ArrayList<String> groupEvents = (ArrayList) group.get("groupEvents");
                System.out.println("Number of events: " + count3);
                System.out.println(groupEvents.toString());
                final Map<String, String> eventsDetails = new HashMap<>();
                for (final String eventID : groupEvents) {
                    Query eventnode = mFirebaseRef.child("events").child(eventID);
                    eventnode.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            count4 += 1;
                            count3 = groupEvents.size();
                            System.out.println("count4: " + count4);
                            Event event = dataSnapshot.getValue(Event.class);
                            String startDate = new SimpleDateFormat("dd/MM/yyyy").format(event.getEventStartDate());
                            String endDate = new SimpleDateFormat("dd/MM/yyyy").format(event.getEventEndDate());
                            String eventPin = event.getPinID();
                            eventsToPin.put(eventID, eventPin);
                            eventNameToEventID.put(event.getEventName(), eventID);
                            eventsDetails.put(eventID, event.getEventName() + "\n" + startDate + " - " + endDate);
                            if (count3 == count4) {
                                System.out.println("Final count: " + count3 + " " + count4);
                                count4 = 0;
                                group.put("groupEvents", eventsDetails);
                                preAdapterData.put(groupID, group);
                                makeAdapterData();
                                adapter = new MyExpandableListAdapter(thisActivity, adapterData);
                                adapter.notifyDataSetChanged();
                                listView.setAdapter(adapter);

                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read at eventnode failed: " + firebaseError.getMessage());
                        }
                    });
                }
            }
            else{
                preAdapterData.put(groupID, group);
                makeAdapterData();
                adapter = new MyExpandableListAdapter(thisActivity, adapterData);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

            }

        }
    }

    public void makeAdapterData(){
        System.out.println("This is when data is translated");
        ArrayList<Item> items = new ArrayList<>();
        for(String groupID : preAdapterData.keySet()){
            countDataTranslation += 1;
            Item item = new Item((String) ((HashMap) preAdapterData.get(groupID)).get("groupName"), (Boolean) ((HashMap)preAdapterData.get(groupID)).get("isPrivate"));
            if (((HashMap)preAdapterData.get(groupID)).containsKey("groupEvents")) {
                Map<String,String> details = ((HashMap<String,String>) ((HashMap)preAdapterData.get(groupID)).get("groupEvents"));
                for (String key : details.keySet()) {
                    item.itemDetails.add(details.get(key));
                }
            }
            items.add(item);
        }
        adapterData = items;

    }





    class MyExpandableListAdapter extends BaseExpandableListAdapter {

        private final ArrayList<Item> items;
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

        public MyExpandableListAdapter(Activity act, ArrayList<Item> items) {
            activity = act;
            this.items = items;
            inflater = act.getLayoutInflater();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).itemDetails.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String itemDetails = (String) getChild(groupPosition, childPosition);
            TextView text = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.expandablelistview_choosegroupevent_itemdetails, null);
                text = (TextView) convertView.findViewById(R.id.eventToChooseDetails);
                text.setText(itemDetails);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String eventDetails = (String) adapter.getChild(groupPosition, childPosition);
                        int endIndex = eventDetails.indexOf("\n");
                        String eventName = eventDetails.substring(0, endIndex);
                        System.out.println(eventName);
                        String eventID = eventNameToEventID.get(eventName);
                        String eventPin = eventsToPin.get(eventID);
                        System.out.println(eventID + " " + eventPin);
                        Intent eventIntent = new Intent(ChooseGroupEventActivity.this, ChooseTypeRideActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putBoolean("isEvent", true);
                        bundle2.putString("Pin", eventPin);
                        bundle2.putString("ID", eventID);
                        bundle2.putBoolean("isCreatingRide", previousActivityActionType);
                        eventIntent.putExtras(bundle2);
                        startActivity(eventIntent);
                    }
                });
            }
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return items.get(groupPosition).itemDetails.size();
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
            TextView text = null;
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.expandablelistview_choosegroupevent_item, null);
                holder.groupButton = (Button) convertView.findViewById(R.id.chooseGroupButton);
                Item item = (Item) getGroup(groupPosition);
                if (!item.getIsPrivate()){
                    holder.groupButton.setVisibility(View.INVISIBLE);
                }
                holder.groupButton.setFocusable(false);
                text = (TextView) convertView.findViewById(R.id.groupToChooseName);
                text.setText(item.itemName);
                ((CheckedTextView) text).setChecked(isExpanded);
                View ind = convertView.findViewById( R.id.explist_indicator);
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
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.groupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    String groupName = ((Item)adapter.getGroup(groupPosition)).getItemName();
                    System.out.println(groupName);
                    String groupID = groupNameToGroupID.get(groupName);
                    String groupPin = groupsToPin.get(groupID);
                    System.out.println(groupID + " " + groupPin);
                    Intent eventIntent = new Intent(ChooseGroupEventActivity.this, ChooseTypeRideActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isEvent", false);
                    bundle.putString("Pin", groupPin);
                    bundle.putString("ID", groupID);
                    bundle.putBoolean("isCreatingRide", previousActivityActionType);
                    eventIntent.putExtras(bundle);
                    startActivity(eventIntent);
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


    class Item {

        public String itemName;
        public Boolean isPrivate;
        public final List<String> itemDetails = new ArrayList<String>();

        public Item(String itemName, Boolean isPrivate) {
            this.itemName = itemName;
            this.isPrivate = isPrivate;
        }

        public String getItemName() {
            return itemName;
        }

        public Boolean getIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(Boolean isPrivate) {
            this.isPrivate = isPrivate;
        }
    }



    static class ViewHolder {
        protected Button groupButton;
    }
}