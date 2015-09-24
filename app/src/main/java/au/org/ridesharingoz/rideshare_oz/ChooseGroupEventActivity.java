package au.org.ridesharingoz.rideshare_oz;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import au.org.ridesharingoz.rideshare_oz.R;

public class ChooseGroupEventActivity extends FirebaseAuthenticatedActivity {
    protected String noEvent = "Create a private ride !";

    ExpandableListView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group_event);

        mView = (ExpandableListView) findViewById(R.id.el_list);
        final MyAdapter myAdapter = new MyAdapter();
        mView.setAdapter(myAdapter);
        mView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
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
                Object Group = myAdapter.getGroup(groupPosition);

                // get event data
                String childMap = (String) myAdapter.getChild(
                        groupPosition,
                        childPosition);

                if (childMap.toString().equals(noEvent)) {
                    // Create a normal ride ---> Choose type
                    Intent GroupAndEvent1 = new Intent(ChooseGroupEventActivity.this, ChooseTypeRideActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("Group", myAdapter.getGroup(groupPosition).toString());
                    bundle1.putString("Event", childMap.toString());
                    GroupAndEvent1.putExtras(bundle1);
                    startActivity(GroupAndEvent1);
                } else {

                    //Choose an event ---> Directly go to map, choose pick-up points
                    Intent GroupAndEvent2 = new Intent(ChooseGroupEventActivity.this, MapsActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("Group", myAdapter.getGroup(groupPosition).toString());
                    bundle2.putString("Event", childMap.toString());
                    GroupAndEvent2.putExtras(bundle2);
                    startActivity(GroupAndEvent2);
                }

                // output log
                Log.d("SampleActivity", "Group: " + Group);
                Log.d("SampleActivity", "Event: " + childMap);

                return false;
            }

        });

    }

    class MyAdapter extends BaseExpandableListAdapter {
        /*


                     SHOULE GET DATA FROM FIREBASE

                     IF THERE IS NO EVENT In ONE GROUP, CHOOSE " Create a private ride !"

                     SEND GROUP NAME( STRING )  AND EVENT NAME ( STRING ) TO NEXT ACTIVITY

                     NEXT ACTIVITY SHOULD DECIDE THE FIX POINT ACCORDING TO THESE



         */

        //set groups ***TEST DATA***
        private String[] generalsTypes = new String[]{"The University of Melbourne", "Group 2", "Group 3", "Group 4", "Group 5"};
        //set events ** TEST DATA ***
        private String[][] generals = new String[][]{
                {"Event 1", "Event 2", noEvent},
                {"Event 1", "Event 2", "Event 3", noEvent},
                {"Event 1", noEvent},
                {noEvent},
                {"Event1", "Event2", noEvent}


        };


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
            return generalsTypes.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return generals[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return generalsTypes[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return generals[groupPosition][childPosition];
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