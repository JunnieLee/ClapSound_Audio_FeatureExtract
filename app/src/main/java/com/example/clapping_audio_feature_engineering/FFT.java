package com.example.clapping_audio_feature_engineering;

import org.jtransforms.fft.DoubleFFT_1D;


public class FFT {

    public double[] get_FFT_val(double[] input){
        DoubleFFT_1D fftDo = new DoubleFFT_1D(input.length);
        double[] fft = new double[input.length * 2];
        System.arraycopy(input, 0, fft, 0, input.length);
        fftDo.realForwardFull(fft);
        return fft;
    }

}
