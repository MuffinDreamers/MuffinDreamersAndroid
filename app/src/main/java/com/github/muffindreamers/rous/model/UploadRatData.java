package com.github.muffindreamers.rous.model;

import android.os.AsyncTask;
import android.util.Log;

import com.github.muffindreamers.rous.model.RatData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by Brooke on 10/12/2017.
 */

public class UploadRatData extends AsyncTask<String, Void, RatData> {
    private RatData addRat = new RatData();

    @Override
    protected RatData doInBackground(String... params) {
        URL githubEndpoint = null;
        try {
            JSONObject addedRatJson = new JSONObject();
            addedRatJson.put("id", addRat.getId());
            SimpleDateFormat formatter1= new SimpleDateFormat("YYYY-MM-dd'T'HH:MM:SS");
            String date1 = formatter1.format(addRat.getDateCreated());
            addedRatJson.put("dateCreated", date1);
            addedRatJson.put("locationType", addRat.getLocationType());
            addedRatJson.put("zipCode", addRat.getZipCode());
            addedRatJson.put("streetAddress", addRat.getStreetAddress());
            addedRatJson.put("city", addRat.getCity());
            addedRatJson.put("borough", addRat.getBorough());
            addedRatJson.put("latitude", addRat.getLatitude());
            addedRatJson.put("longitude", addRat.getLongitude());
            githubEndpoint = new URL("http://muffindreamers.azurewebsites.net/sightings/report");
            // Create connection
            //http://muffindreamers.azurewebsites.net/sightings/report
            //https://requestb.in/rvpop9rv

            HttpURLConnection myConnection =
                    (HttpURLConnection) githubEndpoint.openConnection();
            myConnection.setRequestMethod("POST");
            myConnection.setChunkedStreamingMode(0);
            myConnection.setDoOutput(true);
            myConnection.setRequestProperty("Content-Type", "application/json");
            myConnection.setRequestProperty("Accept", "application/json");
            myConnection.connect();
            OutputStream out = myConnection.getOutputStream();
            out.write(addedRatJson.toString().getBytes());
            out.flush();
            int connect = myConnection.getResponseCode();
            if (connect == 201 || connect == 200) {
                // Success
                // Further processing here
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        myConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.e("fuckingsuccessmodefam", response.toString());
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        myConnection.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.e("fuckingerrormodefam", response.toString());
            }
            out.close();
            myConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addRat;
    }

    @Override
    protected void onPostExecute(RatData ratData) {
        super.onPostExecute(ratData);
    }
}
