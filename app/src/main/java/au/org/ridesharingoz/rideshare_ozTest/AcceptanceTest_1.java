package au.org.ridesharingoz.rideshare_ozTest;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;

import com.robotium.solo.Solo;

import au.org.ridesharingoz.rideshare_oz.LoginActivity;
import au.org.ridesharingoz.rideshare_oz.R;

/**
 * Created by helen on 16/10/15.
 */
public class AcceptanceTest_1 extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private Activity currentActivity;


    public AcceptanceTest_1() {
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
    public void testAcceptance_1() throws Exception {


        //Unlock the lock screen.
        solo.unlockScreen();

            //Assert that LoginActivity is opened.
            solo.assertCurrentActivity("Expected LoginActivity", "LoginActivity");
            // Enter a valid Email and password
            long uniq = System.currentTimeMillis();
            String name = "jack" + uniq;
            String email = name + "@gamil.com";
            String password = "abc1234567";
            solo.enterText((EditText) solo.getView("email_address"), email);
            solo.enterText((EditText) solo.getView("password"), password);
            //Click on login button
            currentActivity = solo.getCurrentActivity();
            int id_register = currentActivity.getResources().getIdentifier("btn_register", "id", currentActivity.getPackageName());
            Button register_btn = (Button) solo.getView(id_register);
            solo.clickOnView(register_btn);
            boolean validRegister = solo.waitForText("Successfully created user account with uid:");
            assertTrue("Pass", validRegister);
            solo.sleep(200);

            //Click on login button
            int id_login = currentActivity.getResources().getIdentifier("btn_login", "id", currentActivity.getPackageName());
            Button login_btn = (Button) solo.getView(id_login);
            solo.clickOnView(login_btn);



//      Fill in personal information
        solo.sleep(500);
        solo.enterText((EditText) solo.getView("editFirstName"), "Jack");
        solo.enterText((EditText)solo.getView("editLastName"),"Zhang");
        solo.enterText((EditText)solo.getView("editPhoneNumber"),"0413895555");
        solo.sleep(500);
        currentActivity = solo.getCurrentActivity();
        int id_submit = currentActivity.getResources().getIdentifier("btn_submit", "id", currentActivity.getPackageName());
        Button submit_button = (Button) solo.getView(id_submit);
        solo.clickOnView(submit_button);

        //Join a group

        boolean submitProfile = solo.waitForActivity("JoinGroupActivity");
        assertTrue("Pass", submitProfile);
        solo.sleep(3000);
        currentActivity = solo.getCurrentActivity();
        int listGroupToJoin = currentActivity.getResources().getIdentifier("listGroupToJoin","id",currentActivity.getPackageName());
        ListView groupList = (ListView) solo.getView(listGroupToJoin);
        ImageButton a = (ImageButton) groupList.getChildAt(0).findViewById(R.id.addButton);
        solo.clickOnView(a);
        solo.sleep(200);
        ImageButton b = (ImageButton) groupList.getChildAt(2).findViewById(R.id.addButton);
        solo.clickOnView(b);

        //view and edit profile
        solo.sleep(500);
        solo.clickOnMenuItem("Profile", true);
        currentActivity = solo.getCurrentActivity();
        int editProfile = currentActivity.getResources().getIdentifier("edit_profile","id",currentActivity.getPackageName());
        solo.clickOnView(solo.getView(editProfile));

        solo.sleep(500);
        solo.enterText((EditText) solo.getView("editLicenseNumber"), "12345");
        currentActivity = solo.getCurrentActivity();
        int lisenceType = currentActivity.getResources().getIdentifier("p1Button", "id", currentActivity.getPackageName());
        RadioButton p1Button = (RadioButton) solo.getView(lisenceType);
        solo.clickOnView(p1Button);
        solo.sleep(500);
        currentActivity = solo.getCurrentActivity();
        id_submit = currentActivity.getResources().getIdentifier("btn_submit", "id", currentActivity.getPackageName());
        solo.clickOnView(solo.getView(id_submit));


        //logout
        solo.sleep(500);
        solo.clickOnMenuItem("Logout",true);



    }}
