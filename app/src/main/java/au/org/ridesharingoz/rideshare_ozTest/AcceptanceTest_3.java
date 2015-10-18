package au.org.ridesharingoz.rideshare_ozTest;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
    private String email = "helen@gmail.com";
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

        This test will simulate a user in a group wants to search a ride and request to join a ride.

    */
    public void testAcceptance_3() throws Exception {

        // Email : helen@gmail.com Password:123


        //Unlock the lock screen.
        solo.unlockScreen();
        solo.enterText((EditText) solo.getView("email_address"), email);
        solo.sleep(500);
        solo.enterText((EditText) solo.getView("password"), password);
        solo.sleep(500);
        //Click on login button
        currentActivity = solo.getCurrentActivity();
        int id_login = currentActivity.getResources().getIdentifier("btn_login", "id", currentActivity.getPackageName());
        Button login_btn = (Button) solo.getView(id_login);
        solo.clickOnView(login_btn);
        solo.sleep(1000);

        currentActivity = solo.getCurrentActivity();
        int id_search = currentActivity.getResources().getIdentifier("button_searchride", "id", currentActivity.getPackageName());
        solo.clickOnView(solo.getView(id_search));
        solo.sleep(1000);

        currentActivity = solo.getCurrentActivity();
        int normalRIde = currentActivity.getResources().getIdentifier("chooseGroupButton", "id", currentActivity.getPackageName());
        solo.clickOnView(solo.getView(normalRIde));

        solo.sleep(500);
        // choose type
        currentActivity = solo.getCurrentActivity();
        int btn_GoingTo = currentActivity.getResources().getIdentifier("button_oneoff_goingto","id",currentActivity.getPackageName());
        solo.clickOnView(solo.getView(btn_GoingTo));
        solo.sleep(500);
        currentActivity = solo.getCurrentActivity();
        int id_searchDate = currentActivity.getResources().getIdentifier("search_date", "id", currentActivity.getPackageName());
        EditText searchDate = (EditText) solo.getView(id_searchDate);
        solo.sleep(300);
        solo.clickOnView(searchDate);
//        solo.sleep(300);
//        solo.clickOnView(searchDate);
//        solo.sleep(300);
//        solo.clickOnView(searchDate);
        solo.waitForDialogToOpen(100);
        solo.setDatePicker(0, 2015, 10, 27);
        solo.clickOnText("Done");
        solo.sleep(800);

        int id_searchTime = currentActivity.getResources().getIdentifier("search_time", "id", currentActivity.getPackageName());
        EditText searchTime = (EditText) solo.getView(id_searchTime);
        solo.clickOnView(searchTime);
        solo.clickOnView(searchTime);
        solo.waitForDialogToOpen(100);
        solo.setTimePicker(0, 11, 20);
        solo.clickOnText("Done");

        solo.sleep(800);

        int id_gotoMap = currentActivity.getResources().getIdentifier("btn_gotomap","id", currentActivity.getPackageName());
        solo.clickOnView(solo.getView(id_gotoMap));
        solo.sleep(6000);

        solo.waitForActivity("SearchGoingtoRideActivity");
        currentActivity = solo.getCurrentActivity();
        int id_searchSubmit = currentActivity.getResources().getIdentifier("btn_search","id", currentActivity.getPackageName());
        solo.clickOnView(solo.getView(id_searchSubmit));

        solo.sleep(1000);
        currentActivity = solo.getCurrentActivity();
        int id_searchResults = currentActivity.getResources().getIdentifier("SearchedResult","id", currentActivity.getPackageName());
        ListView requestList = (ListView) solo.getView(id_searchResults);
        Button profile = (Button) requestList.getChildAt(0).findViewById(R.id.btn_view_driver);
        solo.clickOnView(profile);
        solo.sleep(500);
        solo.goBack();
        currentActivity = solo.getCurrentActivity();
        id_searchResults = currentActivity.getResources().getIdentifier("SearchedResult", "id", currentActivity.getPackageName());
        requestList = (ListView) solo.getView(id_searchResults);
        Button joinRide = (Button) requestList.getChildAt(0).findViewById(R.id.btn_joinride);
        solo.clickOnView(joinRide);

        solo.sleep(1000);


        solo.clickOnMenuItem("Logout", true);


    }
}
