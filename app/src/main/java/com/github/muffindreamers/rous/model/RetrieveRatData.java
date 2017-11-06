package com.github.muffindreamers.rous.model;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.github.muffindreamers.rous.model.RatData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Brooke on 10/7/2017.
 * @author Brooke
 */

public class

RetrieveRatData extends AsyncTask<String, Void, ArrayList<RatData>>  {
    private static final int INT = 200;
    private ArrayList<RatData> ratList= new ArrayList<>();

    /**
     * Pulls the RatData information from the SQL database
     * and creates an arrayList of RatData objects
     * @param params the items needed to parse the json objects from the database
     * @return the ArrayList of RatData objects
     */
    @Override
    protected ArrayList<RatData> doInBackground(String... params) {
        URL gitHubEndpoint;
        try {
            gitHubEndpoint = new URL("https://muffindreamers.azurewebsites.net/sightings");
            // Create connection
            HttpsURLConnection myConnection =
                    (HttpsURLConnection) gitHubEndpoint.openConnection();
            myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
            if (myConnection.getResponseCode() == INT) {
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
                        if ("id".equals(key)) { // Check if desired key
                            // Fetch the value as a String
                            int value = jsonReader.nextInt();
                            rat.setId(value);
                            // Do something with the value
                            // ...

                            // Break out of the loop
                        } else if ("dateCreated".equals(key)) {
                            String value = jsonReader.nextString();
                            SimpleDateFormat formatter1=
                                    new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss",
                                            Locale.ENGLISH);
                            Date date1 = formatter1.parse(value);
                            rat.setDateCreated(date1);
                        } else if ("locationType".equals(key)) {
                            String value = jsonReader.nextString();
                            rat.setLocationType(value);
                        } else if ("zipCode".equals(key)) {
                            int value = jsonReader.nextInt();
                            rat.setZipCode(value);
                        } else if ("streetAddress".equals(key)) {
                            String value = jsonReader.nextString();
                            rat.setStreetAddress(value);
                        } else if ("city".equals(key)) {
                            String value = jsonReader.nextString();
                            rat.setCity(value);
                        } else if ("latitude".equals(key)) {
                            double value = jsonReader.nextDouble();
                            rat.setLatitude(value);
                        } else if ("longitude".equals(key)) {
                            double value = jsonReader.nextDouble();
                            rat.setLongitude(value);
                        } else if ("borough".equals(key)) {
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
                System.out.print("An Error Has Occurred :(");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return ratList;
    }

}
