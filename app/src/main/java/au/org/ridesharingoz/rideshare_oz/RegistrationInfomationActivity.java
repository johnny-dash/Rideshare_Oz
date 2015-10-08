package au.org.ridesharingoz.rideshare_oz;

import android.content.Intent;
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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.MutableData;
import com.firebase.client.Query;
import com.firebase.client.snapshot.Node;

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
    String callingActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_infomation);
        callingActivity = getIntent().getStringExtra("from");
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


    public void dataSubmit(){

        String uid = mAuthData.getUid();
        Firebase userRef = mFirebaseRef.child("users").child(uid);

        String lastName = lNameText.getText().toString();
        String firstName = fNameText.getText().toString();
        String phoneNumber = phoneNbText.getText().toString();
        String licenseNumber = licenseNbText.getText().toString();
        String licenseType = "";


        boolean emptyF = isEmptyEditText(firstName, fNameText);
        boolean emptyL = isEmptyEditText(lastName,lNameText);
        boolean emptyP = isEmptyEditText(phoneNumber,phoneNbText);

        int selectedType = radioLicenseTypeGroup.getCheckedRadioButtonId();
        radioLicenseTypeButton = (RadioButton)findViewById(selectedType);
        if (selectedType != -1) {
            licenseType = radioLicenseTypeButton.getText().toString();
        }
        else licenseType = null;

        if (!emptyF & !emptyL & !emptyP) {
            User user = new User(firstName, lastName, phoneNumber, licenseNumber, licenseType);
            userRef.setValue(user);
            if (callingActivity.equals("registration")){
                Intent intent = new Intent(getApplicationContext(), JoinGroupActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getApplicationContext(), ActionChoiceActivity.class);
                startActivity(intent);
            }

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
