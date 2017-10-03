package com.github.muffindreamers.rous.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.User;

public class MainActivity extends Activity {
    private User user = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        this.setContentView(R.layout.activity_main);
        TextView info = (TextView) findViewById(R.id.main_text);
        info.setText("Username: " + user.getEmail() + "\n" +
            "Full name: " + user.getFullname() + "\n" +
            "Permissions Level: " + user.getPermissions());

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this::logoutHandler);
    }

    public void logoutHandler(View v) {
        user = null;
        Intent backToWelcome = new Intent(this, WelcomeActivity.class);
        startActivity(backToWelcome);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
