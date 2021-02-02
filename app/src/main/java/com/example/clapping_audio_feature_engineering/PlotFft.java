package com.example.clapping_audio_feature_engineering;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;


public class PlotFft {

    public PlotFft(LineChart lineChart, ArrayList<double[]> FFTValArray, int event_window_idx) {

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        int k=0;
        for (double[] arr : FFTValArray){

            ArrayList<Entry> entry_chart = new ArrayList<>();

            // 하나의 FFT window에 대하여 아래의 연산 진행
            for (int i=0; i<arr.length;i++){
                // entry_chart.add(new Entry(x값, y값)); i*20
                entry_chart.add(new Entry(i*20, (float) arr[i]));
            }

            LineDataSet set;
            set = new LineDataSet(entry_chart, null);
            dataSets.add(set); // add the data sets

            if (k==event_window_idx){
                set.setColor(Color.RED);
            } else {
                set.setColor(Color.BLUE);
            }
            set.setDrawCircles(false);
            set.setDrawValues(false);

            k++;
        }

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
