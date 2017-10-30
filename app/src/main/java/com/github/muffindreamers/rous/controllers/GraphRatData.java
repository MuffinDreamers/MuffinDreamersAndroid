package com.github.muffindreamers.rous.controllers;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.RatData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Brooke 10/27/17
 */
public class GraphRatData extends AppCompatActivity {

    private Date startdate, enddate;
    private LineChart chart;
    private Spinner graphType;
    private String selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_rat_data);

        /*List<String> graphTypeArray = Arrays.asList("Month", "Year");

        graphType = (Spinner) findViewById(R.id.graphTypeSpinner);
        ArrayAdapter<String> graphTypeAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, graphTypeArray);
        graphTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphType.setAdapter(graphTypeAdapter);

        selected = graphType.getSelectedItem().toString();
        Log.e("Selected", selected);*/
        sortByMonth();


        Button return_button = (Button) findViewById(R.id.graph_return);
        return_button.setOnClickListener(this::newReturnHandler);

        Button start = (Button) findViewById(R.id.start_graph);
        start.setOnClickListener((v) -> startHandler(v));

        Button end = (Button) findViewById(R.id.end_graph);
        end.setOnClickListener((v) -> endHandler(v));
    }

    /**\
     * 
     * @param v
     */
    public void newReturnHandler(View v) {
        Intent toMain = new Intent(this, FetchRatDataActivity.class);
        toMain.putExtra("user", getIntent().getSerializableExtra("user"));
        toMain.putExtra("auth", getIntent().getSerializableExtra("auth"));
        startActivity(toMain);
    }
    /**
     * Starts the start date dialog
     *
     * @param v the view
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startHandler(View v) {
        DatePickerDialog newFragment = new DatePickerDialog(GraphRatData.this);
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
        DatePickerDialog newFragment = new DatePickerDialog(GraphRatData.this);
        newFragment.setOnDateSetListener(new EndDatePickerDialog());
        newFragment.show();
    }

    /**
     * filters the graph by date
     * @param chart the chart / graph being displayed
     * @param startDate the startdate to filter by
     * @param endDate the enddate to filter by
     */
    private void updateGraphView (LineChart chart, Date startDate, Date endDate) {
        Calendar startC = Calendar.getInstance();
        Calendar endC = Calendar.getInstance();
        if (startDate != null) {
            startC.setTime(startDate);
        }
        if (endDate != null) {
            endC.setTime(endDate);
        }
        XAxis xaxis = chart.getXAxis();
        if (endDate == null && startDate != null) {
            xaxis.setAxisMinimum(startC.get(Calendar.MONTH) + 1);
            chart.invalidate();
        } else if (endDate != null && startDate == null) {
            xaxis.setAxisMaximum(endC.get(Calendar.MONTH) + 1);
            chart.invalidate();
        } else {
            xaxis.setAxisMinimum(startC.get(Calendar.MONTH) + 1);
            xaxis.setAxisMaximum(endC.get(Calendar.MONTH) + 1);
            chart.invalidate();
        }


        chart.fitScreen();
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
            updateGraphView(chart, startdate, null);
        }
    }
    private class EndDatePickerDialog
            implements DatePickerDialog.OnDateSetListener {

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            Date date = cal.getTime();
            enddate = date;
            updateGraphView(chart, null, enddate);
        }
    }

    /**
     * creates a graph by month
     */
    private void sortByMonth() {
        Bundle extras = getIntent().getExtras();
        ArrayList<RatData> ratList = (ArrayList<RatData>) extras.getSerializable("ratlist");
        int jan= 0,feb = 0,mar = 0,apr = 0,may = 0,jun = 0,jul = 0,aug = 0,sep = 0,oct = 0,nov = 0,dec = 0;
        for (RatData rat : ratList) {
            Date date = rat.getDateCreated();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);
            if (month == 0) {
                jan++;
            }if (month == 1) {
                feb++;
            }if (month == 2) {
                mar++;
            }if (month == 3) {
                apr++;
            }if (month == 4) {
                may++;
            }if (month == 5) {
                jun++;
            }if (month == 6) {
                jul++;
            }if (month == 7) {
                aug++;
            }if (month == 8) {
                sep++;
            }if (month == 9) {
                oct++;
            }if (month == 10) {
                nov++;
            }if (month == 11) {
                dec++;
            }
        }
        setContentView(R.layout.activity_graph_rat_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //graph = (GraphView) findViewById(R.id.graph);
        chart = (LineChart) findViewById(R.id.chart);
        ArrayList<Entry> entries = new ArrayList<>();

        entries.add(new Entry(1, jan));
        entries.add(new Entry(2, feb));
        entries.add(new Entry(3, mar));
        entries.add(new Entry(4, apr));
        entries.add(new Entry(5, may));
        entries.add(new Entry(6, jun));
        entries.add(new Entry(7, jul));
        entries.add(new Entry(8, aug));
        entries.add(new Entry(9, sep));
        entries.add(new Entry(10, oct));
        entries.add(new Entry(11, nov));
        entries.add(new Entry(12, dec));

        LineDataSet dataSet = new LineDataSet(entries, "Number of Rat Sightings");

        final String[] lables = new String[] {"0" ,"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        LineData data = new LineData(dataSet);
        chart.setData(data);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return lables[(int) value];
            }

            // we don't draw numbers, so no decimal digits needed

            public int getDecimalDigits() {  return 0; }
        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
    }



}
