package com.github.muffindreamers.rous.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.RatData;
import com.github.muffindreamers.rous.model.User;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Brooke on 10/7/2017.
 */

public class FetchRatDataActivity extends Activity {
    private User user = null;
    ListView listView ;
    ArrayList<RatData> ratList= new ArrayList<>();

    /**
     * Creates the main ListView Screen
     * @param savedInstanceState the instance data passed in
     */
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
                //Toast.makeText(getApplicationContext(), rat.toString(),
                        //Toast.LENGTH_LONG).show();
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
    }

    /**
     * Returns the user to the Welcome Screen
     * @param v the view the button is located in
     */
    public void logoutHandler(View v) {
        user = null;
        Intent backToWelcome = new Intent(this, WelcomeActivity.class);
        startActivity(backToWelcome);
    }

}

