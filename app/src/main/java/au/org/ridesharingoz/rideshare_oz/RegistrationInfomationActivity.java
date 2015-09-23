package au.org.ridesharingoz.rideshare_oz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationInfomationActivity extends FirebaseAuthenticatedActivity {

    Button submit;
    EditText fNameText;
    EditText lNameText;
    EditText phoneNbText;
    EditText licenseNbText;
    RadioGroup radioLicenseTypeGroup;
    RadioButton radioLicenseTypeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_infomation);

        submit = (Button) findViewById(R.id.btn_submit);
        fNameText = (EditText)findViewById(R.id.editFirstName);
        lNameText = (EditText)findViewById(R.id.editLastName);
        phoneNbText = (EditText)findViewById(R.id.editPhoneNumber);
        licenseNbText = (EditText)findViewById(R.id.editLicenseNumber);
        radioLicenseTypeGroup =  (RadioGroup)findViewById(R.id.licenseTypeRadioGroup);

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

        String uid = mAuthData.getUid();
        Firebase userRef = mFirebaseRef.child("users").child(uid);
        String lastName = lNameText.getText().toString();
        String firstName = fNameText.getText().toString();
        String phoneNumber = phoneNbText.getText().toString();
        String licenseNumber = licenseNbText.getText().toString();


        boolean emptyF = isEmptyEditText(firstName, fNameText);
        boolean emptyL = isEmptyEditText(lastName,lNameText);
        boolean emptyP = isEmptyEditText(phoneNumber,phoneNbText);

        int selectedType = radioLicenseTypeGroup.getCheckedRadioButtonId();
        radioLicenseTypeButton = (RadioButton)findViewById(selectedType);
        String licenseType = radioLicenseTypeButton.getText().toString();


        if (!emptyF & !emptyL & !emptyP) {
            User user = new User(firstName, lastName, phoneNumber, licenseNumber, licenseType);
            userRef.setValue(user);

            mFirebaseRef.setValue(uid);
        }
    }


    private boolean isEmptyEditText(String editTextString, EditText editText){
        if(TextUtils.isEmpty(editTextString)) {
            editText.setError("You need to fill this field");
            return true;
        }
        else return false;
    }

}
