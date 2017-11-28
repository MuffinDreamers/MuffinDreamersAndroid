package com.github.muffindreamers.rous.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.muffindreamers.rous.Auth0ManagementAPI.Auth0ManagementAPI;
import com.github.muffindreamers.rous.Auth0ManagementAPI.User;
import com.github.muffindreamers.rous.R;

import java.text.SimpleDateFormat;

/**
 * Created by carty on 11/27/2017.
 */

public class AdminPanel extends AppCompatActivity {

    private TextView name;
    private TextView email;
    private TextView verified;
    private TextView created;
    private TextView loginCount;
    private TextView lastIp;
    private TextView lastLogin;
    private TextView blockedText;

    private ArrayAdapter<String> autoCompleteAdapter;
    private AutoCompleteTextView searchBox;
    private Button blockButton;

    private Auth0ManagementAPI managementAPI;

    /**
     * Prepares the admin panel
     * @param savedInstanceState the instance data passed in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        name = (TextView) findViewById(R.id.admin_name_text);
        email = (TextView) findViewById(R.id.admin_email_text);
        verified = (TextView) findViewById(R.id.admin_verified_text);
        created = (TextView) findViewById(R.id.admin_created_text);
        loginCount = (TextView) findViewById(R.id.admin_count_text);
        lastIp = (TextView) findViewById(R.id.admin_ip_text);
        lastLogin = (TextView) findViewById(R.id.admin_login_text);
        blockedText = (TextView) findViewById(R.id.admin_blocked_text);

        findViewById(R.id.admin_search_button).setOnClickListener(this::searchHandler);
        findViewById(R.id.admin_back_button).setOnClickListener(this::backHandler);
        searchBox = (AutoCompleteTextView)findViewById(R.id.admin_search_box);
        searchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    autoCompleteAdapter.addAll(managementAPI.getEmails());
                }
            }
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchHandler(findViewById(R.id.admin_search_button));
            }
        });

        autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        searchBox.setAdapter(autoCompleteAdapter);

        blockButton = (Button)findViewById(R.id.admin_block_button);
        blockButton.setOnClickListener(this::blockHandler);

        managementAPI = new Auth0ManagementAPI();
    }

    /**
     * Searches for the specified user
     * @param v the view the button is located in
     */
    private void searchHandler(View v) {
        User user = managementAPI.getUser(searchBox.getText().toString());
        if(user != null)
            updateText(user);
    }

    /**
     * Block/unblock the specified user
     * @param v the view the button is located in
     */
    private void blockHandler(View v) {
        User user = managementAPI.getUser(searchBox.getText().toString());
        if(user != null) {
            managementAPI.setUpdatedUserListener(this::updateUserListener);
            if(user.isBlocked()) {
                managementAPI.unblockUser(user);

            }
            else {
                managementAPI.blockUser(user);
            }
        }
    }

    private void updateUserListener(boolean success) {
        if(success) {
            Toast.makeText(this, "Blocked status has been updated", Toast.LENGTH_SHORT).show();
            User user = managementAPI.getUser(searchBox.getText().toString());
            if(user != null) {
                user.setBlocked(!user.isBlocked());
                updateText(user);
            }
        }
        else {
            Toast.makeText(this, "Error updating  blocked status", Toast.LENGTH_LONG).show();
        }
    }

    private void updateText(User user) {
        name.setText(getResources().getText(R.string.admin_name_prefix) + user.getName());
        email.setText(getResources().getText(R.string.admin_email_prefix) + user.getEmail());
        verified.setText("" + getResources().getText(R.string.admin_verified_prefix) + user.isVerified());
        created.setText(getResources().getText(R.string.admin_created_prefix) + new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(user.getCreated()));
        loginCount.setText("" + getResources().getText(R.string.admin_login_count_prefix) + user.getLoginCount());
        lastIp.setText(getResources().getText(R.string.admin_last_ip_prefix) + user.getLastIp());
        lastLogin.setText(getResources().getText(R.string.admin_last_login_prefix) + new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(user.getLastLogin()));
        blockedText.setText("" + getResources().getText(R.string.admin_blocked_prefix) + user.isBlocked());

        blockButton.setText(user.isBlocked() ? "Unblock" : "Block");
    }

    /**
     * Returns the user to the Main Screen
     * @param v the view the button is located in
     */
    private void backHandler(View v) {
        Intent backToMain = new Intent(this, FetchRatDataActivity.class);
        Intent intent2 = getIntent();
        backToMain.putExtra("user", intent2.getSerializableExtra("user"));
        backToMain.putExtra("auth", true);
        startActivity(backToMain);
    }
}
