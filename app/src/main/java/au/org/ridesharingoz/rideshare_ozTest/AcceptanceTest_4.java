package au.org.ridesharingoz.rideshare_ozTest;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.robotium.solo.Solo;

import au.org.ridesharingoz.rideshare_oz.userPackage.LoginActivity;
import au.org.ridesharingoz.rideshare_oz.R;

/**
 * Created by helen on 18/10/15.
 */
public class AcceptanceTest_4 extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private Activity currentActivity;
    private String email = "finaltest@gmail.com";
    private String password = "123";


    public AcceptanceTest_4() {
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

        This test will simulate a user accept a request for her ride and create a group.
        Since we can not accept requests now, this test can only create a group.

    */
    public void testAcceptance_4() throws Exception {

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
        solo.sleep(1000);
        currentActivity = solo.getCurrentActivity();
        int id_createAGroup = currentActivity.getResources().getIdentifier("button_createagroup","id",currentActivity.getPackageName());
        solo.clickOnView(solo.getView(id_createAGroup));

        solo.sleep(1000);



        // Wait for CreateAGroupActivity
        solo.waitForActivity("CreateAGroupActivity");
        Activity currentAcctivity = solo.getCurrentActivity();
        solo.enterText((EditText) solo.getView("createGroupName"), "TestGroup");
        // select group type

        int GroupCategroyId = currentAcctivity.getResources().getIdentifier("pasttimeButton", "id", currentAcctivity.getPackageName());
        RadioButton pasttimeButton = (RadioButton) solo.getView(GroupCategroyId);
        solo.clickOnView(pasttimeButton);

        solo.enterText((EditText) solo.getView("createGroupDescription"), "This group is created to test this function.");
        int mapButton = currentAcctivity.getResources().getIdentifier("createFixedPointAddress","id",currentAcctivity.getPackageName());
        Button createFixedPointAddress = (Button) solo.getView(mapButton);
        solo.clickOnView(createFixedPointAddress);


        // select pin
        boolean initialID = solo.waitForFragmentById(R.id.map); //where id.satellite is defined in the R file, eventually would time out and throw error if fragment was not present
        solo.sleep(1000);
        Activity mapActivity = solo.getCurrentActivity();
        int map = mapActivity.getResources().getIdentifier("map", "id", currentAcctivity.getPackageName());
        View mapView =  solo.getView(map);
        solo.clickOnView(mapView);
        solo.sleep(1000);
        int mapSubmit = currentAcctivity.getResources().getIdentifier("submit", "id", currentAcctivity.getPackageName());
        Button btn_mapSubmit = (Button) solo.getView(mapSubmit);
        solo.clickOnView(btn_mapSubmit);
        solo.sleep(1000);

//        boolean back = solo.waitForActivity("creatAGroupActivity");
//        assertTrue("Sucessfully back to activity", back);
        currentAcctivity = solo.getCurrentActivity();
        int GroupTypeId = currentAcctivity.getResources().getIdentifier("p1privateButton", "id", currentAcctivity.getPackageName());
        RadioButton p1privateButton = (RadioButton) solo.getView(GroupTypeId);
        solo.clickOnView(p1privateButton);
        solo.sleep(500);
        int submit = currentAcctivity.getResources().getIdentifier("btn_submit", "id", currentAcctivity.getPackageName());
        Button btn_submit = (Button) solo.getView(submit);
        solo.clickOnView(btn_submit);
        solo.sleep(1000);


        solo.clickOnMenuItem("Logout", true);



    }

}
