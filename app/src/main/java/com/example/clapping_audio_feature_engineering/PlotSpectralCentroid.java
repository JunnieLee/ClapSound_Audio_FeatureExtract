package com.example.clapping_audio_feature_engineering;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.createDeviceProtectedStorageContext;
import static androidx.core.content.ContextCompat.getDrawable;

public class PlotSpectralCentroid {
    
    // constructor 겸 계산 method
    public PlotSpectralCentroid (LineChart lineChart, ArrayList<Double> SCArray, int event_window_idx, Context context){

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        ArrayList<Entry> entry_chart = new ArrayList<>();
        for (int i=0; i< SCArray.size(); i++){
            // entry_chart.add(new Entry(x값, y값));
            entry_chart.add(new Entry((float) (i*0.05), (float) (double) SCArray.get(i)));
        }

        LineDataSet set;
        set = new LineDataSet(entry_chart, null);
        dataSets.add(set); // add the data sets

        set.getEntryForIndex(event_window_idx).setIcon(getDrawable(context, R.drawable.ic_heart));
        // peak point (event 발생한 window) 에 하트표시!

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
