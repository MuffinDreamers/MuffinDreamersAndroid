package com.github.muffindreamers.rous.Auth0ManagementAPI;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by carty on 11/28/2017.
 */

public class UpdateUserTask extends AsyncTask<JSONObject, Void, Boolean> {

    private HttpsURLConnection connection;
    private URL updateUserURL;
    private String accessToken;

    public interface Listener {
        public void onFinished(boolean success);
    }

    private Listener listener;

    public UpdateUserTask(String userID, String accessToken) {
        try {
            updateUserURL = new URL("https://muffindreamers.auth0.com/api/v2/users/" + userID);
            this.accessToken = accessToken;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFinishedListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(JSONObject... jsonObjects) {
        if(jsonObjects.length != 1) {
            throw new RuntimeException("Must pass in one json object");
        }
        try {
            connection = (HttpsURLConnection) updateUserURL.openConnection();
            connection.setRequestMethod("PATCH");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStreamWriter sw = new OutputStreamWriter(connection.getOutputStream());
            sw.write(jsonObjects[0].toString());
            sw.flush();
            sw.close();

            int response = connection.getResponseCode();
            if (response == HttpsURLConnection.HTTP_OK) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        Log.d("Auth", "Update User Task Finished");

        if(listener != null) {
            listener.onFinished(success);
        }
    }
}
