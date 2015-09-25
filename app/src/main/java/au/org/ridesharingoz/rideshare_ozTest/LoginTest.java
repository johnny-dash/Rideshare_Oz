package au.org.ridesharingoz.rideshare_ozTest;

import android.app.Activity;
import android.content.ClipData;
import android.support.v7.internal.view.menu.MenuView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.List;

import au.org.ridesharingoz.rideshare_oz.LoginActivity;
import au.org.ridesharingoz.rideshare_oz.R;

/**
 * Created by helen on 23/09/15.
 */
public class LoginTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;
    private Object btn_login;
    private boolean isLogin;

    private void logout() {

    }

    public LoginTest() {
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

    public void testInvalidLoginActivity() throws Exception {
        //Unlock the lock screen.
        solo.unlockScreen();
        //Assert that LoginActivity is opened.
        Activity currentAcctivity = solo.getCurrentActivity();
        solo.assertCurrentActivity("Expected LoginActivity", "LoginActivity");
        solo.sleep(3000);
        //if user session
        isLogin = solo.waitForActivity("LoginActivity");
        if (!isLogin) {
            solo.clickOnMenuItem("Logout");
            ;
        }
        // Enter a valid Email and password
        solo.enterText((EditText) solo.getView("email_address"), "invalidUser@gmail.com");
        solo.enterText((EditText) solo.getView("password"), "wrongPassword");
        //Click on login button
        int id = currentAcctivity.getResources().getIdentifier("btn_login", "id", currentAcctivity.getPackageName());
        Button login_btn = (Button) solo.getView(id);
        solo.clickOnView(login_btn);
        //Check if login
        solo.assertCurrentActivity("Invalid user can not log in, test pass!", LoginActivity.class);
        solo.sleep(2000);
        isLogin = solo.waitForActivity("LoginActivity");
        if (!isLogin) {
            solo.clickOnMenuItem("Logout");
            ;
        }


    }


    public void testValidLoginActivity() throws Exception {
        //Unlock the lock screen.
        solo.unlockScreen();
        //Assert that LoginActivity is opened.
        solo.assertCurrentActivity("Expected LoginActivity", "LoginActivity");
        // Enter a valid Email and password
        solo.enterText((EditText) solo.getView("email_address"), "hailunt@student.unimelb.edu.au");
        solo.enterText((EditText) solo.getView("password"), "abc123456");
        //Click on login button
        Activity currentAcctivity = solo.getCurrentActivity();
        int id = currentAcctivity.getResources().getIdentifier("btn_login", "id", currentAcctivity.getPackageName());
        Button login_btn = (Button) solo.getView(id);
        solo.clickOnView(login_btn);

        boolean authSuceess = solo.waitForActivity("ActionChoiceActivity");
        assertTrue("Sucessfully log in", authSuceess);
        solo.sleep(1000);
        solo.clickOnMenuItem("Logout");
        ;
        solo.sleep(300);


    }


}


