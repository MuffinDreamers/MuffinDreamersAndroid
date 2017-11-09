package com.github.muffindreamers.rous.controllers;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.RatData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*import android.support.v7.widget.Toolbar;*/
/*import android.widget.ArrayAdapter;*/
/*import android.widget.Spinner;*/
/*import com.github.mikephil.charting.components.AxisBase;*/

/**
 * Created by Brooke 10/27/17
 */
public class GraphRatData extends AppCompatActivity {

    private LineChart chart;
    private EditText startText;
    private EditText endText;

    /**
     * the function executes when the activity is opened
     * @param savedInstanceState the instance data passed in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_rat_data);

        /*List<String> graphTypeArray = Arrays.asList("Month", "Year");

        graphType = (Spinner) findViewById(R.id.graphTypeSpinner);
        ArrayAdapter<String> graphTypeAdapter =
        new ArrayAdapter(this,android.R.layout.simple_spinner_item, graphTypeArray);
        graphTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphType.setAdapter(graphTypeAdapter);

        selected = graphType.getSelectedItem().toString();
        Log.e("Selected", selected);*/
        sortByMonth();


        Button return_button = (Button) findViewById(R.id.graph_return);
        return_button.setOnClickListener(this::newReturnHandler);

        startText = (EditText) findViewById(R.id.start_graph);
        startText.setOnClickListener(this::startHandler);

        endText = (EditText) findViewById(R.id.end_graph);
        endText.setOnClickListener(this::endHandler);
    }

    /**
     * returns to the main screen
     * @param v the view passed in
     */
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
    private void endHandler(View v) {
        DatePickerDialog newFragment = new DatePickerDialog(GraphRatData.this);
        newFragment.setOnDateSetListener(new EndDatePickerDialog());
        newFragment.show();
    }

    /**
     * filters the graph by date
     * @param chart the chart / graph being displayed
     * @param startDate the starting date to filter by
     * @param endDate the ending date to filter by
     */
    private void updateGraphView (LineChart chart, Date startDate, Date endDate) {
        Calendar startC = Calendar.getInstance();
        startC.set(2017, 0, 9);
        Calendar endC = Calendar.getInstance();
        endC.set(2017,11 , 9);
        if (startDate != null) {
            startC.setTime(startDate);
        }
        if (endDate != null) {
            endC.setTime(endDate);
        }
        XAxis xaxis = chart.getXAxis();
        if ((endDate == null) && (startDate != null)) {
            xaxis.setAxisMinimum(startC.get(Calendar.MONTH) + 1);
            chart.invalidate();
        } else if ((endDate != null) && (startDate == null)) {
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

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            Date date = cal.getTime();
            startText.setText(new SimpleDateFormat("MM-dd-yyyy").format(date));
            Log.d("rat-shit", date.toString());
            updateGraphView(chart, date, null);
        }
    }
    private class EndDatePickerDialog
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            Date end_date = cal.getTime();
            endText.setText(new SimpleDateFormat("MM-dd-yyyy").format(end_date));
            updateGraphView(chart, null, end_date);
        }
    }

    /**
     * creates a graph by month
     */
    private void sortByMonth() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Iterable<RatData> ratList = (ArrayList<RatData>) extras.getSerializable("ratList");
        int jan = 0;
        int feb = 0;
        int mar = 0;
        int apr = 0;
        int may = 0;
        int jun = 0;
        int jul = 0;
        int aug = 0;
        int sep = 0;
        int oct = 0;
        int nov = 0;
        int dec = 0;
        for (RatData rat : ratList) {
            Date date = rat.getDateCreated();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);
            if (month == 0) {
                jan++;
            } else if (month == 1) {
                feb++;
            } else if (month == 2) {
                mar++;
            }else if (month == 3) {
                apr++;
            }else if (month == 4) {
                may++;
            }else if (month == 5) {
                jun++;
            }else if (month == 6) {
                jul++;
            }else if (month == 7) {
                aug++;
            }else if (month == 8) {
                sep++;
            }else if (month == 9) {
                oct++;
            }else if (month == 10) {
                nov++;
            }else if (month == 11) {
                dec++;
            }
        }
        setContentView(R.layout.activity_graph_rat_data);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
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

        final String[] labels = new String[] {"0" ,"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};

        LineData data = new LineData(dataSet);
        chart.setData(data);

        // we don't draw numbers, so no decimal digits needed
        IAxisValueFormatter formatter = (value, axis) -> labels[(int) value];

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
    }



}
