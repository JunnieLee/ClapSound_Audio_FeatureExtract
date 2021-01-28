package com.example.clapping_audio_feature_engineering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.clapping_audio_feature_engineering.ReadingWavFiles.*;
import com.example.clapping_audio_feature_engineering.FFT.*;
import com.example.clapping_audio_feature_engineering.SaveAsFile.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static String[] audioFiles = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
            String file_name = "FFT_for_PCM_no_1";
            // txt 파일 생성을 위해 결과값을 String 형식으로 만들어주기
            String input = "[";
            for (double[]arr: FFTValArray){
                // System.out.println(Arrays.toString(arr));
                String str = Arrays.toString(arr);
                System.out.println(str);
                input.concat(str+"\n");
            }
            input.concat("]");


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
        }

    }
}