package com.example.clapping_audio_feature_engineering;

// TODO 1. 안드로이드에서 WAV 파일 READ
// TODO 2. 각 WAV 파일을 50ms 단위 Window로 쪼개어 Audio Data List 생성
//  (List 생성 시 타겟 소리가 발생한 window는 따로 Index표시)  // --> 10개의 list가 생기겠지


import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class ReadingWavFiles {

    private static AssetManager AM = null;
    private static String audioFile = null; // "./AudioFiles/1.pcm"형태로 생성자를 통해 들어갈것임.

    private static ArrayList<double[]> arrOfDoubleArrs = new ArrayList<double[]>();
    private static ArrayList<ArrayList<double[]>> arrOfarrOfDoubleArrs = new ArrayList<ArrayList<double[]>>();

    private static final double MAX_16_BIT = Short.MAX_VALUE; // 32,767
    private static final int SAMPLE_RATE = 16000;
    // 16000
    private static final int ELEMENT_NUM_PER_WINDOW = 800;


    // 생성자
    ReadingWavFiles(AssetManager am, String file_name){ // constructor
        AM = am;
        audioFile = file_name;
    }



    // (1) read the audio file into byte array
    private static byte[] read_file(String filePath) throws IOException
    {
        if (filePath==null) return null;
        //Reading the file..
        byte[] byteData = null;

        InputStream in = null;

        try {
            // in = afd.createInputStream();
            in = AM.open(filePath);
            int size = in.available(); // 153600
            // System.out.println(size);
            byteData = new byte[size];
            // in = new FileInputStream( file );
            in.read( byteData ); // the byte array is now stored in byteData variable
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return byteData;
    }

    // (2) byte arr --> int arr
    private static short[] Byte2Short(byte[]src) {
        // ShortBuffer shortbuf = ByteBuffer.wrap(src).order(ByteOrder.BIG_ENDIAN).asShortBuffer();
        ShortBuffer shortbuf = ByteBuffer.wrap(src).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short [] dst = new short[shortbuf.remaining()];
        shortbuf.get(dst);
        return dst;
    }

    // (3) int arr --> short arr
    private static short[] Int2Short(int[] arr){
        int len = arr.length;
        short inShort[] = new short[len];
        for(int i = 0; i < len; i++)
        {
            inShort[i] = (short)arr[i];
        }
        return inShort;
    }

    // (4) convert short array -> to double array
    private static double[] Short2Double(short[] arr) {
        double[] out = new double[arr.length]; // will drop last byte if odd number
        for (int i = 0; i < out.length; i++) {
            out[i] = (double) arr[i];
        }
        return out;
    }


    // (4) chunk the extracted array in 50ms size window
    // --> (sample rate * 0.05) = ELEMENT_NUM_PER_WINDOW 개씩 element 를 뽑아내서 나누면 됨!
    // 따라서 output 은 array of double arrays 형태가 됨.
    // --> 각 audio file 의 길이에 따라 해당 array의 길이도 달라질 수 있음. (element 크기는 ELEMENT_NUM_PER_WINDOW로 동일)

    private static Map.Entry<Integer,ArrayList<double[]>> ChunkByWindows(double[] arr){
        ArrayList<double[]> result = new ArrayList<double[]>();
        int loop_cnt = arr.length/ ELEMENT_NUM_PER_WINDOW; // loop를 몇번돌지 (chunk 몇개를 만들지) 결정

        double max_val = arr[0]; // 여기에 max값을
        int max_idx = 0;// 여기에 max값이 있는 윈도우의 idx값을

        int cursor = 0;
        for (int i=0; i<loop_cnt; i++) {
            // (ex) 4개씩 나누고 싶으면 a[0]~a[3], a[4]~a[7]
            result.add(Arrays.copyOfRange(arr, cursor, cursor + ELEMENT_NUM_PER_WINDOW  ));
            cursor+=ELEMENT_NUM_PER_WINDOW;

            // event 발생 window를 체크해주기 위한 코드
            double[] d = Arrays.copyOf(result.get(i), result.get(i).length);
            Arrays.sort(d);
            double local_max = d[d. length-1];
            if (max_val <= local_max){
                max_val = local_max;
                max_idx = i;
            }

        }
        /*
        List b = Arrays.asList(arr);
        Collections.max(b);
         */
        return new AbstractMap.SimpleEntry<Integer,ArrayList<double[]>>(max_idx, result);
        // return result;
    }


    // MAIN FUNCTION // --------------------------------------------------------------------
    // --> 본 클래스 외부에서는 이 function을 통해서만 결과값에 접근할 수 있음!!

    // 여기서 모든 작업들이 일어나서 최종적으로 계산된 double array를 반환해주면 됨!!
    public Map.Entry<Integer,ArrayList<double[]>> GetFFTInputFormat() throws IOException {
        return ChunkByWindows(Short2Double(Byte2Short(read_file(audioFile))));
    }


}
