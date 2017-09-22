package com.github.muffindreamers.rous.controllers;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.Permissions;
import com.github.muffindreamers.rous.model.User;

public class WelcomeActivity extends AppCompatActivity {

    private User loggedInUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViewById(R.id.login_button).setOnClickListener
                (this::onLoginClick);
    }

    private void onLoginClick(View v) {
        Log.d("method_tracker","Clicked on Login button");
        if (loggedInUser == null) {
            Log.d("method_tracker","Logged In User is null");
            Dialog login_dialog = new Dialog(this);
            login_dialog.setContentView(R.layout.login_dialog);

            Button cancel = login_dialog.findViewById(R.id.cancel_button);
            cancel.setOnClickListener((View vi) -> login_dialog.cancel());

            Button login = login_dialog.findViewById(R.id.login_button);
            login.setOnClickListener(view -> {
                EditText username_field = (EditText) login_dialog
                        .findViewById(R.id.username_field);
                EditText password_field = (EditText) login_dialog
                        .findViewById(R.id.password_field);
                Log.d("debug", "user is " + username_field.getText() +
                        " password is " + password_field.getText());

                if (username_field.getText().toString().equals("user") &&
                        password_field.getText().toString().equals("pass")) {
                    loggedInUser = new User(username_field.getText().toString(),
                            Permissions.USER);
                    ((Button) v).setText("Logout");
                    login_dialog.dismiss();
                } else {
                    username_field.setError("Incorrect username or password!");
                    password_field.setText("");
                }
            });


            login_dialog.show();
        } else {
            Log.d("method_tracker","Logged In User is not null");
            loggedInUser = null;
            ((Button) v).setText("Login");
        }
    }
}
