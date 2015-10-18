package au.org.ridesharingoz.rideshare_ozTest;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.robotium.solo.Solo;

import au.org.ridesharingoz.rideshare_oz.LoginActivity;
import au.org.ridesharingoz.rideshare_oz.R;

/**
 * Created by helen on 17/10/15.
 */
public class AcceptanceTest_3 extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private Activity currentActivity;
    private String email = "ocunidee@gmail.com";
    private String password = "123";


    public AcceptanceTest_3() {
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

        This test will simulate a new user register, fill in personal information and then request to join a group

    */
    public void testAcceptance_3() throws Exception {

        // Email : ocunidee@gmail.com  Password:123


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
        View mapView = solo.getView(map);
        solo.clickOnView(mapView);
        int mapSubmit = currentActivity.getResources().getIdentifier("submit", "id", currentActivity.getPackageName());
        Button btn_mapSubmit = (Button) solo.getView(mapSubmit);
        solo.clickOnView(btn_mapSubmit);

        //Back to event information
        solo.sleep(1000);
        currentActivity = solo.getCurrentActivity();
        int btn_submit = currentActivity.getResources().getIdentifier("btn_submit_event", "id", currentActivity.getPackageName());
        solo.clickOnView(solo.getView(btn_submit));
        solo.sleep(1000);


        solo.clickOnMenuItem("Logout", true);


    }
}
