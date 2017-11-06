package com.github.muffindreamers.rous.model;

import android.os.AsyncTask;
import android.util.Log;

import com.github.muffindreamers.rous.model.RatData;

import org.json.JSONException;
import org.json.JSONObject;

/*import java.io.BufferedOutputStream;*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Brooke on 10/7/2017.
 * @author Brooke White
 */

public class UploadRatData extends AsyncTask<String, Void, RatData> {
    private static final int INT = 201;
    private static final int INT1 = 200;
    private  RatData addRat;

    /**
     * Override AsyncTask constructor
     * @param addRat the rat to be uploaded to the database
     */
    public UploadRatData(RatData addRat) {
        super();
        this.addRat = addRat;
    }


    /**
     * Uploads rat data to an SQL server
     * @param params the parameters required for the asyncTask
     * @return the new rat created
     */
    @Override
    protected RatData doInBackground(String... params) {
        URL gitHubEndpoint;
        try {
            JSONObject addedRatJson = new JSONObject();
            addedRatJson.put("id", addRat.getId());
            SimpleDateFormat formatter1= new SimpleDateFormat("YYYY-MM-dd'T'HH:MM:SS",
                    Locale.ENGLISH);
            String date1 = formatter1.format(addRat.getDateCreated());
            addedRatJson.put("dateCreated", date1);
            addedRatJson.put("locationType", addRat.getLocationType());
            addedRatJson.put("zipcode", addRat.getZipCode());
            addedRatJson.put("streetAddress", addRat.getStreetAddress());
            addedRatJson.put("city", addRat.getCity());
            addedRatJson.put("borough", addRat.getBorough());
            addedRatJson.put("latitude", addRat.getLatitude());
            addedRatJson.put("longitude", addRat.getLongitude());
            gitHubEndpoint = new URL("http://muffindreamers.azurewebsites.net/sightings/report");
            // Create connection
            //http://muffindreamers.azurewebsites.net/sightings/report
            //https://requestb.in/rvpop9rv

            HttpURLConnection myConnection =
                    (HttpURLConnection) gitHubEndpoint.openConnection();
            myConnection.setRequestMethod("POST");
            myConnection.setChunkedStreamingMode(0);
            myConnection.setDoOutput(true);
            myConnection.setRequestProperty("Content-Type", "application/json");
            myConnection.setRequestProperty("Accept", "application/json");
            myConnection.connect();
            OutputStream out = myConnection.getOutputStream();
            String rat_to_string = addedRatJson.toString();
            out.write(rat_to_string.getBytes());
            out.flush();
            int connect = myConnection.getResponseCode();
            if ((connect == INT) || (connect == INT1)) {
                // Success
                // Further processing here
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        myConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.e("fuckingSuccessModeFam", response.toString());
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        myConnection.getErrorStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.e("FuckingErrorModeFam", response.toString());
            }
            out.close();
            myConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return addRat;
    }

    @Override
    protected void onPostExecute(RatData ratData) {
        super.onPostExecute(ratData);
    }


}
