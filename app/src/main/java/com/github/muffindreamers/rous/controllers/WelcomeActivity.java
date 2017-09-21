package com.github.muffindreamers.rous.controllers;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.github.muffindreamers.rous.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViewById(R.id.login_button).setOnClickListener
                (this::onLoginClick);
    }

    private void onLoginClick(View v) {
        Log.d("method_tracker","Clicked on Login button");
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

            if (username_field.getText().equals("user") &&
                    password_field.getText().equals("pass")) {
                // TODO(m3rcuriel) make all the changes that touch the model
                login_dialog.dismiss();
            } else {
                username_field.setError("Incorrect username or password!");
                password_field.setText("");
            }
        });


        login_dialog.show();
    }
}
