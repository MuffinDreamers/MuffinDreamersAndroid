package com.github.muffindreamers.rous.Auth0ManagementAPI;

import android.os.AsyncTask;

import com.github.muffindreamers.rous.model.Credentials;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by carty on 11/28/2017.
 */

public class GetAccessTokenTask extends AsyncTask<Void, Void, String> {

    public interface Listener {
        public void onFinished(String accessToken);
    }

    private final Listener listener;

    private HttpsURLConnection connection;
    private URL tokenURL;

    public GetAccessTokenTask(Listener listener) {
        try {
            tokenURL = new URL("https://muffindreamers.auth0.com/oauth/token");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... objects) {
        String accessKey;
        try {
            connection = (HttpsURLConnection) tokenURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject data = new JSONObject();
            data.put("grant_type", "client_credentials");
            data.put("client_id", Credentials.auth0ClientId);
            data.put("client_secret", Credentials.auth0ClientSecret);
            data.put("audience", "https://muffindreamers.auth0.com/api/v2/");

            OutputStreamWriter sw = new OutputStreamWriter(connection.getOutputStream());
            sw.write(data.toString());
            sw.flush();
            sw.close();

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

            JSONObject output = new JSONObject(sb.toString());
            accessKey = output.getString("access_token");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return accessKey;
    }

    @Override
    protected void onPostExecute(String accessToken) {
        super.onPostExecute(accessToken);

        if(listener != null) {
            listener.onFinished(accessToken);
        }
    }
}
