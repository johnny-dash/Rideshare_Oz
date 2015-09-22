package au.org.ridesharingoz.rideshare_oz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class RegistrationInfomation extends FirebaseAuthenticatedActivity {

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_infomation);

        submit = (Button) findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSubmit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_personal_detail, menu);
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

    public void dataSubmit(){
        Map<String, String> alanisawesomeMap = new HashMap<String, String>();
        alanisawesomeMap.put("birthYear", "1912");
        alanisawesomeMap.put("fullName", "Alan Turing");
        Map<String, Map<String, String>> users = new HashMap<String, Map<String, String>>();
        users.put("alanisawesome", alanisawesomeMap);

        mFirebaseRef.setValue(users);
    }

}
