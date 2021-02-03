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
import com.example.clapping_audio_feature_engineering.SpectralCentroid.*;
import com.example.clapping_audio_feature_engineering.PlotSpectralCentroid.*;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Double.NaN;



public class MainActivity extends AppCompatActivity {

    // for the chart
    public LineChart lineChart;
    public LineChart lineChart2;
    public LineChart lineChart3;

    private static String[] audioFiles = new String[10];
    private String fileName="1.pcm";
    int event_window_idx = -1;  // 소리나는 윈도우에 대한 변수 설정
    ArrayList<double[]> FirstOneInput = new ArrayList<double[]>(); // raw data
    Context context;
    AssetManager am;
    ArrayList<Float> ZCR_arr = new ArrayList<Float>(); // ZCR value는 나중에 plotting을 위해 여기다가 저장!!
    ArrayList<double[]> FFTValArray = new ArrayList<double[]>();
    ArrayList<Double> arr_of_SC = new ArrayList<Double>(); // Spectral Centroid



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
        context = getApplicationContext();
        am = context.getAssets() ; // getResources().getAssets()

        // [0단계] : 각 PCM 파일별 경로 넘겨주기
        for (int i=0; i<8; i++){ // 일단 당장은 8개라 8 이라고 해놨음!! --> 추후 10개까지 채우면 그때 수정~
            audioFiles[i] = (i+1)+".pcm";
        } // audio file 배열에 file path 채워넣기

        fileName = audioFiles[0]; // 처음시작은 1.pcm을 디폴트로 해놓자!!

        setContentView(R.layout.activity_main);

        activateAll(fileName);


        // on-click event 에 따라서 각기 다른 데이터 값을 볼 수 있도록 조정!!
        Button button1 = (Button) findViewById(R.id.button1) ;
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileName = audioFiles[0];
                activateAll(fileName);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2) ;
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileName = audioFiles[1];
                activateAll(fileName);
            }
        });

        Button button3 = (Button) findViewById(R.id.button3) ;
        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileName = audioFiles[2];
                activateAll(fileName);
            }
        });


        } // end of OnCreate




    private void PreProcessing(){
        // [1단계] : 전처리 (ReadingWavFiles)
        ReadingWavFiles FirstOne = new ReadingWavFiles(am, fileName);
        try {
            event_window_idx = FirstOne.GetFFTInputFormat().getKey();
            FirstOneInput = FirstOne.GetFFTInputFormat().getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CalculateFFTVal() {
        // [2단계] : FFT 결과 추출 (FFT)
        if (FirstOneInput != null) {

            // ZCR 추출을 위한 mini-phase
            for (double[] rawData : FirstOneInput) {
                // ZCR zcr = new ZCR();
                float ZCRVal = ZCR.calculate(rawData);
                ZCR_arr.add(ZCRVal);
            }

            // FFT 추출
            int len = FirstOneInput.size();
            for (int i = 0; i < len; i++) {
                FFT fft = new FFT();
                FFTValArray.add(fft.get_FFT_val(FirstOneInput.get(i)));
            }
            String file_name = "FFT " + fileName;
            // txt 파일 생성을 위해 결과값을 String 형식으로 만들어주기
            StringBuilder builder = new StringBuilder("[");
            for (double[] arr : FFTValArray) {
                String str = Arrays.toString(arr);
                System.out.println(str);
                builder.append(str + "\n");
            }
            builder.append("]");
            String input = builder.toString();


            Button button = (Button) findViewById(R.id.button); // '다운로드'를 위한 버튼 생성
            button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : click event
                    SaveAsFile pcm_1 = new SaveAsFile();
                    pcm_1.save(file_name, input);
                    Toast.makeText(context, "DOWNLOAD SUCCESS!!!", Toast.LENGTH_LONG).show();
                }
            });
        }

        // ** Spectral Centroid 계산
        for (double[]arr: FFTValArray){
            Double val = SpectralCentroid.calculate(arr);
            if (val.isNaN()){ // zero devision의 경우 따로 처리!
                val = 0.0;
            }
            arr_of_SC.add(val);
            // Log.i("centroidtest", Double.toString(sc.calculate(arr))); --> 디버깅용 꿀팁!!
        }
    }

    private void Plot(){

        // ** FFT plotting 부분 코드
        lineChart = (LineChart)findViewById(R.id.lineChart1);
        PlotFft FFT_plot = new PlotFft(lineChart, FFTValArray, event_window_idx);


        // ** Spectral Centroid plotting
        lineChart2 = (LineChart)findViewById(R.id.lineChart2);
        PlotSpectralCentroid SCPlot = new PlotSpectralCentroid(lineChart2, arr_of_SC, event_window_idx, context);

        // ** ZCR 은 위에 ZCR_arr에서 이미 계산해놨구

        // ** ZCR plotting
        lineChart3 = (LineChart)findViewById(R.id.lineChart3);
        PlotZCR ZCRPlot = new PlotZCR(lineChart3, ZCR_arr, event_window_idx, context);
    }

    private void reset(){
        event_window_idx = -1;  // 소리나는 윈도우에 대한 변수 설정
        FirstOneInput = new ArrayList<double[]>(); // raw data
        ZCR_arr = new ArrayList<Float>(); // ZCR value는 나중에 plotting을 위해 여기다가 저장!!
        FFTValArray = new ArrayList<double[]>();
        arr_of_SC = new ArrayList<Double>(); // Spectral Centroid
    }

    private void activateAll(String input){
        reset();
        fileName = input;
        PreProcessing();
        CalculateFFTVal();
        Plot();
    }

    }


