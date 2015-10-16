package au.org.ridesharingoz.rideshare_oz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MarkPinsActivity extends MapsActivity {

    private Boolean isGoingTo;
    private Boolean isRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isGoingTo = intent.getBooleanExtra("isGoingTo", false);
        isRegular = intent.getBooleanExtra("isRegular", false);

        // Get fixed pin
        if (intent.getSerializableExtra("pins") != null) {
            List<String> intentPins;
            intentPins = (List<String>) intent.getSerializableExtra("pins");
            addPinFromID(intentPins.get(1));
        }
        else if (intent.getSerializableExtra("Pin") != null) {
            addPinFromID((String) intent.getSerializableExtra("Pin"));
        }

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent submitIntent = null;
                if (isRegular) {
                    if (isGoingTo) {
                        submitIntent = new Intent(MarkPinsActivity.this, RegularRideActivity.class);
                    }
                    else {
                        submitIntent = new Intent(MarkPinsActivity.this, RegularRideActivity.class);
                    }
                } else {
                    if (isGoingTo) {
                        submitIntent = new Intent(MarkPinsActivity.this, OneRideGoingtoActivity.class);
                    }
                    else {
                        submitIntent = new Intent(MarkPinsActivity.this, OneRideLeavingfromActivity.class);
                    }
                }
                ArrayList<Pin> pinsArray = new ArrayList<Pin>(pins.values());
                submitIntent.putExtra("pins", pinsArray);
                submitIntent.putExtras(intent.getExtras());
                startActivity(submitIntent);
            }

        });

    }

    protected void setUpMap() {
        super.setUpMap();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                addMarker(arg0, null, true);
            }
        });
    }

    private void addPinFromID(String pinID) {
        Query fixedpointnode = mFirebaseRef.child("fixedpins").child(pinID);
        fixedpointnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pin fixedPin = new Pin(
                        null,
                        (double) dataSnapshot.child("longitude").getValue(),
                        (double) dataSnapshot.child("latitude").getValue(),
                        (String) dataSnapshot.child("address").getValue(),
                        null,
                        null,
                        null,
                        (String) dataSnapshot.child("type").getValue()
                );
                addMarker(new LatLng(fixedPin.getlatitude(), fixedPin.getlongitude()), fixedPin, false);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}
