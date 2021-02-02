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
        Context context = getApplicationContext();
        AssetManager am = context.getAssets() ; // getResources().getAssets()

        // [0단계] : 각 PCM 파일별 경로 넘겨주기
        for (int i=0; i<8; i++){ // 일단 당장은 8개라 8 이라고 해놨음!! --> 추후 10개까지 채우면 그때 수정~
            audioFiles[i] = (i+1)+".pcm"; // com\example\clapping_audio_feature_engineering\AudioFiles\1.pcm
        } // audio file 배열에 file path 채워넣기


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

        // [2단계] : FFT 결과 추출 (FFT)
        if (FirstOneInput != null){
            ArrayList<double[]> FFTValArray = new ArrayList<double[]>();
            int len = FirstOneInput.size();
            for (int i=0; i<len; i++){
                FFT fft = new FFT();
                FFTValArray.add(fft.get_FFT_val(FirstOneInput.get(i)));
            }
            String file_name = "FFT_1";
            // txt 파일 생성을 위해 결과값을 String 형식으로 만들어주기
            StringBuilder builder = new StringBuilder("[");
            for (double[]arr: FFTValArray){
                String str = Arrays.toString(arr);
                System.out.println(str);
                builder.append(str+"\n");
            }
            builder.append("]");
            String input = builder.toString();


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


            // ** FFT plotting 부분 코드
            lineChart = (LineChart)findViewById(R.id.lineChart1);
            PlotFft FFT_plot = new PlotFft(lineChart, FFTValArray);

            // ** Spectral Centroid 계산
            ArrayList<Double> arr_of_SC = new ArrayList<Double>();
            for (double[]arr: FFTValArray){
                SpectralCentroid sc = new SpectralCentroid();
                Double val = sc.calculate(arr);
                if (val.isNaN()){ // zero devision의 경우 따로 처리!
                    val = 0.0;
                }
                arr_of_SC.add(val);
                // Log.i("centroidtest", Double.toString(sc.calculate(arr))); --> 디버깅용 꿀팁!!
            }

            // ** Spectral Centroid plotting
            lineChart2 = (LineChart)findViewById(R.id.lineChart2);
            PlotSpectralCentroid SCPlot = new PlotSpectralCentroid(lineChart2, arr_of_SC);
        }

    }
}