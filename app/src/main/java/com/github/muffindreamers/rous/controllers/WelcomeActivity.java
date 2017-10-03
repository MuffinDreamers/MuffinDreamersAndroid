package com.github.muffindreamers.rous.controllers;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
        findViewById(R.id.register_button).setOnClickListener
                (this::onRegisterClick);
    }

    protected void onRegisterClick(View v) {
        Dialog registration_dialog = new Dialog(this);
        registration_dialog.setContentView(R.layout.registration_dialog);
        Button cancel = registration_dialog.findViewById(R.id.cancel_registration_button);
        cancel.setOnClickListener((View vi) -> registration_dialog.cancel());

        Button registration = registration_dialog.findViewById(R.id.register_button);
        registration.setOnClickListener(view -> {
            EditText email_field = (EditText) registration_dialog
                    .findViewById(R.id.email_reg_field);
            EditText password_field = (EditText) registration_dialog
                    .findViewById(R.id.password_reg_field);
            CheckBox admin_field = (CheckBox) registration_dialog
                    .findViewById(R.id.admin_checkbox);
            boolean checked = admin_field.isChecked();
            Log.d("debug", "user is " + email_field.getText() +
                    " password is " + password_field.getText() + " and is an admin:"
                   + checked );
            System.out.print("user is " + email_field.getText() +
                    " password is " + password_field.getText() + " and is an admin:"
                   + checked );
            registration_dialog.dismiss();
/*
            if ( //some validation credit) {

            } else {
                username_field.setError("Invalid email or password!");
                password_field.setText("");
            }
            */
        });

        registration_dialog.show();
    }

    protected void onLoginClick(View v) {
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
                        .findViewById(R.id.email_registration_field);
                EditText password_field = (EditText) login_dialog
                        .findViewById(R.id.password_registration_field);
                Log.d("debug", "user is " + username_field.getText() +
                        " password is " + password_field.getText());
                //add other validation logic
                if (username_field.getText().toString().equals("user") &&
                        password_field.getText().toString().equals("pass")) {
                    loggedInUser = new User(username_field.getText().toString(),
                            password_field.getText().toString(),Permissions.USER);
                    ((Button) v).setText("Logout");
                    Button register = (Button) this.findViewById(R.id
                            .register_button);
                    register.setVisibility(View.INVISIBLE);
                    register.setEnabled(false);
                    login_dialog.dismiss();
                } else {
                    username_field.setError("Incorrect email or password!");
                    password_field.setText("");
                }
            });


            login_dialog.show();
        } else {
            Log.d("method_tracker","Logged In User is not null");
            loggedInUser = null;
            Button register = (Button) this.findViewById(R.id
                    .register_button);
            register.setVisibility(View.VISIBLE);
            register.setEnabled(true);
            ((Button) v).setText("Login");
        }
    }
}
