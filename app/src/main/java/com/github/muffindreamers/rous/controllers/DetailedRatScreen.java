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
        idTextView.setText("Rat Street Address: " + rat.getStreetAddress());
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
        startActivity(backToMain);
    }
}
