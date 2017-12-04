package com.github.muffindreamers.rous.controllers;
import android.content.Context;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Brooke on 11/3/2017.
 * Tests the GraphRatData updateGraphView method
 */

public class TestGraphData {
    private GraphRatData graphRatData = new GraphRatData();
    private static String METHOD_NAME = "updateGraphView";
    private Method m;
    private Class[] parameterTypes;
    private Object[] parameters;
    private XAxis xaxis;
    private LineChart lineChart;

    @Mock
    Context mockContext;

    @Before
    public void setUp() throws NoSuchMethodException {
        lineChart = new LineChart(mockContext);
        parameterTypes = new Class[3];
        parameterTypes[0] = LineChart.class;
        parameterTypes[1] = java.util.Date.class;
        parameterTypes[2] = java.util.Date.class;
        m = graphRatData.getClass().getDeclaredMethod(METHOD_NAME, parameterTypes);
        m.setAccessible(true);
        parameters = new Object[3];
        xaxis = lineChart.getXAxis();
        xaxis.setAxisMaximum(12);
        xaxis.setAxisMinimum(0);
    }
    @Test
    public void test() {
        assertEquals(true,true);
    }

    @Test
    public void testNullStartEndDate() throws InvocationTargetException, IllegalAccessException {
        parameters[0] = lineChart;
        parameters[1] = null;
        parameters[2] = null;
        m.invoke(graphRatData, parameters);
        float a = 1;
        float b = 12;
        assertEquals(a, xaxis.getAxisMinimum());
        assertEquals(b, xaxis.getAxisMaximum());
    }

    @Test
    public void testStartDate() throws InvocationTargetException, IllegalAccessException {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, 4);
        Date date = calendar.getTime();
        parameters[0] = lineChart;
        parameters[1] = date;
        parameters[2] = null;
        m.invoke(graphRatData, parameters);
        float a = 5;
        float b = 12;
        assertEquals(a, xaxis.getAxisMinimum());
        assertEquals(b, xaxis.getAxisMaximum());
    }

    @Test
    public void testEndDate() throws InvocationTargetException, IllegalAccessException {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, 8);
        Date date = calendar.getTime();
        parameters[0] = lineChart;
        parameters[1] = null;
        parameters[2] = date;
        m.invoke(graphRatData, parameters);
        float a = 0;
        float b = 9;
        assertEquals(a, xaxis.getAxisMinimum());
        assertEquals(b, xaxis.getAxisMaximum());
    }

    @Test
    public void testStartEndDate() throws InvocationTargetException, IllegalAccessException {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, 8);
        Date date = calendar.getTime();
        calendar.set(Calendar.MONTH, 3);
        Date date2 = calendar.getTime();
        parameters[0] = lineChart;
        parameters[1] = date2;
        parameters[2] = date;
        m.invoke(graphRatData, parameters);
        float a = 4;
        float b = 9;
        assertEquals(a, xaxis.getAxisMinimum());
        assertEquals(b, xaxis.getAxisMaximum());
    }
}
