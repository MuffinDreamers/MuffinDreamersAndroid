package com.github.muffindreamers.rous.controllers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.RatData;
import com.github.muffindreamers.rous.model.RetrieveRatData;
import com.github.muffindreamers.rous.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Brooke on 10/7/2017.
 */

public class MapRatDataActivity extends Activity {
    private User user = null;
    private ListView listView ;
    private ArrayList<RatData> ratList;
    private HashMap<Integer, Marker> markersOnMap = new HashMap<>();
    private GoogleMap map;

    private final DateFormat df = new SimpleDateFormat("dd/MM/YY h:m a", Locale.US);

    private Date startdate, enddate;

    /**
     * Creates the main ListView Screen
     * @param savedInstanceState the instance data passed in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        boolean auth = extras.getBoolean("auth");
        if (!auth) {
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        User user = (User) extras.getSerializable("user");
        if (user == null) {
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        this.user = user;

            try {
                ratList = new RetrieveRatData().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        setContentView(R.layout.activity_map_screen);

        Button start = (Button) findViewById(R.id.start_date);
        start.setOnClickListener((v) -> startHandler(v));

        Button end = (Button) findViewById(R.id.end_date);
        end.setOnClickListener((v) -> endHandler(v));

        if (findViewById(R.id.map_frame_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            MapFragment mapFragment = new MapFragment();

            // Handle map loading
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {

                            LinearLayout info = new LinearLayout(getApplicationContext());
                            info.setOrientation(LinearLayout.VERTICAL);

                            TextView title = new TextView(getApplicationContext());
                            title.setTextColor(Color.BLACK);
                            title.setGravity(Gravity.CENTER);
                            title.setTypeface(null, Typeface.BOLD);
                            title.setText(marker.getTitle());

                            TextView snippet = new TextView(getApplicationContext());
                            snippet.setTextColor(Color.GRAY);
                            snippet.setText(marker.getSnippet());

                            info.addView(title);
                            info.addView(snippet);

                            return info;
                        }
                    });
                    LatLng coordinates = new LatLng(40.730610, -73.935242);
                    updateMapMarkers(googleMap);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10));
                }
            });

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            mapFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.map_frame_container, mapFragment).commit();
        }
    }

    /**
     * Starts the start date dialog
     *
     * @param v the view
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startHandler(View v) {
        DatePickerDialog newFragment = new DatePickerDialog(MapRatDataActivity.this);
        newFragment.setOnDateSetListener(new StartDatePickerDialog());
        newFragment.show();
    }

    /**
     * Starts the end date dialog
     *
     * @param v the view
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void endHandler(View v) {
        DatePickerDialog newFragment = new DatePickerDialog(MapRatDataActivity.this);
        newFragment.setOnDateSetListener(new EndDatePickerDialog());
        newFragment.show();
    }

    /**
     * Returns the user to the Welcome Screen
     * @param v the view the button is located in
     */
    public void logoutHandler(View v) {
        user = null;
        Intent backToWelcome = new Intent(this, WelcomeActivity.class);
        startActivity(backToWelcome);
    }

    /**
     * Sends the user to the add new rat screen
     * @param v the view the button is located in
     */
    public void newRatDataHandler(View v) {
        Intent toNewRatDataScreen = new Intent(this, AddNewRatData.class);
        toNewRatDataScreen.putExtra("user", user);
        //REMOVE LATER - ONCE DATABASE IS FIXED
        //toNewRatDataScreen.putExtra("ratlist", ratList);
        startActivity(toNewRatDataScreen);
    }

    /**
     * Refresh the markers on the map to match a set
     *
     * @param rat_idxs the set of indexes
     * @param map the map
     */
    private void refreshMapMarkerMap(Set<Integer> rat_idxs, GoogleMap map) {
        for (int i : rat_idxs) {
            if (!markersOnMap.containsKey(i)) {
                Optional<RatData> data = ratList.stream()
                        .filter((d) -> d.getId() == i).findFirst();
                if (data.isPresent()) {
                    RatData rat = data.get();
                    Log.d("rat-shit", "" + rat.getId());
                    LatLng coords = new LatLng(rat.getLatitude(), rat.getLongitude());
                    markersOnMap.put(i, map.addMarker(new MarkerOptions().position(coords)
                            .title("Rat " + rat.getId())
                            .snippet("Spotted: " + rat.getLocationType() + "\n" +
                                    "Date: " + df.format(rat.getDateCreated()))));
                }
            }
        }

        Set<Integer> remove_idx = new HashSet<>();
        markersOnMap.entrySet().stream().filter((e) -> !rat_idxs.contains(e.getKey())).forEach(
                (e) -> {
                    remove_idx.add(e.getKey());
                    e.getValue().remove();
                }
        );
        markersOnMap.keySet().removeAll(remove_idx);
    }

    /**
     * Update the map markers to consider the start and end dates
     *
     * @param map the map
     */
    private void updateMapMarkers(GoogleMap map) {
        Set<Integer> ratidxset = new HashSet<>();
        ratList.parallelStream().filter((d) -> {
            if (startdate == null && enddate == null) {
                return true;
            } else if (startdate == null) {
                return d.getDateCreated().before(enddate);
            } else if (enddate == null) {
                return d.getDateCreated().after(startdate);
            }
            return d.getDateCreated().after(startdate) &&
                    d.getDateCreated().before(enddate);

        }).forEach((d) -> ratidxset.add(d.getId()));
        Log.d("rat-shit2", "" + ratidxset.size());
        refreshMapMarkerMap(ratidxset, map);
    }

    /**
     * Class that handles onDateSet for start
     */
    private class StartDatePickerDialog
            implements DatePickerDialog.OnDateSetListener {

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            Date date = cal.getTime();
            Log.d("rat-shit", date.toString());
            startdate = date;
            updateMapMarkers(map);
        }
    }

    /**
     * Class that handles onDateSet for end
     */
    private class EndDatePickerDialog
            implements DatePickerDialog.OnDateSetListener {

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            Date date = cal.getTime();
            enddate = date;
            updateMapMarkers(map);
        }
    }
}

