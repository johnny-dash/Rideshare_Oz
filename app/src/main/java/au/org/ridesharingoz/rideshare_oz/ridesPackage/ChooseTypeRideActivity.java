package au.org.ridesharingoz.rideshare_oz.ridesPackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import au.org.ridesharingoz.rideshare_oz.R;
import au.org.ridesharingoz.rideshare_oz.firebasePackage.FirebaseAuthenticatedActivity;
import au.org.ridesharingoz.rideshare_oz.mapsPackage.MarkPinsActivity;

public class ChooseTypeRideActivity extends FirebaseAuthenticatedActivity {


    private Button ogoingto;
    private Button oleavingfrom;
    private Button rgoingto;
    private Button rleavingfrom;
    private Boolean isEvent;
    private Boolean isCreatingRide;
    private Bundle bundle;
    private TextView regularText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosetyperide);
        bundle = getIntent().getExtras();
        isCreatingRide = bundle.getBoolean("isCreatingRide");
        isEvent = bundle.getBoolean("isEvent");
        System.out.println("In chooseTypeRide, actionType is :" + isCreatingRide.toString());
        ogoingto = (Button) findViewById(R.id.button_oneoff_goingto);
        oleavingfrom = (Button) findViewById(R.id.button_oneoff_leavingfrom);
        rgoingto = (Button) findViewById(R.id.button_regular_goingto);
        rleavingfrom = (Button) findViewById(R.id.button_regular_leavingfrom);
        regularText = (TextView) findViewById(R.id.regularRideText);
        if (isEvent){
            regularText.setVisibility(View.INVISIBLE);
            rgoingto.setVisibility(View.INVISIBLE);
            rleavingfrom.setVisibility(View.INVISIBLE);
        }

        ogoingto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isGoingTo = true;
                Intent intent = setupIntent(isGoingTo);
                intent.putExtra("isRegular", false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        oleavingfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isGoingTo = false;
                Intent intent = setupIntent(isGoingTo);
                intent.putExtra("isRegular", false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rgoingto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isGoingTo = true;
                Intent intent = setupIntent(isGoingTo);
                intent.putExtra("isRegular", true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rleavingfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isGoingTo = false;
                Intent intent = setupIntent(isGoingTo);
                intent.putExtra("isRegular", true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private Intent setupIntent(boolean isGoingTo){
        Intent intent = null;
        if (isCreatingRide) {
            intent = new Intent(ChooseTypeRideActivity.this, MarkPinsActivity.class);
            intent.putExtra("isGoingTo", isGoingTo);
            return intent;
        }
        else if(isGoingTo){
            intent = new Intent(ChooseTypeRideActivity.this, SearchGoingtoRideActivity.class);
            return intent;
            }
        else {
            intent = new Intent(ChooseTypeRideActivity.this, SearchLeavingfromActivity.class);
            return intent;
        }
    }



}