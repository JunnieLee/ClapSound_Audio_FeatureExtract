package com.example.clapping_audio_feature_engineering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.clapping_audio_feature_engineering.ReadingWavFiles.*;
import com.example.clapping_audio_feature_engineering.FFT.*;

public class MainActivity extends AppCompatActivity {

    private static String[] audioFiles = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [0단계] : 각 PCM 파일별 경로 넘겨주기
        for (int i=0; i<8; i++){ // 일단 당장은 8개라 8 이라고 해놨음!! --> 추후 10개까지 채우면 그때 수정~
            audioFiles[i] = "./AudioFiles/"+(i+1)+".pcm";
        } // audio file 배열에 file path 채워넣기


        /////////////////////////////////////////////////////////////////////////////////////////////
        // 일단 당장은 1.pcm에 대해서만 하자~ /////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////


        // [1단계] : 전처리 (ReadingWavFiles)




        // [2단계] : FFT 결과 추출 (FFT)

        /* FFT 에 대하여 결과 출력
        for(double d: fft) {
            System.out.println(d);
        }
        */
        setContentView(R.layout.activity_main);
    }
}