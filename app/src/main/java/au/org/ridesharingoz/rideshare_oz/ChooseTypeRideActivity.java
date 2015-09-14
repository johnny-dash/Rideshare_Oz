package au.org.ridesharingoz.rideshare_oz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Ocunidee on 14/09/2015.
 */
public class ChooseTypeRideActivity extends Activity {


    private Button oneoff = null;
    private Button regular = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosetyperide);

        oneoff = (Button) findViewById(R.id.button_oneoff);
        regular = (Button) findViewById(R.id.button_regular);

        oneoff.setOnClickListener(buttonListener);
        regular.setOnClickListener(buttonListener);
    }


    public View.OnClickListener buttonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.button_joingroup:
                    intent = new Intent(ChooseTypeRideActivity.this, RegularRideActivity.class); //to change depending on the actual name
                    break;
                case R.id.button_searchride:
                    intent = new Intent(ChooseTypeRideActivity.this, OneOffRideActivity.class); // to change depending on the actual name
                    break;
            }
            startActivity(intent);
        }

    };



}
