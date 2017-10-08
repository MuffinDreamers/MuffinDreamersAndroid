package com.github.muffindreamers.rous.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.RatData;
import com.github.muffindreamers.rous.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;


public class FetchRatDataActivity extends Activity {
    private User user = null;
    ListView listView ;
    ArrayList<RatData> ratList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        boolean auth = extras.getBoolean("auth");
        if (!auth) {
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        User user = (User) extras.getSerializable("user");
        if (user == null) {
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        this.user = user;
        try {
            ratList = new RetrieveRatData().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_fetch_rat_data);
        listView = (ListView) findViewById(R.id.list);

        ArrayAdapter<RatData> adapter = new ArrayAdapter<RatData>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, ratList);

        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        // ListView Item Click Listener

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RatData rat = adapter.getItem(position);
                Toast.makeText(getApplicationContext(), rat.toString(),
                        Toast.LENGTH_LONG).show();
                Intent toDetailedScreen = new Intent
                        (FetchRatDataActivity.this,
                                DetailedRatScreen.class);
                toDetailedScreen.putExtra("rat", rat);
                startActivity(toDetailedScreen);
            }

        });
        Button logout = (Button) findViewById(R.id.logout_main);
        logout.setOnClickListener(this::logoutHandler);
    }

    public void logoutHandler(View v) {
        user = null;
        Intent backToWelcome = new Intent(this, WelcomeActivity.class);
        startActivity(backToWelcome);
    }

}

