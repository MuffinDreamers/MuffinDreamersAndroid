package com.github.muffindreamers.rous.controllers;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
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
import com.github.muffindreamers.rous.model.User;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import static android.R.id.message;

public class AddNewRatData extends AppCompatActivity {
    private User user = null;
    private RatData addedRat;
    private Spinner locationType;
    private Spinner borough;
    private EditText zipcode;
    private EditText city;
    private EditText address;
    private EditText latitude;
    private EditText longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        User user = (User) extras.getSerializable("user");
        setContentView(R.layout.activity_add_new_rat_data);
        addedRat = new RatData();

        locationType = (Spinner) findViewById(R.id.location_type_spinner);
        borough = (Spinner) findViewById(R.id.borough_edit);
        zipcode = (EditText) findViewById(R.id.zipcode_edit);
        city = (EditText) findViewById(R.id.city_edit);
        address = (EditText) findViewById(R.id.address_edit);
        latitude = (EditText) findViewById(R.id.latitude_edit);
        longitude = (EditText) findViewById(R.id.longitude_edit);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, RatData.locationTypeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationType.setAdapter(adapter);

        ArrayAdapter<Borough> boroughAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, Borough.values());
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
    public void cancelHandler(View v) {
        Intent backToMain = new Intent(AddNewRatData.this, FetchRatDataActivity.class);
        backToMain.putExtra("user", getIntent().getSerializableExtra("user"));
        backToMain.putExtra("auth", true);
        //REMOVE LATER - ONCE DATABASE IS WORKING
        backToMain.putExtra("ratlist", getIntent().getSerializableExtra("ratlist"));
        startActivity(backToMain);
    }

    /**
     * Adds a new rat and sends the user to the
     * Main Screen
     * @param v the view the button is located in
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void newRatDataHandler(View v) {
        Intent backToMain = new Intent(this, FetchRatDataActivity.class);
        backToMain.putExtra("user", getIntent().getSerializableExtra("user"));
        backToMain.putExtra("auth", true);
        //REMOVE LATER - ONCE DATABASE IS WORKING
        backToMain.putExtra("ratlist", getIntent().getSerializableExtra("ratlist"));

        try {
            Random rand = new Random();
            int n = rand.nextInt(500) + 37018500;
            addedRat.setId(n);
            addedRat.setLocationType((String) locationType.getSelectedItem());
            addedRat.setBorough(borough.getSelectedItem().toString());
            addedRat.setZipCode((Integer.parseInt(zipcode.getText().toString())));
            addedRat.setCity(city.getText().toString());
            addedRat.setStreetAddress(address.getText().toString());
            addedRat.setLatitude((Double.parseDouble(latitude.getText().toString())));
            addedRat.setLongitude((Double.parseDouble(longitude.getText().toString())));
        } catch(RuntimeException ex) {
            Log.e("AddNewRatData", "Parse error", ex);
            return;
        }


        try {
            addedRat = new UploadRatData().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        backToMain.putExtra("rat", addedRat);
        startActivity(backToMain);
    }

}
