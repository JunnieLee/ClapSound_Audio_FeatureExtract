package com.example.clapping_audio_feature_engineering;

// TODO 1. 안드로이드에서 WAV 파일 READ
// TODO 2. 각 WAV 파일을 50ms 단위 Window로 쪼개어 Audio Data List 생성
//  (List 생성 시 타겟 소리가 발생한 window는 따로 Index표시)  // --> 10개의 list가 생기겠지

/*
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
*/

import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class ReadingWavFiles {

    private static final double MAX_16_BIT = Short.MAX_VALUE; // 32,767
    private static final int SAMPLE_RATE = 48000;
    private static final int ELEMENT_NUM_PER_WINDOW = 2400;
    // 모두 4초 미만의 오디오 데이터이므로, 한 오디오 파일 당 max window num은 (48000*4) / 2400 = 80
    private static final int MAX_WINDOW_NUM_PER_FILE = 80;

    // 고민: 오디오파일 1~10까지 하나의 array 에 담을 것인가 (fancier code) 아니면 10개의 variable 을 따로 만들것인가(좀 노가다스럽지만 복잡도 감소)



    // ** 고민거리 : constructor 에서 모든 데이터들에 대해 아래 과정이 다 되도록 만들까?!

    // --> 모든 데이터들에 대해 아래 procedure 를 모두 실행하는 함수는 또 따로 만들자


    // (1) read the audio file into byte array
    private byte[] read_file(String filePath) throws IOException
    {
        if (filePath==null) return null;
        //Reading the file..
        byte[] byteData = null;
        File file = null;
        file = new File(filePath); // for ex. path= "/sdcard/samplesound.pcm"
        byteData = new byte[(int) file.length()];
        FileInputStream in = null;
        try {
            in = new FileInputStream( file );
            in.read( byteData ); // the byte array is now stored in byteData variable
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return byteData;
    }

    // 16bit PCM audio file이기 때문에 byte array -> short array -> double array 로 바꿔줘야함

    // (2) convert byte array -> to short array
    private static short[] Byte2Short(byte[] bytes) {
        short[] out = new short[bytes.length / 2]; // will drop last byte if odd number
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        for (int i = 0; i < out.length; i++) {
            out[i] = bb.getShort();
        }
        return out;
    }

    // (3) convert short array -> to double array
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

    private static double[][] ChunkByWindows(double[] arr){
        double[][] result = new double[MAX_WINDOW_NUM_PER_FILE][ELEMENT_NUM_PER_WINDOW];
        int loop_cnt = arr.length/ ELEMENT_NUM_PER_WINDOW; // loop를 몇번돌지 (chunk 몇개를 만들지) 결정
        int cursor = 0;
        for (int i=0; i<loop_cnt; i++) {
            // (ex) 4개씩 나누고 싶으면 a[0]~a[3], a[4]~a[7]
            result[i] = Arrays.copyOfRange(arr, cursor, cursor + ELEMENT_NUM_PER_WINDOW -1 );
            cursor++;
        }
        return result;
    }



    // HELPER FUNCTIONS // --------------------------------------------------------------------



}
