package com.example.clapping_audio_feature_engineering;

import org.jtransforms.fft.DoubleFFT_1D;


public class FFT {

    public double[] get_FFT_val(double[] input){
        DoubleFFT_1D fftDo = new DoubleFFT_1D(input.length);
        double[] fft = new double[input.length * 2];
        System.arraycopy(input, 0, fft, 0, input.length);
        fftDo.realForwardFull(fft);
        /*
        for (double elements:fft){
            elements = Math.abs(elements);
        }
        */
        double[] abs_fft = new double[fft.length];
        int i=0;
        for (double k:fft){
            abs_fft[i] = Math.abs(k);
            i++;
        }

        return abs_fft;
        // return fft;
    }

}
