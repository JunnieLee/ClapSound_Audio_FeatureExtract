package com.example.clapping_audio_feature_engineering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.clapping_audio_feature_engineering.ReadingWavFiles.*;
import com.example.clapping_audio_feature_engineering.FFT.*;
import com.example.clapping_audio_feature_engineering.SaveAsFile.*;
import com.example.clapping_audio_feature_engineering.PlotFft.*;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // for the chart
    public LineChart lineChart;
    public ArrayList<String> x = new ArrayList<String>();
    public ArrayList<Entry> y = new ArrayList<Entry>();
    public ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
    public LineData lineData = null;

    private static String[] audioFiles = new String[10];


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }


        // AssetFileDescriptor afd = getResources().getAssets().openFd("audio/oh.mp3");

        Context context = getApplicationContext();
        AssetManager am = context.getAssets() ; // getResources().getAssets()

        // [0단계] : 각 PCM 파일별 경로 넘겨주기
        for (int i=0; i<8; i++){ // 일단 당장은 8개라 8 이라고 해놨음!! --> 추후 10개까지 채우면 그때 수정~

            // src\main\java\com\example\clapping_audio_feature_engineering\MainActivity.java
            // src\main\java\com\example\clapping_audio_feature_engineering\AudioFiles\1.pcm
            audioFiles[i] = (i+1)+".pcm"; // com\example\clapping_audio_feature_engineering\AudioFiles\1.pcm
        } // audio file 배열에 file path 채워넣기


        // ------------------------------------------------------------------------------------------
        // 파일 하나씩 처리하도록 일단 짜놓음!!! -------------------------------------------------------

        /////////////////////////////////////////////////////////////////////////////////////////////
        // 일단 당장은 첫번째 파일 '1.pcm' 에 대해서만 하자~ ///////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////



        // [1단계] : 전처리 (ReadingWavFiles)
        ReadingWavFiles FirstOne = new ReadingWavFiles(am, audioFiles[0]);
        ArrayList<double[]> FirstOneInput = new ArrayList<double[]>();
        try {
            FirstOneInput = FirstOne.GetFFTInputFormat();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // printing for check
        System.out.println("FFT Input as double arrays : ");
        for (double[] arr : FirstOneInput){
            System.out.println(Arrays.toString(arr));
        }
        System.out.println("-------------------------------------------------------------");



        // [2단계] : FFT 결과 추출 (FFT)
        if (FirstOneInput != null){
            ArrayList<double[]> FFTValArray = new ArrayList<double[]>();
            int len = FirstOneInput.size();
            for (int i=0; i<len; i++){
                FFT fft = new FFT();
                FFTValArray.add(fft.get_FFT_val(FirstOneInput.get(i)));
            }
            // FFT값들을 담은 array에 대하여 값 출력 (one FFT array for one PCM file)
            // System.out.println("FFT array value for '1.pcm' file :");
            System.out.println("FFT array value for '1.pcm' file :");

            // file_name은 뭐로 하징
            String file_name = "FFT_1";
            // txt 파일 생성을 위해 결과값을 String 형식으로 만들어주기
            StringBuilder builder = new StringBuilder("[");
            for (double[]arr: FFTValArray){
                // System.out.println(Arrays.toString(arr));
                String str = Arrays.toString(arr);
                System.out.println(str);
                builder.append(str+"\n");
            }
            builder.append("]");

            String input = builder.toString();
            System.out.println("input :");
            System.out.println(input);


            setContentView(R.layout.activity_main);


            Button button = (Button) findViewById(R.id.button) ; // '다운로드'를 위한 버튼 생성

            button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : click event
                    SaveAsFile pcm_1 = new SaveAsFile();
                    pcm_1.save(file_name, input);
                    Toast.makeText(context, "DOWNLOAD SUCCESS!!!", Toast.LENGTH_LONG).show();
                }
            });
            // 출력 형식이 달라져야 한다면 추후에 이쪽 코드 수정~~~


            // plotting 부분 코드
            lineChart = (LineChart)findViewById(R.id.lineChart1);

            /*
            ArrayList<Entry> values = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                float val = (float) (Math.random() * 10);
                values.add(new Entry(i, val));
            }
            LineDataSet set1;
            set1 = new LineDataSet(values, null);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            ArrayList<Entry> values2 = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                float val = (float) (Math.random() * 10);
                values2.add(new Entry(i, val));
            }
            LineDataSet set2;
            set2 = new LineDataSet(values2, null);
            dataSets.add(set2); // add the data sets


            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // black lines and points
            set1.setColor(Color.RED);
            set1.setDrawCircles(false);
            set1.setDrawValues(false);

            set2.setColor(Color.RED);
            set2.setDrawCircles(false);
            set2.setDrawValues(false);

            // set data
            lineChart.setData(data);

            */

            PlotFft FFT_plot = new PlotFft(lineChart, FFTValArray);

        }

    }
}