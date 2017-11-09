package com.github.muffindreamers.rous.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.RatData;
import com.github.muffindreamers.rous.model.RetrieveRatData;
import com.github.muffindreamers.rous.model.User;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Brooke on 10/16/2017.
 * @author Brooke White
 */

public class FetchRatDataActivity extends Activity {
    private User user = null;
    private ArrayList<RatData> ratList;
    private boolean auth;

    /**
     * Creates the main ListView Screen
     * @param savedInstanceState the instance data passed in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        assert extras != null;
        auth = extras.getBoolean("auth");
        if (!auth) {
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        User user = (User) extras.getSerializable("user");
        if (user == null) {
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        this.user = user;

        try {
            RetrieveRatData retrieveRatData = new RetrieveRatData();
            AsyncTask<String, Void, ArrayList<RatData>> execute = retrieveRatData.execute();
            ratList = execute.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_fetch_rat_data);
        ListView listView = (ListView) findViewById(R.id.list);

        ArrayAdapter<RatData> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, ratList);

        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        // ListView Item Click Listener

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * This method takes you to the detailed screen
             * for particular RatData Object
             * @param parent the adapter view the item is in
             * @param view of the adapter view was in
             * @param position the location of the item
             * @param id the row id of the item that was clicked
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RatData rat = adapter.getItem(position);
                Intent toDetailedScreen = new Intent
                        (FetchRatDataActivity.this,
                                DetailedRatScreen.class);
                toDetailedScreen.putExtra("rat", rat);
                toDetailedScreen.putExtra("user", user);
                startActivity(toDetailedScreen);
            }

        });
        Button logout = (Button) findViewById(R.id.logout_main);
        logout.setOnClickListener(this::logoutHandler);

        Button add_new = (Button) findViewById(R.id.add_new_main);
        add_new.setOnClickListener(this::newRatDataHandler);

        Button map_button = (Button) findViewById(R.id.map_button);
        map_button.setOnClickListener(this::newMapHandler);

        Button graph_button = (Button) findViewById(R.id.graph_button);
        graph_button.setOnClickListener(this::newGraphHandler);

    }


    /**
     * to go to the graph
     * @param v the graph view
     */
    private void newGraphHandler(View v) {
        Intent toNewGraphScreen = new Intent(this, GraphRatData.class);
        toNewGraphScreen.putExtra("user", user);
        toNewGraphScreen.putExtra("auth", auth);
        toNewGraphScreen.putExtra("ratList", ratList);
        startActivity(toNewGraphScreen);
    }

    /**
     * to go to the map screen
     * @param v a view to go to the new map
     */
    private void newMapHandler(View v) {
        Intent toNewMapScreen = new Intent(this, MapRatDataActivity.class);
        toNewMapScreen.putExtra("user", user);
        toNewMapScreen.putExtra("auth", auth);
        startActivity(toNewMapScreen);
    }

    /**
     * Returns the user to the Welcome Screen
     * @param v the view the button is located in
     */
    private void logoutHandler(View v) {
        /*user = null*/
        Intent backToWelcome = new Intent(this, WelcomeActivity.class);
        startActivity(backToWelcome);
    }

    /**
     * Sends the user to the add new rat screen
     * @param v the view the button is located in
     */
    private void newRatDataHandler(View v) {
        Intent toNewRatDataScreen = new Intent(this, AddNewRatData.class);
        toNewRatDataScreen.putExtra("user", user);
        //REMOVE LATER - ONCE DATABASE IS FIXED
        //toNewRatDataScreen.putExtra("ratList", ratList);
        startActivity(toNewRatDataScreen);
    }
}
