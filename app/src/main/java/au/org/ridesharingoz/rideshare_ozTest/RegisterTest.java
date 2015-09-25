package au.org.ridesharingoz.rideshare_ozTest;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.robotium.solo.Solo;

import junit.framework.TestSuite;

import java.util.Date;
import java.util.Random;

import au.org.ridesharingoz.rideshare_oz.LoginActivity;

/**
 * Created by helen on 24/09/15.
 */
public class RegisterTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private Object btn_login;
    private boolean isLogin;


    public RegisterTest() {
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
        // click menu bar to log out


        solo.finishOpenedActivities();
    }
    public void testExistedAccount()throws Exception{
        //Unlock the lock screen.
        solo.unlockScreen();
        //Assert that LoginActivity is opened.
        solo.assertCurrentActivity("Expected LoginActivity", "LoginActivity");
        solo.enterText((EditText) solo.getView("email_address"), "hailunt@student.unimelb.edu.au");
        solo.enterText((EditText) solo.getView("password"), "abc123456");
        Activity currentAcctivity = solo.getCurrentActivity();
        int id_register = currentAcctivity.getResources().getIdentifier("btn_register", "id", currentAcctivity.getPackageName());
        Button register_btn = (Button) solo.getView(id_register);
        solo.clickOnView(register_btn);
        solo.assertCurrentActivity("Pass", LoginActivity.class);

    }

    public void testValidRegisterActivity() throws Exception {
        //Unlock the lock screen.
        solo.unlockScreen();
        //Assert that LoginActivity is opened.
        solo.assertCurrentActivity("Expected LoginActivity", "LoginActivity");
        // Enter a valid Email and password
        long uniq = System.currentTimeMillis();
        String name = "jack"+ uniq;
        String email = name + "@gamil.com";
        String password = "abc1234567";
        solo.enterText((EditText) solo.getView("email_address"), email);
        solo.enterText((EditText) solo.getView("password"), password);
        //Click on login button
        Activity currentAcctivity = solo.getCurrentActivity();
        int id_register = currentAcctivity.getResources().getIdentifier("btn_register", "id", currentAcctivity.getPackageName());
        Button register_btn = (Button) solo.getView(id_register);
        solo.clickOnView(register_btn);
        boolean validRegister = solo.waitForText("Successfully created user account with uid:");
        assertTrue("Pass",validRegister);
        solo.sleep(200);

        //Click on login button
        int id_login = currentAcctivity.getResources().getIdentifier("btn_login", "id", currentAcctivity.getPackageName());
        Button login_btn = (Button) solo.getView(id_login);
        solo.clickOnView(login_btn);



//        int id_firstName = currentAcctivity.getResources().getIdentifier("editFirstName","id",currentAcctivity.getPackageName());
//        solo.enterText(id_firstName,"Jack");
        // enter user details
//        solo.enterText((EditText)solo.getView("editFirstName"),"Jack");
//        solo.enterText((EditText)solo.getView("editLastName"),"Zhang");
//        solo.enterText((EditText)solo.getView("editPhoneNumber"),"0413895555");
//        int id_submit = currentAcctivity.getResources().getIdentifier("btn_submit", "id", currentAcctivity.getPackageName());
//        Button submit_button = (Button) solo.getView(id_login);
//        solo.clickOnView(login_btn);
//
//
//
//        solo.sleep(200);
//        solo.clickOnMenuItem("Logout");
//        solo.sleep(300);


    }


}