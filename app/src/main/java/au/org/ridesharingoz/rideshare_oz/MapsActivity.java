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

    protected GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final int PERMISSION_REQUEST_LOCATION = 0;
    private View mLayout;

    protected Intent intent;

    protected EditText mTextbox;
    protected Button   addressButton;
    protected Button   submitButton;

    private Geocoder geocoder;

    protected Map<String, Pin> pins = new HashMap<>();
    protected int numDeparturePins;
    protected int numDestinationPins;

    private final static int ZOOM_LEVEL = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        intent = getIntent();

        mTextbox      = (EditText) findViewById(R.id.address);
        submitButton  = (Button)   findViewById(R.id.submit);
        addressButton = (Button)   findViewById(R.id.address_submit);

        // Respond to addresses entered in the address box
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
    protected void setUpMap() {

        final double DEFAULT_LONGITUDE = -37.8136111;
        final double DEFAULT_LATITUDE  = 144.9630556;

        mMap.setMyLocationEnabled(true);

        // Set up the location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria               = new Criteria();
        LatLng locationLatLng           = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Location is enabled, request location
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, ZOOM_LEVEL));

        // Get geocoder stuff
        geocoder = new Geocoder(this, Locale.getDefault());

        // Update the address box as camera is moved
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                //mTextbox.setText("");
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                customOnMarkerClick(marker);
                return true;
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

    // Update the address label
    private void updateAddressLabel() {
        LatLng target = mMap.getCameraPosition().target;
        mTextbox.setText(getAddressFromLatLng(target));
    }

    // Given a LatLng, get an address string
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
        } catch (IOException|IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return addressString;
    }

    // Given an address string, get a LatLng
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

    // Add a marker to the map, update the pins object
    protected Pin addMarker(LatLng location, Pin fixedPin, Boolean options) {

        // Start a marker object
        MarkerOptions markerOptions = new MarkerOptions().position(location);

        // Set the title of the object
        if (fixedPin == null && options) {
            markerOptions.title(getString(R.string.pickup));
            markerOptions.snippet(getString(R.string.has_options));
        }
        else {
            markerOptions.snippet(getString(R.string.fixed));
        }
        if (fixedPin != null && fixedPin.getType() != null && fixedPin.getType().equals("departure")) {
            markerOptions.title(getString(R.string.departure));
        }
        else if (fixedPin != null && fixedPin.getType() != null && fixedPin.getType().equals("arrival")) {
            markerOptions.title(getString(R.string.arrival));
        }

        // Add the marker to the map
        Marker marker = mMap.addMarker(markerOptions);
        customOnMarkerClick(marker);

        // Add the pin object to our array of pins
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
                    "mid"
            );
            pins.put(marker.getId(), pin);
        }
        else {
            pin = fixedPin;
            if (pin.getType() != null && pin.getType().equals("departure")) {
                numDeparturePins++;
            }
            if (pin.getType() != null && pin.getType().equals("arrival")) {
                numDestinationPins++;
            }
        }

        // Return the pin
        return pin;
    }

    // As we cannot programmatically trigger a marker click, we call this instead to simulate it
    protected void customOnMarkerClick(Marker marker) {
        final Marker theMarker = marker;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), ZOOM_LEVEL), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                theMarker.showInfoWindow();
                updateAddressLabel();
            }

            @Override
            public void onCancel() {

            }
        });
    }

}
