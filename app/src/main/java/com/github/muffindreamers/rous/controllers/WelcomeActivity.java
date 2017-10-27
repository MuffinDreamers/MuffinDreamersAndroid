package com.github.muffindreamers.rous.controllers;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.management.ManagementException;
import com.auth0.android.management.UsersAPIClient;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.Permissions;
import com.github.muffindreamers.rous.model.User;

import java.util.Arrays;


public class WelcomeActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_welcome);
        Button welcome = (Button) findViewById(R.id.welcomebutton);
        welcome.setOnClickListener(this::onWelcomeButtonClick);
    }

    public void onWelcomeButtonClick(View view) {
        Auth0 auth0 = new Auth0(this);
        auth0.setOIDCConformant(true);
        WebAuthProvider.init(auth0)
                .withScheme("https")
                .withAudience(String.format("https://%s/userinfo",
                        getString(R.string.com_auth0_domain)))
                .start(WelcomeActivity.this, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull Dialog dialog) {
                        // later
                    }

                    @Override
                    public void onFailure(AuthenticationException exception) {
                        // later
                    }

                    @Override
                    public void onSuccess(@NonNull Credentials credentials) {
                        UsersAPIClient usersClient = new UsersAPIClient
                                (auth0, credentials.getIdToken());
                        AuthenticationAPIClient authClient = new
                                AuthenticationAPIClient(auth0);
                        authClient.userInfo((credentials.getAccessToken()))
                                .start(new BaseCallback<UserProfile,
                                        AuthenticationException>() {
                                    @Override
                                    public void onSuccess(UserProfile payload) {
                                       Log.d("debug", "user id: " + payload
                                                       .getId());
                                       String userId = payload.getId();

                                       usersClient.getProfile(userId)
                                               .start(new BaseCallback<UserProfile, ManagementException>() {
                                                   @Override
                                                   public void onSuccess(UserProfile payload) {
                                                       String username = payload.getEmail();
                                                       String name = (String) payload
                                                               .getUserMetadata()
                                                               .get("full_name");
                                                       String perms = (String) payload
                                                               .getUserMetadata()
                                                               .get("role");
                                                       Log.d("user info", "User is " +
                                                               username + " with name: " + name +
                                                               "and permissions: " + perms);
                                                       Log.d("debug", Arrays.toString
                                                               (payload.getUserMetadata()
                                                                       .keySet().toArray()));

                                                       Intent toMain = new Intent
                                                                       (WelcomeActivity.this,
                                                                       FetchRatDataActivity.class);

                                                       toMain.putExtra("auth", true);
                                                       User user = new User(username, name,
                                                               credentials.getAccessToken(),
                                                               Permissions.fromString(perms));
                                                       toMain.putExtra("user", user);
                                                       startActivity(toMain);
                                                   }

                                                   @Override
                                                   public void onFailure(ManagementException error) {
                                                   }
                                               });
                                    }

                                    @Override
                                    public void onFailure
                                            (AuthenticationException error) {
                                        // later
                                    }
                                });

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
