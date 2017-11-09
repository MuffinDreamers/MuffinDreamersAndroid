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
 * Created by Brooke on 10/16/2017.
 * @author Brooke White
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
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        assert extras != null;
        RatData rat = (RatData) extras.getSerializable("rat");
        setContentView(R.layout.activity_detailed_rat_screen);
        TextView idTextView = (TextView)findViewById(R.id.rat_id);
        assert rat != null;
        idTextView.setText(String.format("%s%d", getString(R.string.rat_id_num), rat.getId()));

        TextView LocationTypeTextView = (TextView)findViewById(R.id.location_type);
        LocationTypeTextView.setText(String.format("%s%s", getString(R.string.location_type),
                rat.getLocationType()));

        TextView boroughTextView = (TextView)findViewById(R.id.borough);
        boroughTextView.setText(String.format("%s%s", getString(R.string.borough),
                rat.getBorough()));

        TextView zipTextView = (TextView)findViewById(R.id.zipCode);
        zipTextView.setText(String.format("%s%d", getString(R.string.zipCode), rat.getZipCode()));

        TextView addressTextView = (TextView)findViewById(R.id.address);
        addressTextView.setText(String.format("%s%s", getString(R.string.address),
                rat.getStreetAddress()));

        TextView cityTextView = (TextView)findViewById(R.id.city);
        cityTextView.setText(String.format("%s%s", getString(R.string.city), rat.getCity()));

        TextView latitudeTextView = (TextView)findViewById(R.id.latitude);
        latitudeTextView.setText(String.format("%s%s", getString(R.string.latitude),
                rat.getLatitude()));

        TextView longitudeTextView = (TextView)findViewById(R.id.longitude);
        longitudeTextView.setText(String.format("%s%s", getString(R.string.longitude),
                rat.getLongitude()));

        TextView dateCreatedTextView = (TextView)findViewById(R.id.date_created);
        dateCreatedTextView.setText(String.format("%s%s", getString(R.string.date_created),
                rat.getDateCreated()));

        Button returnButton = (Button) findViewById(R.id.return_to_main);
        returnButton.setOnClickListener(this::returnHandler);
    }

    /**
     * returns the user to the main rat app screen
     * @param v the view passed into method
     */
    private void returnHandler(View v) {
        Intent backToMain = new Intent(DetailedRatScreen.this, FetchRatDataActivity.class);
        Intent intent2 = getIntent();
        backToMain.putExtra("user", intent2.getSerializableExtra("user"));
        backToMain.putExtra("auth", true);
        startActivity(backToMain);
    }
}
