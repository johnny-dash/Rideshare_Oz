package au.org.ridesharingoz.rideshare_oz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import au.org.ridesharingoz.rideshare_oz.R;

public class SigninActivity extends AppCompatActivity {

    /* A button for submit signin info to firebase*/
    private Button SigninButton;

    /* A reference to the Firebase */
    private Firebase mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        /* Firebase setting*/
        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        /* button setting*/
        SigninButton = (Button) findViewById(R.id.btn_register);
        SigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Register(){
        EditText username_ET = (EditText) findViewById(R.id.username);
        String username = username_ET.getText().toString();
        EditText password_ET = (EditText) findViewById(R.id.password);
        String password = password_ET.getText().toString();

        if(!username.equals("")&&!password.equals(""))
        {
            User user = new User(username,password);
            mFirebaseRef.child("User").push().setValue(user);
            username_ET.setText("");
            password_ET.setText("");
        }

    }

}
