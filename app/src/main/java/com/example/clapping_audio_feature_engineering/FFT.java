package com.example.clapping_audio_feature_engineering;

import org.jtransforms.fft.DoubleFFT_1D;


public class FFT {

    public double[] get_FFT_val(double[] input){

        double[] a = new double[input.length * 2];
        for(int k=0;k<input.length;k++){
            a[2*k] = input[k];   //Re
            a[2*k+1] = 0; //Im
        }
        DoubleFFT_1D fft = new DoubleFFT_1D(input.length); //1차원의 fft 수행
        fft.realForward(a); //a 배열에 output overwrite

        double[] abs_fft = new double[input.length/2];
        for (int i =0; i< input.length/2; i++){
            abs_fft[i] = Math.abs(a[i]);
        }

        return abs_fft;
    }

}
