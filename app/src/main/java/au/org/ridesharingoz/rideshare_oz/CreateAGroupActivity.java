package au.org.ridesharingoz.rideshare_oz;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;

/**
 * Created by Ocunidee on 30/09/2015.
 */
public class CreateAGroupActivity extends FirebaseAuthenticatedActivity {

    Button submit;
    Button goToMap;  //TODO link this activity to the map to set a pin
    EditText groupNameText;
    EditText groupDescriptionText;
    EditText groupOwnerText;
    RadioGroup radioCategoryGroup;
    RadioButton radioCategoryButton;
    RadioGroup radioGroupTypeGroup;
    RadioButton radioGroupTypeButton;
    int PIN_REQUEST = 1;
    FixedPin fixedPointAddress = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_a_group);

        submit = (Button) findViewById(R.id.btn_submit);
        goToMap = (Button) findViewById(R.id.createFixedPointAddress);
        groupNameText = (EditText) findViewById(R.id.createGroupName);
        groupDescriptionText = (EditText) findViewById(R.id.createGroupDescription);
        radioGroupTypeGroup = (RadioGroup) findViewById(R.id.groupTypeRadioGroup);
        radioCategoryGroup = (RadioGroup) findViewById(R.id.categoryRadioGroup);

        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPinFromMap = new Intent(CreateAGroupActivity.this, MapsActivity.class);
                startActivityForResult(getPinFromMap, PIN_REQUEST);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSubmit();
            }
        });
    }


    public void dataSubmit() {

        String uid = mAuthData.getUid();
        Firebase groupsRef = mFirebaseRef.child("groups");

        String groupName = groupNameText.getText().toString();
        String groupDescription = groupDescriptionText.getText().toString();
        String groupCategory;
        Boolean privateGroup;


        boolean emptyN = isEmptyEditText(groupName, groupNameText);
        boolean emptyD = isEmptyEditText(groupDescription, groupDescriptionText);

        int selectedType = radioGroupTypeGroup.getCheckedRadioButtonId();
        radioGroupTypeButton = (RadioButton) findViewById(selectedType);
        if (selectedType != -1) {
            if (radioGroupTypeButton.getText().toString().equals("Private Group")) {
                privateGroup = true;
            } else privateGroup = false;
        } else privateGroup = null;

        int selectedCategory = radioGroupTypeGroup.getCheckedRadioButtonId();
        radioCategoryButton = (RadioButton) findViewById(selectedCategory);
        if (selectedType != -1) {
            groupCategory = radioGroupTypeButton.getText().toString();
        } else groupCategory = null;

/*        if (!emptyN & !emptyD & privateGroup != null & groupCategory != null & fixedPointAddress != null) { //Data validation
            Group group = new Group(groupName, groupDescription, groupCategory, pinID, privateGroup);
            group.setGroupOwner(uid);
            groupsRef.push().setValue(group);
            Intent intent = new Intent(getApplicationContext(), ManageMyGroupsActivity.class);
            startActivity(intent);
        } */
    } 


    private boolean isEmptyEditText(String editTextString, EditText editText) {
        if (TextUtils.isEmpty(editTextString)) {
            editText.setError("You need to fill this field");
            return true;
        } else return false;
    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == REQUEST_CANCELED) {
            // code to handle cancelled state
        } else if (requestCode == PIN_REQUEST) {
            FixedPin fixedPointAddress = new FixedPin();
        }

    } */

}
