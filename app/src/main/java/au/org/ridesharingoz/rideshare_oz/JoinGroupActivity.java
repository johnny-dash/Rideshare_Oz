package au.org.ridesharingoz.rideshare_oz;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JoinGroupActivity extends FirebaseAuthenticatedActivity {

    ArrayList<Item> groupItemList = new ArrayList<>();
    ExpandableListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        createData();
        listView = (ExpandableListView) findViewById(R.id.joingroup_list);
        final MyExpandableListAdapter myExpandableListAdapter = new MyExpandableListAdapter(this, groupItemList);
        listView.setAdapter(myExpandableListAdapter);


    }


    public void createData() {
        Query usernode = mFirebaseRef.child("groups");
        usernode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupItemList = new ArrayList<>();
                List<Group> groupList = new ArrayList<Group>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Group group = postSnapshot.getValue(Group.class);
                    groupList.add(group);  //create the item and itemDetails here
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

}


    class MyExpandableListAdapter extends BaseExpandableListAdapter {

        private final ArrayList<Item> items;
        public LayoutInflater inflater;
        public Activity activity;

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
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String itemDetails = (String) getChild(groupPosition, childPosition);
            TextView text = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listview_joingroup_itemdetails, null);
                text = (TextView) convertView.findViewById(R.id.groupToJoinName);
                text.setText(itemDetails);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, itemDetails,
                                Toast.LENGTH_SHORT).show();
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
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listview_joingroup_item, null);
            }
            Item item = (Item) getGroup(groupPosition);
            ((CheckedTextView) convertView).setText(item.itemName);
            ((CheckedTextView) convertView).setChecked(isExpanded);
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
            public final List<String> itemDetails = new ArrayList<String>();

            public Item(String itemName) {
                this.itemName = itemName;
            }

        }