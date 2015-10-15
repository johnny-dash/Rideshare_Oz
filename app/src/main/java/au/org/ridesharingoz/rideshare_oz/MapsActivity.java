package au.org.ridesharingoz.rideshare_oz;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FirebaseAuthenticatedActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final int PERMISSION_REQUEST_LOCATION = 0;
    private View mLayout;

    private EditText mTextbox;
    private Button addressButton;
    private Geocoder geocoder;

    private String rideType;
    private int destinationPins;

    private Pin fixedPin;

    Map<String, Pin> pins = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final Bundle bundle = getIntent().getExtras();

        Intent intent = getIntent();
        rideType = intent.getStringExtra("rideType");
        fixedPin = (Pin) intent.getSerializableExtra("fixedPin");

        mTextbox = (EditText) findViewById(R.id.address);

        Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (rideType != null) {
                    switch (rideType) {
                        case "regular":
                            intent = new Intent(MapsActivity.this, RegularRideActivity.class);
                            break;

                        default:
                            intent = new Intent(MapsActivity.this, OneRideGoingtoActivity.class);
                            break;
                    }
                    ArrayList<Pin> pinsArray = new ArrayList<Pin>(pins.values());
                    intent.putExtra("pins", pinsArray);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    // Group creation
                    if (!pins.isEmpty()) {
                        String callingActivity = getIntent().getStringExtra("from");
                        if (callingActivity.equals("CreateEventActivity")){
                            intent = new Intent(MapsActivity.this, CreateEventActivity.class);
                        }
                        else {
                            intent = new Intent(MapsActivity.this, CreateAGroupActivity.class);
                        }
                        intent.putExtra("pin", pins.get(0));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }

        });

        addressButton = (Button) findViewById(R.id.address_submit);
        addressButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LatLng location = getLocationFromAddress(mTextbox.getText().toString());
                if (location != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
                }
            }
        });

        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        final double DEFAULT_LONGITUDE = -37.8136111;
        final double DEFAULT_LATITUDE  = 144.9630556;

        mMap.setMyLocationEnabled(true);

        // Set up the location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria               = new Criteria();
        Location location               = null;
        LatLng locationLatLng           = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Location is enabled, request location
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            }

        }
        else {

            // Location is disabled, request permission
            requestLocationPermission();

        }

        // If we weren't able to get the current location, set to Melbourne
        if (locationLatLng == null) {
            locationLatLng = new LatLng(DEFAULT_LONGITUDE, DEFAULT_LATITUDE);
        }

        // Move the camera and add marker
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 13));
        /*mMap.addMarker(new MarkerOptions().position(locationLatLng)
                .title("Set address")
                .snippet("Please move the marker if needed.")
                .draggable(true));*/

        // Add the first fixed pin
        if (fixedPin != null) {
            addMarker(new LatLng(fixedPin.getlatitude(), fixedPin.getlongitude()), fixedPin);
        }

        // Get geocoder stuff
        geocoder = new Geocoder(this, Locale.getDefault());

        // Update the address box as camera is moved
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                //updateAddressLabel();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                addMarker(arg0, null);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                updateAddressLabel();
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                // Mark the marker object as final so we can affect it in the dialog listener
                final Marker theMarker = marker;

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle(R.string.options)
                        .setItems(R.array.pin_options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        pins.get(theMarker.getId()).setType("pickup");
                                        break;

                                    case 1:
                                        if(destinationPins == 0) {
                                            pins.get(theMarker.getId()).setType("destination");
                                            destinationPins++;
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "There is already a destination pin",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        break;

                                    case 2:
                                        pins.remove(theMarker.getId());
                                        theMarker.remove();
                                        break;

                                    default:
                                        break;
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

    });

        mTextbox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    addressButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Requests the {@link android.Manifest.permission#ACCESS_FINE_LOCATION} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestLocationPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mLayout, "Location access is required to display the current location.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MapsActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Permission is not available. Requesting location permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_LOCATION);
        }
    }

    private void updateAddressLabel() {
        LatLng target = mMap.getCameraPosition().target;
        mTextbox.setText(getAddressFromLatLng(target));
    }

    private String getAddressFromLatLng(LatLng target) {

        List<Address> addresses;
        String addressString = "";

        try {
            addresses = geocoder.getFromLocation(target.latitude, target.longitude, 1);
            for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                if (i > 0) {
                    addressString += ", ";
                }
                addressString += addresses.get(0).getAddressLine(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressString;
    }

    private LatLng getLocationFromAddress(String strAddress) {

        Log.v("address", "Looking up address: " + strAddress);
        List<Address> address;

        try {
            address = geocoder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            return new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException|IndexOutOfBoundsException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Unable to locate address",
                    Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private void addMarker(LatLng location, Pin fixedPin) {
        MarkerOptions markerOptions = new MarkerOptions().position(location);
        if (fixedPin == null) {
            markerOptions.title(getString(R.string.options));
        }
        Marker marker = mMap.addMarker(markerOptions);

        Pin pin;
        if (fixedPin == null) {
            pin = new Pin(
                    null,
                    marker.getPosition().longitude,
                    marker.getPosition().latitude,
                    getAddressFromLatLng(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude)),
                    null,
                    null,
                    null,
                    "pickup"
            );
        }
        else {
            pin = fixedPin;
            if (pin.getType() == "destination") {
                destinationPins++;
            }
        }

        pins.put(marker.getId(), pin);
    }

}
