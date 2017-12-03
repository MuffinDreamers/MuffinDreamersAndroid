package com.github.muffindreamers.rous.controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by Brooke 10/1/17
 */
public class AddNewRatData extends AppCompatActivity {
    private Spinner locationTypeSpinner;
    private Spinner boroughSpinner;
    private EditText zipCodeText;
    private EditText cityText;
    private EditText addressText;
    private EditText latitudeText;
    private EditText longitudeText;

    private FusedLocationProviderClient locationClient;

    /**
     * Creates and takes in new rat data
     * @param savedInstanceState the instance data passed in
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_rat_data);

        locationTypeSpinner = (Spinner) findViewById(R.id.location_type_spinner);
        boroughSpinner = (Spinner) findViewById(R.id.borough_edit);
        zipCodeText = (EditText) findViewById(R.id.zipCode_edit);
        cityText = (EditText) findViewById(R.id.city_edit);
        addressText = (EditText) findViewById(R.id.address_edit);
        latitudeText = (EditText) findViewById(R.id.latitude_edit);
        longitudeText = (EditText) findViewById(R.id.longitude_edit);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, RatData.getLocationTypeArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationTypeSpinner.setAdapter(adapter);

        ArrayAdapter<Borough> boroughAdapter =
                new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, Borough.values());
        boroughAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boroughSpinner.setAdapter(boroughAdapter);

        Button cancel = (Button) findViewById(R.id.cancel_add_rat);
        cancel.setOnClickListener(this::cancelHandler);

        Button add_new_new = (Button) findViewById(R.id.add_new_new);
        add_new_new.setOnClickListener(this::newRatDataHandler);

        ((Button) findViewById(R.id.use_location)).setOnClickListener(this::useLocationHandler);

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getBoolean("autofill", false)) {
            autofillFromLocation();
        }
    }

    /**
     * Handles the user pressing the Use Location button
     * @param v the view the button is located in
     */
    private void useLocationHandler(View v) {
        autofillFromLocation();
    }

    /**
     * Automatically fills in textviews using the devices current location
     */
    private void autofillFromLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }

        Geocoder geocoder = new Geocoder(this);
        locationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    latitudeText.setText("" + location.getLatitude());
                    longitudeText.setText("" + location.getLongitude());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        if(addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            zipCodeText.setText(address.getPostalCode());
                            cityText.setText(address.getLocality());
                            addressText.setText(address.getSubThoroughfare() + " " + address.getThoroughfare());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: //Access to fine location
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    autofillFromLocation();
                }
                return;
        }
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
            Object locationItem = locationTypeSpinner.getSelectedItem();
            addedRat.setLocationType(locationItem.toString());
            Object boroughItem = boroughSpinner.getSelectedItem();
            addedRat.setBorough(boroughItem.toString());
            Editable zipCodeItem = zipCodeText.getText();
            addedRat.setZipCode((Integer.parseInt(zipCodeItem.toString())));
            Editable cityItem = cityText.getText();
            addedRat.setCity(cityItem.toString());
            Editable addressItem = addressText.getText();
            addedRat.setStreetAddress(addressItem.toString());
            Editable latitudeItem = latitudeText.getText();
            addedRat.setLatitude((Double.parseDouble(latitudeItem.toString())));
            Editable longitudeItem = longitudeText.getText();
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
