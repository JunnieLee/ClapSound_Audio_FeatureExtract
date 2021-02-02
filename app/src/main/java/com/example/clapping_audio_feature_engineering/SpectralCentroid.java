package com.example.clapping_audio_feature_engineering;

public class SpectralCentroid {

    public static double calculate(double[] fft_arr){
        // length
        int N = fft_arr.length;

        double[] freq = new double[N];
        for(int i=0;i<N;i++) {
            freq[i] = i * 20;
        }

        return  normalize((sum(multiply(fft_arr,freq))/sum(fft_arr)));
    }

    private static double sum(double[] arr){
        int size = arr.length;
        double answer = 0;
        for (int i=0; i<size; i++){
            answer+=arr[i];
        }
        return answer;
    }

    private static double normalize(double input){
        return (input/8000);
    }

    private static double[] multiply(double[] arr1, double[] arr2){
        if (arr1.length != arr2.length) return null;
        int N = arr1.length;
        double[] answer = new double[N];
        for (int i=0; i<N; i++){
            answer[i] = arr1[i]*arr2[i];
        }
        return answer;
    }

}


/*
python version code


def spectral_centroid(x, samplerate=44100):
    magnitudes = np.abs(np.fft.rfft(x)) # magnitudes of positive frequencies
    length = len(x)
    freqs = np.abs(np.fft.fftfreq(length, 1.0/samplerate)[:length//2+1]) # positive frequencies
    return np.sum(magnitudes*freqs) / np.sum(magnitudes) # return weighted mean

*/