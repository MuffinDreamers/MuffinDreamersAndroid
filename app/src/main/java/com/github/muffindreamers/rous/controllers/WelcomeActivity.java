package com.github.muffindreamers.rous.controllers;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.auth0.android.request.Request;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.UserType;
import com.github.muffindreamers.rous.model.User;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * Creates welcome screen after user is authenticated
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_welcome);
        Button welcome = (Button) findViewById(R.id.welcome_button);
        welcome.setOnClickListener(this::onWelcomeButtonClick);
    }

    /**
     * Event when button on welcome screen is selected
     * @param view Screen for welcome screen
     */
    private void onWelcomeButtonClick(View view) {
        Auth0 auth0 = new Auth0(this);
        auth0.setOIDCConformant(true);
        WebAuthProvider.Builder init = WebAuthProvider.init(auth0);
        WebAuthProvider.Builder https = init.withScheme("https");
        WebAuthProvider.Builder builder = https.withAudience(String.format("https://%s/userinfo", 
                getString(R.string.com_auth0_domain)));
        builder.start(WelcomeActivity.this, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull Dialog dialog) {
                        // later 
                    }

                    @Override
                    public void onFailure(AuthenticationException exception) {
                        /*
                        Blocked:
                            code = "unauthorized"
                            description = "user is blocked"
                            statusCode = 0
                         */
                        if(exception.getStatusCode() == 0) { // User is banned
                            Log.d("Auth", "User is banned");
                            AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                            builder.setTitle("Unable to log in")
                                    .setMessage("Your account has been banned");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    }

                    @Override
                    public void onSuccess(@NonNull Credentials credentials) {
                        UsersAPIClient usersClient = new UsersAPIClient
                                (auth0, credentials.getIdToken());
                        AuthenticationAPIClient authClient = new
                                AuthenticationAPIClient(auth0);
                        assert (credentials.getAccessToken()) != null;
                        Request<UserProfile, AuthenticationException> userAuthenticationExceptionRequest =
                                authClient.userInfo((credentials.getAccessToken()));
                        userAuthenticationExceptionRequest.start(new BaseCallback<UserProfile,
                                        AuthenticationException>() {
                                    @Override
                                    public void onSuccess(UserProfile payload) {
                                       Log.d("Auth", "user id: " + payload
                                                       .getId());
                                       String userId = payload.getId();

                                        Request<UserProfile, ManagementException> profile = 
                                                usersClient.getProfile(userId);
                                        profile.start(new BaseCallback<UserProfile, 
                                                ManagementException>() {
                                                   @Override
                                                   public void onSuccess(UserProfile payload) {
                                                       String username = payload.getEmail();
                                                       Map<String, Object> userMetadata = payload
                                                               .getUserMetadata();
                                                       String name = (String) userMetadata
                                                               .get("full_name");
                                                       String perms = (String) userMetadata
                                                               .get("role");
                                                       Log.d("Auth", "User is " +
                                                               username + " with name: " + name +
                                                               "and permissions: " + perms);
                                                       Set<String> userData = userMetadata
                                                               .keySet();
                                                       Log.d("Auth", Arrays.toString
                                                               (userData.toArray()));

                                                       User user = new User(username, name,
                                                               credentials.getAccessToken(),
                                                               UserType.fromString(perms));

                                                       Intent toMain = new Intent
                                                                       (WelcomeActivity.this,
                                                                       FetchRatDataActivity.class);

                                                       toMain.putExtra("auth", true);
                                                       toMain.putExtra("user", user);
                                                       startActivity(toMain);
                                                   }

                                                   @Override
                                                   public void onFailure(ManagementException
                                                                                 error) {
                                                   }
                                               });
                                    }

                                    @Override
                                    public void onFailure
                                            (AuthenticationException error) {
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
