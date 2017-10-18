package com.github.muffindreamers.rous.model;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.github.muffindreamers.rous.model.RatData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Brooke on 10/7/2017.
 */

public class

RetrieveRatData extends AsyncTask<String, Void, ArrayList<RatData>>  {
    ArrayList<RatData> ratList= new ArrayList<>();

    /**
     * Pulls the RatData information from the SQL database
     * and creates an arrayList of RatData objects
     * @param params the items needed to parse the json objects from the database
     * @return the ArrayList of RatData objects
     */
    @Override
    protected ArrayList<RatData> doInBackground(String... params) {
        URL githubEndpoint = null;
        try {
            githubEndpoint = new URL("https://muffindreamers.azurewebsites.net/sightings");
            // Create connection
            HttpsURLConnection myConnection =
                    (HttpsURLConnection) githubEndpoint.openConnection();
            myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
            if (myConnection.getResponseCode() == 200) {
                // Success
                // Further processing here
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginArray(); // Start processing the JSON array
                while (jsonReader.hasNext()) { // Loop through all keys
                    jsonReader.beginObject();
                    RatData rat = new RatData();
                    while(jsonReader.hasNext() ) {
                        String key = jsonReader.nextName(); // Fetch the next key
                        if (key.equals("id")) { // Check if desired key
                            // Fetch the value as a String
                            int value = jsonReader.nextInt();
                            rat.setId(value);
                            // Do something with the value
                            // ...

                            // Break out of the loop
                        } else if (key.equals("dateCreated")) {
                            String value = jsonReader.nextString();
                            SimpleDateFormat formatter1= new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss");
                            Date date1 = formatter1.parse(value);
                            rat.setDateCreated(date1);
                        } else if (key.equals("locationType")) {
                            String value = jsonReader.nextString();
                            rat.setLocationType(value);
                        } else if (key.equals("zipcode")) {
                            int value = jsonReader.nextInt();
                            rat.setZipCode(value);
                        } else if (key.equals("streetAddress")) {
                            String value = jsonReader.nextString();
                            rat.setStreetAddress(value);
                        } else if (key.equals("city")) {
                            String value = jsonReader.nextString();
                            rat.setCity(value);
                        } else if (key.equals("latitude")) {
                            double value = jsonReader.nextDouble();
                            rat.setLatitude(value);
                        } else if (key.equals("longitude")) {
                            double value = jsonReader.nextDouble();
                            rat.setLongitude(value);
                        } else if (key.equals("borough")) {
                            String value = jsonReader.nextString();
                            rat.setBorough(value);
                        } else {
                            jsonReader.skipValue(); // Skip values of other keys
                        }
                    }
                    ratList.add(rat);
                    jsonReader.endObject();
                }
                jsonReader.endArray();
                jsonReader.close();
                myConnection.disconnect();
            } else {
                // Error handling code goes here
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ratList;
    }

}
