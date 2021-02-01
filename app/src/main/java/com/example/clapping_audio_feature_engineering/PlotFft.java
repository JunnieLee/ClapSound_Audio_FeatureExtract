package com.example.clapping_audio_feature_engineering;

import android.graphics.Color;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;


public class PlotFft {

    public LineChart lineChart;
    public ArrayList<String> x = new ArrayList<String>();
    public ArrayList<Entry> y = new ArrayList<Entry>();
    public ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
    public LineData lineData = null;
    //  lineChart = (LineChart)findViewById(R.id.spread_line_chart);

    public PlotFft(LineChart lineChart){
        this.lineChart = lineChart;
    }

    /**
     * Initialization data
     * count indicates the number of coordinate points, range indicates the range of y value generation
     */
    public LineData getLineData(int count, float range) {
        for (int i = 0; i < count; i++) {  //Data displayed on X axis
            x.add(i + "");
        }
        for (int i = 0; i < count; i++) {//y-axis data
            float result = (float) (Math.random() * range) + 3;
            y.add(new Entry(result, i));
        }
        LineDataSet lineDataSet = new LineDataSet(y, "Randomly Generated Line Chart");//y-axis data collection
        lineDataSet.setLineWidth(1f);//Line width
        lineDataSet.setCircleSize(Color.BLUE);//Circle color
        lineDataSet.setCircleSize(2f);//Realistic circle size
        lineDataSet.setColor(Color.RED);//Realistic color
        lineDataSet.setHighLightColor(Color.BLACK);//The color of the height line
        lineDataSets.add(lineDataSet);
        lineData = new LineData((ILineDataSet) x,lineDataSet);
        return lineData;
    }
    /**
     * Set style
     */
    public void showChart() {
        lineChart.setDrawBorders(false);//Whether to add a border
        // lineChart.setDescription("Randomly Generated Data");//Data description
        lineChart.setNoDataText("I need data"); //No data to display
        lineChart.setDrawGridBackground(true);//Whether to display the table color
        lineChart.setBackgroundColor(Color.GRAY);//background color
        lineChart.setData(lineData);//Set data
        Legend legend = lineChart.getLegend();//Set the scale picture mark, which is the value of that group of Y
        legend.setForm(Legend.LegendForm.SQUARE);//style
        legend.setFormSize(10f);//Font
        legend.setTextColor(Color.BLUE);//Set the color
        lineChart.animateX(2000);//X-axis animation
    }
}
