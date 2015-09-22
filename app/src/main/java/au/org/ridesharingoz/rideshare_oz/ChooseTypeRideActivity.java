package au.org.ridesharingoz.rideshare_oz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseTypeRideActivity extends FirebaseAuthenticatedActivity {


    private Button oneoff = null;
    private Button regular = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosetyperide);

        oneoff = (Button) findViewById(R.id.button_oneoff);
        regular = (Button) findViewById(R.id.button_regular);

        oneoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseTypeRideActivity.this, OneRideActivity.class);
                startActivity(intent);
            }
        });
        regular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseTypeRideActivity.this, RegularRideActivity.class);
                startActivity(intent);
            }
        });
    }

}