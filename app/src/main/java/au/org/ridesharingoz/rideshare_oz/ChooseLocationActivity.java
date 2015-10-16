package au.org.ridesharingoz.rideshare_oz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ChooseLocationActivity extends MapsActivity {

    private Pin pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent submitIntent = null;
                if (pin != null) {
                    String callingActivity = intent.getStringExtra("callingActivity");
                    if (callingActivity.equals("CreateEventActivity")){
                        submitIntent = new Intent(ChooseLocationActivity.this, CreateEventActivity.class);
                    }
                    else {
                        submitIntent = new Intent(ChooseLocationActivity.this, CreateAGroupActivity.class);
                    }
                    submitIntent.putExtra("pin", pin);
                    setResult(RESULT_OK, submitIntent);
                    finish();
                }
                else {
                    Toast.makeText(ChooseLocationActivity.this, "You have not placed a pin",
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
                mMap.clear();
                pin = addMarker(arg0, null, false);
            }
        });
    }

}
