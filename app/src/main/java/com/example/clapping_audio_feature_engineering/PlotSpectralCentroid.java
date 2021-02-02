package com.example.clapping_audio_feature_engineering;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class PlotSpectralCentroid {
    
    // constructor 겸 계산 method
    public PlotSpectralCentroid (LineChart lineChart, ArrayList<Double> SCArray){

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        ArrayList<Entry> entry_chart = new ArrayList<>();
        for (int i=0; i< SCArray.size(); i++){
            // entry_chart.add(new Entry(x값, y값));
            entry_chart.add(new Entry((float) (i*0.05), (float) (double) SCArray.get(i)));
        }



        LineDataSet set;
        set = new LineDataSet(entry_chart, null);
        dataSets.add(set); // add the data sets
        
        set.setColor(Color.BLUE);
        set.setDrawCircles(false);
        set.setDrawValues(false);

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // set data
        lineChart.setData(data);
        lineChart.getLegend().setEnabled(false);

        // description 부분 empty하게 일단 해놓음
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);


        lineChart.invalidate();
    }
}
