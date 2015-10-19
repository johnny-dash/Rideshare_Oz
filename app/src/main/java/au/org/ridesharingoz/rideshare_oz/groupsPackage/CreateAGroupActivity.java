package au.org.ridesharingoz.rideshare_oz.groupsPackage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

import au.org.ridesharingoz.rideshare_oz.R;
import au.org.ridesharingoz.rideshare_oz.dataPackage.Group;
import au.org.ridesharingoz.rideshare_oz.dataPackage.Pin;
import au.org.ridesharingoz.rideshare_oz.firebasePackage.FirebaseAuthenticatedActivity;
import au.org.ridesharingoz.rideshare_oz.mapsPackage.ChooseLocationActivity;

/**
 * Created by Ocunidee on 30/09/2015.
 */
public class CreateAGroupActivity extends FirebaseAuthenticatedActivity {

    Button submit;
    Button goToMap;
    EditText groupNameText;
    EditText groupDescriptionText;
    RadioGroup radioCategoryGroup;
    RadioButton radioCategoryButton;
    RadioGroup radioGroupTypeGroup;
    RadioButton radioGroupTypeButton;
    int PIN_REQUEST = 1;
    Pin fixedPin = null;
    String pinID = null;


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

        /* *************************************
        *          Go to Map button listener      *
        ***************************************/

        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPinFromMap = new Intent(CreateAGroupActivity.this, ChooseLocationActivity.class);
                getPinFromMap.putExtra("callingActivity","CreateAGroupActivity");
                startActivityForResult(getPinFromMap, PIN_REQUEST);
            }
        });


        /* *************************************
        *      Submit  button listener      *
        ***************************************/

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSubmit();
            }
        });
    }

        /* *************************************
        *    Create Group and Pin on submit    *
        ***************************************/

    public void dataSubmit() {

        String uid = mAuthData.getUid();
        Firebase groupsRef = mFirebaseRef.child("groups");

        String groupName = groupNameText.getText().toString();
        String groupDescription = groupDescriptionText.getText().toString();
        String groupCategory;
        Boolean privateGroup;

        boolean emptyN = isEmptyEditText(groupName, groupNameText);
        boolean emptyD = isEmptyEditText(groupDescription, groupDescriptionText);

        //get the radioButton chosen for group type
        int selectedType = radioGroupTypeGroup.getCheckedRadioButtonId();
        radioGroupTypeButton = (RadioButton) findViewById(selectedType);
        if (selectedType != -1) {
            if (radioGroupTypeButton.getText().toString().equals("Private Group")) {
                privateGroup = true;
            } else privateGroup = false;
        } else privateGroup = null;

        // get the radioButton chosen for
        int selectedCategory = radioCategoryGroup.getCheckedRadioButtonId();
        radioCategoryButton = (RadioButton) findViewById(selectedCategory);
        if (selectedCategory != -1) {
            groupCategory = radioCategoryButton.getText().toString();
        } else groupCategory = null;

        //Data validation then pin and group creation in firebase and update of owner
        if (!emptyN & !emptyD & privateGroup != null & groupCategory != null & fixedPin != null) {
            Firebase pinsRef = mFirebaseRef.child("fixedpins");
            Firebase uniqueID = pinsRef.push();
            uniqueID.setValue(fixedPin);
            pinID = uniqueID.getKey();
            Group group = new Group(groupName, groupDescription, groupCategory, pinID, privateGroup);
            group.setGroupOwner(uid);
            Firebase groupUniqueID = groupsRef.push();
            groupUniqueID.setValue(group);
            String groupID = groupUniqueID.getKey();
            Firebase ownerRef = mFirebaseRef.child("users").child(uid).child("groupsOwned");
            Map<String, Object> groupOwned = new HashMap<>();
            groupOwned.put(groupID, true);
            ownerRef.updateChildren(groupOwned);
            Firebase joinRef = mFirebaseRef.child("users").child(uid).child("groupsJoined");
            joinRef.updateChildren(groupOwned);
            Toast.makeText(getApplicationContext(), "Group created successfully.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), ManageMyGroupsActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Group cannot be created. Missing data.", Toast.LENGTH_LONG).show();
        }
    }

    //checks if text data has been entered
    private boolean isEmptyEditText(String editTextString, EditText editText) {
        if (TextUtils.isEmpty(editTextString)) {
            editText.setError("You need to fill this field");
            return true;
        } else return false;
    }

    //gets pin data back from the ChooseLocationActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                fixedPin = (Pin) data.getSerializableExtra("pin");
            }
        }


    }

}
