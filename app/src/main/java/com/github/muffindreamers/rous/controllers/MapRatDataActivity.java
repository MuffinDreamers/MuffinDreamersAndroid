package com.github.muffindreamers.rous.controllers;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.RatData;
import com.github.muffindreamers.rous.model.RetrieveRatData;
import com.github.muffindreamers.rous.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

/*import com.google.android.gms.maps.OnMapReadyCallback;*/

/**
 * Created by Brooke on 10/7/2017.
 * @author Brooke White
 */

public class MapRatDataActivity extends Activity {
    private static final double V = 40.730610;
    private static final double V_1 = -73.935242;
    private User user = null;
    private Collection<RatData> ratList;
    private Map<Integer, Marker> markersOnMap = new HashMap<>();
    private GoogleMap map;

    private final DateFormat df = new SimpleDateFormat("dd/MM/YY h:m a", Locale.US);

    private Date startDate;
    private Date endDate;
    private EditText startText;
    private EditText endText;

    private FusedLocationProviderClient locationClient;

    /**
     * Creates the main ListView Screen
     * @param savedInstanceState the instance data passed in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent1 = getIntent();
        Bundle extras = intent1.getExtras();
        assert extras != null;
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
                RetrieveRatData retrieveRatData = new RetrieveRatData();
                AsyncTask<String, Void, ArrayList<RatData>> execute = retrieveRatData.execute();
                ratList = execute.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        setContentView(R.layout.activity_map_screen);

        Button return_button = (Button) findViewById(R.id.map_return);
        return_button.setOnClickListener(this::newReturnHandler);

        ((Button)findViewById(R.id.submit_report_at_location)).setOnClickListener(this::submitReportHandler);

        startText = (EditText) findViewById(R.id.start_map);
        startText.setOnClickListener(this::startHandler);

        endText = (EditText) findViewById(R.id.end_map);
        endText.setOnClickListener(this::endHandler);

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
            mapFragment.getMapAsync(googleMap -> {
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
                LatLng coordinates = new LatLng(V, V_1);
                updateMapMarkers(googleMap);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10));
                enableMyLocationOnMap();
            });

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            Intent intent = getIntent();
            mapFragment.setArguments(intent.getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.map_frame_container,
                    mapFragment);
            fragmentTransaction.commit();
        }

        locationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Automatically fills in textviews using the devices current location
     */
    private void enableMyLocationOnMap() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }

        map.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: //Access to fine location
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationOnMap();
                }
                return;
        }
    }

    private void submitReportHandler(View v) {
        Intent toNewRatDataScreen = new Intent(this, AddNewRatData.class);
        toNewRatDataScreen.putExtra("user", user);
        toNewRatDataScreen.putExtra("autofill", true);
        //REMOVE LATER - ONCE DATABASE IS FIXED
        //toNewRatDataScreen.putExtra("ratList", ratList);
        startActivity(toNewRatDataScreen);
    }

    private void newReturnHandler(View v) {
        Intent toMain = new Intent(this, FetchRatDataActivity.class);
        Intent intent = getIntent();
        toMain.putExtra("user", intent.getSerializableExtra("user"));
        toMain.putExtra("auth", intent.getSerializableExtra("auth"));
        startActivity(toMain);
    }

    /**
     * Starts the start date dialog
     *
     * @param v the view
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startHandler(View v) {
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
    private void endHandler(View v) {
        DatePickerDialog newFragment = new DatePickerDialog(MapRatDataActivity.this);
        newFragment.setOnDateSetListener(new EndDatePickerDialog());
        newFragment.show();
    }

    /**
     * Returns the user to the Welcome Screen
     * @param v the view the button is located in
     */
    public void logoutHandler(View v) {
        /*user = null;*/
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
        startActivity(toNewRatDataScreen);
    }

    /**
     * Refresh the markers on the map to match a set
     *
     * @param rat_indexes the set of indexes
     * @param map the map
     */
    private void refreshMapMarkerMap(Collection<Integer> rat_indexes, GoogleMap map) {
        for (int i : rat_indexes) {
            if (!markersOnMap.containsKey(i)) {
                Stream<RatData> stream = ratList.stream();
                Stream<RatData> ratDataStream = stream.filter((d) -> d.getId() == i);
                ratDataStream.findFirst().ifPresent((rat) -> {
                    Log.d("rat-shit", "" + rat.getId());
                    LatLng coordinates = new LatLng(rat.getLatitude(), rat.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    MarkerOptions position = markerOptions.position(coordinates);
                    MarkerOptions title = position
                            .title("Rat " + rat.getId());
                    markersOnMap.put(i, map.addMarker(title
                            .snippet("Spotted: " + rat.getLocationType() + "\n" +
                                    "Date: " + df.format(rat.getDateCreated()))));
                });
            }
        }

        Collection<Integer> remove_idx = new HashSet<>();
        Set<Map.Entry<Integer, Marker>> entries = markersOnMap.entrySet();
        Stream<Map.Entry<Integer, Marker>> stream2 = entries.stream();
        Stream<Map.Entry<Integer, Marker>> entryStream =
                stream2.filter((e) -> !rat_indexes.contains(e.getKey()));
        entryStream.forEach(
                (e) -> {
                    remove_idx.add(e.getKey());
                    Marker value = e.getValue();
                    value.remove();
                }
        );
        Set<Integer> integers = markersOnMap.keySet();
        integers.removeAll(remove_idx);
    }

    /**
     * Update the map markers to consider the start and end dates
     * TODO: Implement a return button to return to home screen
     * @param map the map
     */
    private void updateMapMarkers(GoogleMap map) {
        Set<Integer> ratIndexSet = new HashSet<>();
        Stream<RatData> ratDataStream = ratList.parallelStream();
        Stream<RatData> ratDataStream1 = ratDataStream.filter((d) -> {
            Date dateCreated = d.getDateCreated();
            if ((startDate == null) && (endDate == null)) {
                return true;
            } else if (startDate == null) {
                return dateCreated.before(endDate);
            } else if (endDate == null) {
                return dateCreated.after(startDate);
            }
            return dateCreated.after(startDate) &&
                    dateCreated.before(endDate);

        });
        ratDataStream1.forEach((d) -> ratIndexSet.add(d.getId()));
        Log.d("rat-shit2", "" + ratIndexSet.size());
        refreshMapMarkerMap(ratIndexSet, map);
    }

    /**
     * Class that handles onDateSet for start
     */
    private class StartDatePickerDialog
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            Date date = cal.getTime();
            Log.d("rat-shit", date.toString());
            startText.setText(new SimpleDateFormat("MM-dd-yyyy").format(date));
            startDate = date;
            updateMapMarkers(map);
        }
    }

    /**
     * Class that handles onDateSet for end
     */
    private class EndDatePickerDialog
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            endDate = cal.getTime();
            endText.setText(new SimpleDateFormat("MM-dd-yyyy").format(endDate));
            updateMapMarkers(map);
        }
    }
}

