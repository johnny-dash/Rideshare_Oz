package au.org.ridesharingoz.rideshare_ozTest;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.robotium.solo.Solo;

import au.org.ridesharingoz.rideshare_oz.LoginActivity;
import au.org.ridesharingoz.rideshare_oz.OneRideGoingtoActivity;
import au.org.ridesharingoz.rideshare_oz.R;

/**
 * Created by helen on 16/10/15.
 */
public class AcceptanceTest_2 extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private Activity currentActivity;
    private String email = "hailunt@gmail.com";
    private String password = "123";


    public AcceptanceTest_2() {
        super(LoginActivity.class);


    }

    @Override
    protected void setUp() throws Exception {

        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    /*

        This test will simulate a group owner accepts the join group request, creates an event and offers a ride for that event.

    */
    public void testAcceptance_2() throws Exception {

        // Email : hailunt@gmail.com Password:123


        //Unlock the lock screen.
        solo.unlockScreen();
        solo.enterText((EditText) solo.getView("email_address"), email);
        solo.enterText((EditText) solo.getView("password"), password);
        //Click on login button
        currentActivity = solo.getCurrentActivity();
        int id_login = currentActivity.getResources().getIdentifier("btn_login", "id", currentActivity.getPackageName());
        Button login_btn = (Button) solo.getView(id_login);
        solo.clickOnView(login_btn);
        solo.sleep(1000);
        currentActivity = solo.getCurrentActivity();
        // click menu item
        int id_groups = currentActivity.getResources().getIdentifier("my_groups", "id", currentActivity.getPackageName());
        solo.sleep(500);
        solo.clickOnView(solo.getView(id_groups));

        solo.sleep(500);
        currentActivity = solo.getCurrentActivity();
        solo.sleep(2000);
        int listGroupOwned = currentActivity.getResources().getIdentifier("listGroupsOwned", "id", currentActivity.getPackageName());
        ListView groupList = (ListView) solo.getView(listGroupOwned);
        ImageButton a = (ImageButton) groupList.getChildAt(0).findViewById(R.id.getDeatilsButton);
        solo.clickOnView(a);

        /*

            GroupManagementPanel

            Group owner will accept the request.

         */


        solo.sleep(2000);
        currentActivity = solo.getCurrentActivity();

        int joinGroupRequest = currentActivity.getResources().getIdentifier("listGroupsRequests", "id", currentActivity.getPackageName());
        ListView requestList = (ListView) solo.getView(joinGroupRequest);
        ImageButton p = (ImageButton) requestList.getChildAt(0).findViewById(R.id.profileButton);
        solo.sleep(500);
        solo.clickOnView(p);
        solo.goBack();
        ImageButton r = (ImageButton) requestList.getChildAt(0).findViewById(R.id.acceptButton);
        solo.clickOnView(r);


        /*

            ManageMyGroupsPanel

            Group owner will create an event.

         */

        currentActivity = solo.getCurrentActivity();
        int btn_createEvent = currentActivity.getResources().getIdentifier("button_createEvent", "id", currentActivity.getPackageName());
        solo.clickOnView(solo.getView(btn_createEvent));
        solo.waitForDialogToOpen(100);

        solo.enterText((EditText) solo.getView("createEventName"), "Halloween Party");
        solo.enterText((EditText) solo.getView("createEventDescription"), "Have fun with us!");

        int id_startDate = currentActivity.getResources().getIdentifier("createEventStartDate", "id", currentActivity.getPackageName());
        EditText startDate = (EditText) solo.getView(id_startDate);
        solo.clickOnView(startDate);
        solo.clickOnView(startDate);
        solo.waitForDialogToOpen(100);
        solo.setDatePicker(0, 2015, 10, 28);
        solo.clickOnText("Done");

        int id_endDate = currentActivity.getResources().getIdentifier("createEventEndDate", "id", currentActivity.getPackageName());
        EditText endDate = (EditText) solo.getView(id_endDate);
        solo.clickOnView(endDate);
        solo.clickOnView(endDate);
        solo.waitForDialogToOpen(100);
        solo.setDatePicker(0, 2015, 10, 28);
        solo.clickOnText("Done");

        int id_map = currentActivity.getResources().getIdentifier("createFixedPointAddressEvent", "id", currentActivity.getPackageName());
        solo.clickOnView(solo.getView(id_map));
        solo.sleep(500);

        // Go to map
        currentActivity = solo.getCurrentActivity();
        boolean initialID = solo.waitForFragmentById(R.id.map); //where id.satellite is defined in the R file, eventually would time out and throw error if fragment was not present
        solo.sleep(1000);
        Activity mapActivity = solo.getCurrentActivity();
        int map = mapActivity.getResources().getIdentifier("map", "id", currentActivity.getPackageName());
        View mapView =  solo.getView(map);
        solo.clickOnView(mapView);
        int mapSubmit = currentActivity.getResources().getIdentifier("submit", "id", currentActivity.getPackageName());
        Button btn_mapSubmit = (Button) solo.getView(mapSubmit);
        solo.clickOnView(btn_mapSubmit);

        //Back to event information
        solo.sleep(1000);
        currentActivity = solo.getCurrentActivity();
        int btn_submit = currentActivity.getResources().getIdentifier("btn_submit_event","id",currentActivity.getPackageName());
        solo.clickOnView(solo.getView(btn_submit));
        solo.sleep(1000);



        //Offers a ride for that event

        int id_action = currentActivity.getResources().getIdentifier("R.id.home", "id", currentActivity.getPackageName());
        solo.sleep(1000);

        currentActivity = solo.getCurrentActivity();
        int btn_offerARide = currentActivity.getResources().getIdentifier("button_offerride","id",currentActivity.getPackageName());
        solo.clickOnView(solo.getView(btn_offerARide));
        solo.sleep(900);
        currentActivity = solo.getCurrentActivity();
        int groupEventList = currentActivity.getResources().getIdentifier("choose_group_list", "id", currentActivity.getPackageName());
        ExpandableListView gList = (ExpandableListView) solo.getView(groupEventList);

        int normalRIde = currentActivity.getResources().getIdentifier("chooseGroupButton", "id", currentActivity.getPackageName());
//        View normal = gList.getChildAt(1).findViewById(R.id.chooseGroupButton);
        solo.sleep(900);
        solo.clickOnView(solo.getView(normalRIde));



        solo.sleep(500);
        // choose type
        currentActivity = solo.getCurrentActivity();
        int btn_GoingTo = currentActivity.getResources().getIdentifier("button_oneoff_goingto","id",currentActivity.getPackageName());
        solo.clickOnView(solo.getView(btn_GoingTo));

        solo.sleep(1000);

        // Go to map
//        currentActivity = solo.getCurrentActivity();
//        boolean initialID = solo.waitForFragmentById(R.id.map);
//        Activity mapActivity = solo.getCurrentActivity();
//        int map = mapActivity.getResources().getIdentifier("map", "id", currentActivity.getPackageName());
//        View mapView =  solo.getView(map);


/*
             Manually select pins
  */

        solo.sleep(6000);

//        int mapSubmit = currentActivity.getResources().getIdentifier("submit", "id", currentActivity.getPackageName());
//        Button btn_mapSubmit = (Button) solo.getView(mapSubmit);
//        solo.clickOnView(btn_mapSubmit);

        solo.waitForActivity("OneRideGoingtoActivity");

/*
            Ride details
 */
        solo.enterText((EditText) solo.getView("SeatNum"), "3");
        currentActivity = solo.getCurrentActivity();
        int id_Date = currentActivity.getResources().getIdentifier("Date", "id", currentActivity.getPackageName());
        EditText date = (EditText) solo.getView(id_Date);
        solo.clickOnView(date);
        solo.clickOnView(date);
        solo.waitForDialogToOpen(100);
        solo.setDatePicker(0, 2015, 10, 22);
        solo.clickOnText("Done");

        int id_ArrivalTime = currentActivity.getResources().getIdentifier("Arrival_Time", "id", currentActivity.getPackageName());
        EditText Arrival_Time = (EditText) solo.getView(id_ArrivalTime);
        solo.clickOnView(Arrival_Time);
        solo.clickOnView(Arrival_Time);
        solo.waitForDialogToOpen(100);
        solo.setTimePicker(0, 11, 30);
        solo.clickOnText("Done");

        int AddressList = currentActivity.getResources().getIdentifier("AddressList", "id", currentActivity.getPackageName());
        ListView addList = (ListView) solo.getView(AddressList);
        // set time for each address
        for(int i=0; i < addList.getAdapter().getCount(); i++){
            solo.sleep(300);
            Button time1 = (Button) addList.getChildAt(i).findViewById(R.id.AddTime);
            solo.clickOnView(time1);
            solo.waitForDialogToOpen(100);
            solo.setTimePicker(0, 11, (30+(10*(i+1))));
            solo.clickOnText("Done");
            solo.sleep(300);

        }

        int create = currentActivity.getResources().getIdentifier("Create_Ride", "id", currentActivity.getPackageName());
        Button btn_create = (Button) solo.getView(create);
        solo.clickOnView(btn_create);
        solo.sleep(500);




        solo.sleep(1000);
        solo.clickOnMenuItem("Logout", true);


    }



}
