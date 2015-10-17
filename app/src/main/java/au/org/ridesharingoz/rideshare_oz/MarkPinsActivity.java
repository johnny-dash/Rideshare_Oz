package au.org.ridesharingoz.rideshare_oz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

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
                if (numDeparturePins >= 1 && numDestinationPins >= 1) {
                    Intent submitIntent = null;
                    if (isRegular) {
                        if (isGoingTo) {
                            submitIntent = new Intent(MarkPinsActivity.this, RegularRideActivity.class);
                        } else {
                            submitIntent = new Intent(MarkPinsActivity.this, RegularRideActivity.class);
                        }
                    } else {
                        if (isGoingTo) {
                            submitIntent = new Intent(MarkPinsActivity.this, OneRideGoingtoActivity.class);
                        } else {
                            submitIntent = new Intent(MarkPinsActivity.this, OneRideLeavingfromActivity.class);
                        }
                    }
                    ArrayList<Pin> pinsArray = new ArrayList<Pin>(pins.values());
                    submitIntent.putExtra("pins", pinsArray);
                    submitIntent.putExtras(intent.getExtras());
                    startActivity(submitIntent);
                } else if (numDeparturePins == 0) {
                    Toast.makeText(MarkPinsActivity.this, "You must mark a departure point",
                            Toast.LENGTH_LONG).show();
                } else if (numDestinationPins == 0) {
                    Toast.makeText(MarkPinsActivity.this, "You must mark an arrival point",
                            Toast.LENGTH_LONG).show();
                }
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

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                // Act as if we had clicked on the marker also
                customOnMarkerClick(marker);

                // If we can't set the options for this marker (it's fixed), end the function
                if (marker.getSnippet() != null && marker.getSnippet().equals(getString(R.string.fixed))) {
                    return;
                }

                // Mark the marker object as final so we can affect it in the dialog listener
                final Marker theMarker = marker;

                // Display an options dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MarkPinsActivity.this);
                builder.setTitle(R.string.options)
                        .setItems(R.array.pin_options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Do a function based on the selected option
                                switch (which) {

                                    // Mark as a departure point pin
                                    case 0:

                                        // Check if we're allowed to add a departure point
                                        if (numDeparturePins == 0) {

                                            // Update counters
                                            if (pins.get(theMarker.getId()).getType().equals("arrival")) {
                                                numDestinationPins--;
                                            }

                                            // Change the pin type
                                            pins.get(theMarker.getId()).setType("departure");
                                            theMarker.setTitle(getString(R.string.departure));
                                            numDeparturePins++;
                                        }

                                        // Alert the user if not (if the problem isn't the selected pin)
                                        else if (!pins.get(theMarker.getId()).getType().equals("departure")) {
                                            Toast.makeText(MarkPinsActivity.this, "There is already a departure point pin",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                        break;

                                    // Mark as a pick-up point pin
                                    case 1:

                                        // Update the counters
                                        if (pins.get(theMarker.getId()).getType().equals("arrival")) {
                                            numDestinationPins--;
                                        }
                                        if (pins.get(theMarker.getId()).getType().equals("departure")) {
                                            numDeparturePins--;
                                        }

                                        // Change the pin type
                                        pins.get(theMarker.getId()).setType("mid");
                                        theMarker.setTitle(getString(R.string.pickup));

                                        break;

                                    // Mark as a arrival point pin
                                    case 2:

                                        // Check if we're allowed to add an arrival point
                                        if (numDestinationPins == 0) {

                                            // Update counters
                                            if (pins.get(theMarker.getId()).getType().equals("departure")) {
                                                numDeparturePins--;
                                            }

                                            // Change the pin type
                                            pins.get(theMarker.getId()).setType("arrival");
                                            theMarker.setTitle(getString(R.string.arrival));
                                            numDestinationPins++;
                                        }

                                        // Alert the user if not (if the problem isn't the selected pin)
                                        else if (!pins.get(theMarker.getId()).getType().equals("arrival")) {
                                            Toast.makeText(MarkPinsActivity.this, "There is already an arrival point pin",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                        break;

                                    // Remove the pin
                                    case 3:

                                        // Update the counters
                                        if (pins.get(theMarker.getId()).getType().equals("arrival")) {
                                            numDestinationPins--;
                                        }
                                        if (pins.get(theMarker.getId()).getType().equals("departure")) {
                                            numDeparturePins--;
                                        }

                                        // Remove the pin from the pins object and from the map
                                        pins.remove(theMarker.getId());
                                        theMarker.remove();

                                        break;

                                    default:
                                        break;
                                }

                                // Update the info window
                                theMarker.hideInfoWindow();
                                theMarker.showInfoWindow();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });
    }

    private void addPinFromID(String pinID) {
        Query fixedpointnode = mFirebaseRef.child("fixedpins").child(pinID);
        fixedpointnode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String type;
                if (isGoingTo) {
                    type = "arrival";
                }
                else {
                    type = "departure";
                }
                Pin fixedPin = new Pin(
                        null,
                        (double) dataSnapshot.child("longitude").getValue(),
                        (double) dataSnapshot.child("latitude").getValue(),
                        (String) dataSnapshot.child("address").getValue(),
                        null,
                        null,
                        null,
                        type
                );
                addMarker(new LatLng(fixedPin.getlatitude(), fixedPin.getlongitude()), fixedPin, true);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}
