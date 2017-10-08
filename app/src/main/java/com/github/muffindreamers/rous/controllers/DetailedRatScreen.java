package com.github.muffindreamers.rous.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.github.muffindreamers.rous.R;

public class DetailedRatScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_rat_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button returnButton = (Button) findViewById(R.id.return_to_main);
        returnButton.setOnClickListener(this::returnHandler);
    }

    public void returnHandler(View v) {
        Intent backToMain = new Intent(DetailedRatScreen.this, FetchRatDataActivity.class);
        backToMain.putExtra("auth", true);
        startActivity(backToMain);
    }
}
