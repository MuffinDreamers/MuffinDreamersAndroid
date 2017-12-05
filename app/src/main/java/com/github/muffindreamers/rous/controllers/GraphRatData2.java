package com.github.muffindreamers.rous.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.muffindreamers.rous.R;
import com.github.muffindreamers.rous.model.RatData;

import java.util.ArrayList;

public class GraphRatData2 extends AppCompatActivity {
    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_rat_data2);

        buildGraph();

        Button borough_button = (Button) findViewById(R.id.back_to_graph);
        borough_button.setOnClickListener(this::otherGraphHandler);

        Button return_button = (Button) findViewById(R.id.graph2_return);
        return_button.setOnClickListener(this::newReturnHandler);
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
     * goes to the other graph
     * @param v the view passed in
     */
    private void otherGraphHandler(View v) {
        Intent toGraph = new Intent(this, GraphRatData.class);
        Intent intent = getIntent();
        toGraph.putExtra("user", intent.getSerializableExtra("user"));
        toGraph.putExtra("auth", intent.getSerializableExtra("auth"));
        toGraph.putExtra("ratList", intent.getSerializableExtra("ratList"));
        startActivity(toGraph);
    }

    /**
     * creates a graph by borough
     */
    private void buildGraph() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Iterable<RatData> ratList = (ArrayList<RatData>) extras.getSerializable("ratList");
        int manhattan = 0;
        int staten_island = 0;
        int queens = 0;
        int brooklyn = 0;
        int bronx = 0;
        for (RatData rat :ratList) {
            String borough = rat.getBorough();
            if (borough.equals("Manhattan")) {
                manhattan++;
            } else if (borough.equals("Staten Island")) {
                staten_island++;
            } else if (borough.equals("Queens")) {
                queens++;
            } else if (borough.equals("Brooklyn")) {
                brooklyn++;
            } else {
                bronx++;
            }
        }
        chart = (BarChart) findViewById(R.id.chart2);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f, manhattan));
        barEntries.add(new BarEntry(1f, staten_island));
        barEntries.add(new BarEntry(2f, queens));
        barEntries.add(new BarEntry(3f, brooklyn));
        barEntries.add(new BarEntry(4f, bronx));
        BarDataSet dataSet = new BarDataSet(barEntries, "Borough Locations");

        String[] labels = {"Manhattan" , "Staten Island", "Queens", "Brooklyn", "Bronx"};
        //chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));


        BarData data = new BarData(dataSet);
        chart.setData(data); // set the data and list of lables into chart<br />
        IAxisValueFormatter formatter = (value, axis) -> labels[(int) value];
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
    }

}
