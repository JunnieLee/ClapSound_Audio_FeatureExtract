package com.example.clapping_audio_feature_engineering;

import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;


public class PlotFft {

    public PlotFft(LineChart lineChart, ArrayList<double[]> FFTValArray) {

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        for (double[] arr : FFTValArray){

            ArrayList<Entry> entry_chart = new ArrayList<>();

            // 하나의 FFT window에 대하여 아래의 연산 진행
            for (int i=0; i<arr.length;i++){
                // entry_chart.add(new Entry(x값, y값)); i*20
                entry_chart.add(new Entry(i*20, (float) arr[i]));
            }


            /*
            LineData chartData = new LineData();
            LineDataSet set = new LineDataSet(entry_chart, "꺾은선1");
            chartData.addDataSet(set);
            lineChart.setData(chartData);
            */
            LineDataSet set;
            set = new LineDataSet(entry_chart, null);
            // ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set); // add the data sets

            set.setColor(Color.RED);
            set.setDrawCircles(false);
            set.setDrawValues(false);
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
