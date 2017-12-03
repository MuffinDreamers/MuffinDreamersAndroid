package com.github.muffindreamers.rous.Auth0ManagementAPI;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Will Carty on 11/28/2017.
 * Requests the complete list of users from the Auth0 Management API using an access token
 */

public class GetUserListTask extends AsyncTask<String, Void, User[]> {

    /**
     * Interface for a callback listener
     */
    public interface Listener {
        /**
         * Called when the task completes
         * @param users the array of users returned from the server
         */
        public void onFinished(User[] users);
    }

    private final Listener listener;

    HttpsURLConnection connection;
    URL userListURL;

    /**
     * Constructs the task, automatically creating the URL to access and setting the callback listener
     * @param listener The listener function to call on completion of the task. Can be null
     */
    public GetUserListTask(Listener listener) {
        try {
            this.listener = listener;
            userListURL = new URL("https://muffindreamers.auth0.com/api/v2/users");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates and configures the GET request, reads the response and deserializes the JSON into User objects
     * @param strings One string, an unexpired access token
     * @return the array of users returned by the server
     */
    @Override
    protected User[] doInBackground(String... strings) {
        if(strings.length != 1)
            throw new RuntimeException("Must pass in an access token");
        ArrayList<User> userList;
        try {
            connection = (HttpsURLConnection) userListURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + strings[0]);

            StringBuilder sb = new StringBuilder();

            int response = connection.getResponseCode();
            if (response == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                connection.disconnect();
            } else {
                throw new RuntimeException("Could not authorize Auth0 Management API");
            }

            JSONArray arr = new JSONArray(sb.toString());
            userList = new ArrayList<>(arr.length());
            for(int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject u = arr.getJSONObject(i);
                    String userId = u.getString("user_id");
                    String name = u.getJSONObject("user_metadata").getString("full_name");
                    String email = u.getString("email");
                    boolean verified = u.getBoolean("email_verified");
                    Date created = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(u.getString("created_at"));
                    int loginCount = u.getInt("logins_count");
                    String lastIp = u.getString("last_ip");
                    Date lastLogin = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(u.getString("last_login"));
                    boolean blocked = (u.has("blocked") && u.getBoolean("blocked"));
                    userList.add(new User(userId, name, email, verified, created, loginCount, lastIp, lastLogin, blocked));
                } catch (Exception e) {
                    Log.e("Auth", e.getMessage());
                    continue;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userList.toArray(new User[0]);
    }

    /**
     * Calls the callback listener, passing on the array of users
     * @param users the array of users returned by the server
     */
    @Override
    protected void onPostExecute(User[] users) {
        super.onPostExecute(users);

        if(listener != null) {
            listener.onFinished(users);
        }
    }
}
