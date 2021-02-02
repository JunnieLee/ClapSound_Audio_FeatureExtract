package com.example.clapping_audio_feature_engineering;

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

import static androidx.core.content.ContextCompat.getDrawable;


public class PlotZCR {

    // constructor 겸 계산 method

    public PlotZCR (LineChart lineChart, ArrayList<Float> ZCRArray, int event_window_idx, Context context){

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        ArrayList<Entry> entry_chart = new ArrayList<>();
        for (int i=0; i< ZCRArray.size(); i++){
            // entry_chart.add(new Entry(x값, y값));
            entry_chart.add(new Entry((float) (i*0.05), ZCRArray.get(i)));
        }

        LineDataSet set;
        set = new LineDataSet(entry_chart, null);
        dataSets.add(set); // add the data sets

        set.getEntryForIndex(event_window_idx).setIcon(getDrawable(context, R.drawable.ic_heart));
        // peak point (event 발생한 window) 에 하트표시!

        set.setColor(Color.GREEN);
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
