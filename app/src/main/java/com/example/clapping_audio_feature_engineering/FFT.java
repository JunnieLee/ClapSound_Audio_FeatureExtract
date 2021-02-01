package com.example.clapping_audio_feature_engineering;

import org.jtransforms.fft.DoubleFFT_1D;


public class FFT {

    public double[] get_FFT_val(double[] input){
        DoubleFFT_1D fftDo = new DoubleFFT_1D(input.length);
        double[] a = new double[input.length * 2];
        for(int k=0;k<input.length;k++){
            a[2*k] = input[k];   //Re
            a[2*k+1] = 0; //Im
        }
        // System.arraycopy(input, 0, fft, 0, input.length);
        // fftDo.realForwardFull(fft);
        // fftDo.realForward(fft);

        DoubleFFT_1D fft = new DoubleFFT_1D(input.length); //1차원의 fft 수행
        fft.complexForward(a); //a 배열에 output overwrite


        /*
        for (double elements:fft){
            elements = Math.abs(elements);
        }
        */
        // double[] abs_fft = new double[input.length];
        int i=0;
        for (double k:a){
            a[i] = Math.abs(k);
            i++;
        }

        System.out.println("Hello~~~~~~~~~~~~~~");

        double[] mag = new double[input.length/2];
        for(int k=0;k<(input.length/2);k++){
            mag[k] = Math.sqrt(Math.pow(input[2*k],2)+Math.pow(input[2*k+1],2));
        }

        System.out.println("Hi~~~~~~~~~~~~~~");

        return mag;
        // return abs_fft;
        // return fft;
    }

}
