package au.org.ridesharingoz.rideshare_ozTest;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;

import au.org.ridesharingoz.rideshare_oz.LoginActivity;
import au.org.ridesharingoz.rideshare_oz.R;

/**
 * Created by helen on 24/09/15.
 */
public class OneOffRideScenarioTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private Object btn_login;
    private Activity currentAcctivity;

    private void logout() {

    }

    public OneOffRideScenarioTest() {
        super(LoginActivity.class);

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

        solo.finishOpenedActivities();
    }

    public void testOneOffRideScenario() throws Exception {

        //Unlock the lock screen.
        solo.unlockScreen();

        //Assert that LoginActivity is opened.
        solo.assertCurrentActivity("Expected LoginActivity", "LoginActivity");
        solo.sleep(1000);
        // Enter a valid Email and password
        solo.enterText((EditText) solo.getView("email_address"), "hailunt@student.unimelb.edu.au");
        solo.enterText((EditText) solo.getView("password"), "abc123456");
        solo.sleep(1000);
        //Click on login button
        currentAcctivity = solo.getCurrentActivity();
        int id_login = currentAcctivity.getResources().getIdentifier("btn_login", "id", currentAcctivity.getPackageName());
        Button login_btn = (Button) solo.getView(id_login);
        solo.clickOnView(login_btn);
        solo.sleep(1000);
        //test if user log in sucessfully
        boolean authSuceess = solo.waitForActivity("ActionChoiceActivity");
        assertTrue("Sucessfully log in and go to ActionChoiceActivity!", authSuceess);
        solo.sleep(1000);
        //Choose to offer a ride
        currentAcctivity = solo.getCurrentActivity();

        //click on offer a ride
        int id_offerRide = currentAcctivity.getResources().getIdentifier("button_offerride", "id", currentAcctivity.getPackageName());
        Button offerride_button = (Button) solo.getView(id_offerRide);
        solo.clickOnView(offerride_button);
        solo.sleep(1000);
        //test if user can go to next activity -- choose group and event
        boolean offerRideButtonSuceess = solo.waitForActivity("ChooseGroupEventActivity");
        assertTrue("Sucessfully go to ChooseGroupEventActivity!", offerRideButtonSuceess);

        //Choose the University of Melbourne Group and offer a normal ride
        solo.clickOnText("The University of Melbourne");
        solo.sleep(1000);
        solo.clickOnText("Create a private ride !");
        solo.sleep(1000);
        //test if user can go to next activity -- ChooseTypeRideActivity
        boolean createRideSucess = solo.waitForActivity("ChooseTypeRideActivity");
        assertTrue("Sucessfully go to ChooseTypeRideActivity!", createRideSucess);
        solo.sleep(1000);
        //click offer one off ride
        solo.clickOnButton("One-off ride");
        solo.enterText(0, "3");
        currentAcctivity = solo.getCurrentActivity();
        int id_Date = currentAcctivity.getResources().getIdentifier("Date", "id", currentAcctivity.getPackageName());
        EditText date = (EditText) solo.getView(id_Date);
        solo.clickOnView(date);
        solo.clickOnView(date);
        solo.waitForDialogToOpen(100);
        solo.setDatePicker(0, 2015, 10, 1);
        solo.clickOnText("Done");
        //set time for each ride
        ListView ListView = (ListView) solo.getView(R.id.AddressList);

        View view1 = ListView.getChildAt(0);
        solo.clickOnView(view1);
        Button button1 = (Button) view1.findViewById(R.id.AddTime);
        solo.clickOnView(button1);
        solo.setTimePicker(0, 10, 20);
        solo.clickOnText("Done");

        View view2 = ListView.getChildAt(1);
        solo.clickOnView(view2);
        Button button2 = (Button) view2.findViewById(R.id.AddTime);
        solo.clickOnView(button2);
        solo.setTimePicker(0, 10, 30);
        solo.clickOnText("Done");

        View view3 = ListView.getChildAt(2);
        Button button = (Button) view3.findViewById(R.id.AddTime);
        solo.clickOnView(button);
        solo.setTimePicker(0, 10, 55);
        solo.clickOnText("Done");

        solo.clickOnButton("Create Ride!");
        //save successfully!
        // log out
        solo.sleep(3600);
        solo.clickOnMenuItem("Logout");
        solo.sleep(500);

    }
}
