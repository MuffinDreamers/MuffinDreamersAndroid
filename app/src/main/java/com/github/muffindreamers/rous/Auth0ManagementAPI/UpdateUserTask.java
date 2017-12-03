package com.github.muffindreamers.rous.Auth0ManagementAPI;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Will Carty on 11/28/2017.
 * Sends a request to update some data of a User
 */

public class UpdateUserTask extends AsyncTask<JSONObject, Void, Boolean> {

    private HttpsURLConnection connection;
    private URL updateUserURL;
    private String accessToken;

    /**
     * Interface for a callback listener
     */
    public interface Listener {
        /**
         * Called when the task completes
         * @param success whether or not the task was successful
         */
        public void onFinished(boolean success);
    }

    private Listener listener;

    /**
     * Constructs the task, automatically creating the URL to access
     * @param userID The ID of the user to update
     * @param accessToken an unexpired access token
     */
    public UpdateUserTask(String userID, String accessToken) {
        try {
            updateUserURL = new URL("https://muffindreamers.auth0.com/api/v2/users/" + userID);
            this.accessToken = accessToken;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the callback listener
     * @param listener the function to call when the task finishes
     */
    public void setFinishedListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Creates and configures the PATCH request to update the user
     * @param jsonObjects One JSONObject containing the updated data
     *                    Example: {"blocked":true}     To set the blocked status to true
     * @return whether or not the request succeeded
     */
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

    /**
     * Calls the callback listener, passing on the success status
     * @param success whether or not the task succeeded
     */
    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        Log.d("Auth", "Update User Task Finished");

        if(listener != null) {
            listener.onFinished(success);
        }
    }
}
