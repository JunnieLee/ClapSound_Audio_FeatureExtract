package com.example.clapping_audio_feature_engineering;

import java.util.ArrayList;

public class ZCR {

    public static float calculate(double[] raw_data){
        int cnt = raw_data.length;
        double a = sum(abs(diff(sign(raw_data))))/2;

        // count_zero = np.sum(np.abs(np.diff(np.sign(frame))))/2
        // return np.float64(count_zero)/np.float64(count-1.0)
        return (float)a/(float)(cnt-1.0);
    }


    private static double sum(ArrayList<Double> arr){
        int size = arr.size();
        double answer = 0;
        for (int i=0; i<size; i++){
            answer+=arr.get(i);
        }
        return answer;
    }

    private static ArrayList<Double> sign(double[] arr){
        int size = arr.length;
        ArrayList<Double> answer = new ArrayList<Double>();
        for (double element : arr){
            if (element < 0.0){
                answer.add(-1.0);
            } else if (element > 0.0){
                answer.add(1.0);
            } else { // if element == 0
                answer.add(0.0);
            }
        }
        return answer;
    }

    private static ArrayList<Double> diff(ArrayList<Double> arr){
        int size = arr.size();
        ArrayList<Double> answer = new ArrayList<Double>();
        for (int i=0; i<size-1; i++){
            answer.add( arr.get(i+1) - arr.get(i) );
        }
        return answer;
    }

    private static ArrayList<Double> abs(ArrayList<Double> arr){
        int size = arr.size();
        ArrayList<Double> answer = new ArrayList<Double>();
        for (int i=0; i<size; i++){
            answer.add( Math.abs(arr.get(i)) );
        }
        return answer;
    }


}
