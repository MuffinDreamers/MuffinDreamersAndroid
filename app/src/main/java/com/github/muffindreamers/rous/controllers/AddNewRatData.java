package com.github.muffindreamers.rous.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.Borough;
import com.github.muffindreamers.rous.model.RatData;
import com.github.muffindreamers.rous.model.UploadRatData;

import java.util.concurrent.ExecutionException;


/**
 * Created by Brooke 10/1/17
 */
public class AddNewRatData extends AppCompatActivity {
    private Spinner locationType;
    private Spinner borough;
    private EditText zipCode;
    private EditText city;
    private EditText address;
    private EditText latitude;
    private EditText longitude;

    /**
     * Creates and takes in new rat data
     * @param savedInstanceState the instance data passed in
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_rat_data);

        locationType = (Spinner) findViewById(R.id.location_type_spinner);
        borough = (Spinner) findViewById(R.id.borough_edit);
        zipCode = (EditText) findViewById(R.id.zipCode_edit);
        city = (EditText) findViewById(R.id.city_edit);
        address = (EditText) findViewById(R.id.address_edit);
        latitude = (EditText) findViewById(R.id.latitude_edit);
        longitude = (EditText) findViewById(R.id.longitude_edit);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, RatData.getLocationTypeArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationType.setAdapter(adapter);

        ArrayAdapter<Borough> boroughAdapter =
                new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, Borough.values());
        boroughAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        borough.setAdapter(boroughAdapter);

        Button cancel = (Button) findViewById(R.id.cancel_add_rat);
        cancel.setOnClickListener(this::cancelHandler);

        Button add_new_new = (Button) findViewById(R.id.add_new_new);
        add_new_new.setOnClickListener(this::newRatDataHandler);
    }
    /**
     * Returns the user to the Main Screen
     * @param v the view the button is located in
     */
    private void cancelHandler(View v) {
        Intent backToMain = new Intent(AddNewRatData.this, FetchRatDataActivity.class);
        Intent intent2 = getIntent();
        backToMain.putExtra("user", intent2.getSerializableExtra("user"));
        backToMain.putExtra("auth", true);
        startActivity(backToMain);
    }

    /**
     * Adds a new rat and sends the user to the
     * Main Screen
     * @param v the view the button is located in
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void newRatDataHandler(View v) {
        Intent backToMain = new Intent(this, FetchRatDataActivity.class);
        Intent intent3 = getIntent();
        backToMain.putExtra("user", intent3.getSerializableExtra("user"));
        backToMain.putExtra("auth", true);
        RatData addedRat = new RatData();

        try {
            Object locationItem = locationType.getSelectedItem();
            addedRat.setLocationType(locationItem.toString());
            Object boroughItem = borough.getSelectedItem();
            addedRat.setBorough(boroughItem.toString());
            Editable zipCodeItem = zipCode.getText();
            addedRat.setZipCode((Integer.parseInt(zipCodeItem.toString())));
            Editable cityItem = city.getText();
            addedRat.setCity(cityItem.toString());
            Editable addressItem = address.getText();
            addedRat.setStreetAddress(addressItem.toString());
            Editable latitudeItem = latitude.getText();
            addedRat.setLatitude((Double.parseDouble(latitudeItem.toString())));
            Editable longitudeItem = longitude.getText();
            addedRat.setLongitude((Double.parseDouble(longitudeItem.toString())));
        } catch(RuntimeException ex) {
            Log.e("AddNewRatData", "Parse error", ex);
            return;
        }

        try {
            UploadRatData uploadRatData = new UploadRatData(addedRat);
            AsyncTask<String, Void, RatData> execute = uploadRatData.execute();
            addedRat = execute.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        backToMain.putExtra("rat", addedRat);
        startActivity(backToMain);
    }

}
