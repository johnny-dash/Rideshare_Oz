package au.org.ridesharingoz.rideshare_oz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import au.org.ridesharingoz.rideshare_oz.R;

public class SearchRideActivity extends MapsActivity {

    RadioGroup typRadioGroup;

    EditText searchdate;
    EditText searchtime;
    EditText searchaddress;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride);


        searchdate = (EditText) findViewById(R.id.search_date);
        searchtime = (EditText) findViewById(R.id.search_time);
        searchaddress = (EditText) findViewById(R.id.search_address);

        typRadioGroup = (RadioGroup) findViewById(R.id.OneoffTypeRadioGroup);
        typRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.oGoingto) {
                    type = "going to";
                } else if (checkedId == R.id.oLeavingfrom) {
                    type = "leaving from";
                } else {
                    Toast.makeText(SearchRideActivity.this, "Please select a type!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_ride, menu);
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

    public void searchRide(){
        
    }



}
