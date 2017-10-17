package com.github.muffindreamers.rous.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.RatData;

/**
 * Created by Brooke on 10/7/2017.
 */
public class DetailedRatScreen extends AppCompatActivity {

    /**
     * Creates the detailed rat screen by
     * filling in text views with the RatData Object's
     * instance data
     * @param savedInstanceState the instance passed in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        RatData rat = (RatData) extras.getSerializable("rat");
        setContentView(R.layout.activity_detailed_rat_screen);
        TextView idTextView = (TextView)findViewById(R.id.rat_id);
        idTextView.setText("Rat Identification Number: " + rat.getId());

        TextView LocationTypeTextView = (TextView)findViewById(R.id.location_type);
        LocationTypeTextView.setText("Location Type: " + rat.getLocationType());

        TextView boroughTextView = (TextView)findViewById(R.id.borough);
        boroughTextView.setText("Borough Location: " + rat.getBorough());

        TextView zipTextView = (TextView)findViewById(R.id.zipcode);
        zipTextView.setText("Zipcode: " + rat.getZipCode());

        TextView addressTextView = (TextView)findViewById(R.id.address);
        addressTextView.setText("Street Address: " + rat.getStreetAddress());

        TextView cityTextView = (TextView)findViewById(R.id.city);
        cityTextView.setText("City Location: " + rat.getCity());

        TextView latitudeTextView = (TextView)findViewById(R.id.latitude);
        latitudeTextView.setText("Latitude: " + rat.getLatitude());

        TextView longitudeTextView = (TextView)findViewById(R.id.longitude);
        longitudeTextView.setText("Longitude: " + rat.getLongitude());

        TextView dateCreatedTextView = (TextView)findViewById(R.id.date_created);
        dateCreatedTextView.setText("Date Created: " + rat.getDateCreated());

        Button returnButton = (Button) findViewById(R.id.return_to_main);
        returnButton.setOnClickListener(this::returnHandler);
    }

    /**
     * returns the user to the main rat app screen
     * @param v the view passed into method
     */
    public void returnHandler(View v) {
        Intent backToMain = new Intent(DetailedRatScreen.this, FetchRatDataActivity.class);
        backToMain.putExtra("user", getIntent().getSerializableExtra("user"));
        backToMain.putExtra("auth", true);
        //REMOVE LATER - ONCE DATABASE IS FIXED
        backToMain.putExtra("ratlist", getIntent().getSerializableExtra("ratlist"));
        startActivity(backToMain);
    }
}
