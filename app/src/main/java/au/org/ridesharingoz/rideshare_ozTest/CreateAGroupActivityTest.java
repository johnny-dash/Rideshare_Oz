package au.org.ridesharingoz.rideshare_ozTest;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.robotium.solo.Solo;


import au.org.ridesharingoz.rideshare_oz.groupsPackage.CreateAGroupActivity;
import au.org.ridesharingoz.rideshare_oz.R;

/**
 * Created by helen on 11/10/15.
 */
public class CreateAGroupActivityTest extends ActivityInstrumentationTestCase2<CreateAGroupActivity> {
    private Solo solo;
    private Object btn_login;
    private boolean isLogin;

    private void logout() {

    }

    public CreateAGroupActivityTest() {
        super(CreateAGroupActivity.class);

    }

    @Override
    protected void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        //tearDown() is run after a test case has finished
        //finishOpenedActivities() will finish all the activities
        //been opened during the test execution
        // click menu bar to log out


        solo.finishOpenedActivities();
    }

    public void testCreateAGroupActivity() throws Exception {
        solo.unlockScreen();


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

        boolean goToMap = solo.waitForActivity("MapsActivity");
        assertTrue("Sucessfully go to map", goToMap);
        // select pin
        boolean initialID = solo.waitForFragmentById(R.id.map); //where id.satellite is defined in the R file, eventually would time out and throw error if fragment was not present
        solo.sleep(1000);
        Activity mapActivity = solo.getCurrentActivity();
        int map = mapActivity.getResources().getIdentifier("map", "id", currentAcctivity.getPackageName());
        View mapView =  solo.getView(map);
        solo.clickOnView(mapView);
        int mapSubmit = currentAcctivity.getResources().getIdentifier("submit", "id", currentAcctivity.getPackageName());
        Button btn_mapSubmit = (Button) solo.getView(mapSubmit);
        solo.clickOnView(btn_mapSubmit);

//        boolean back = solo.waitForActivity("creatAGroupActivity");
//        assertTrue("Sucessfully back to activity", back);
        currentAcctivity = solo.getCurrentActivity();
        int GroupTypeId = currentAcctivity.getResources().getIdentifier("p1privateButton", "id", currentAcctivity.getPackageName());
        RadioButton p1privateButton = (RadioButton) solo.getView(GroupTypeId);
        solo.clickOnView(p1privateButton);

        int submit = currentAcctivity.getResources().getIdentifier("btn_submit", "id", currentAcctivity.getPackageName());
        Button btn_submit = (Button) solo.getView(submit);
        solo.clickOnView(btn_submit);



    }


}