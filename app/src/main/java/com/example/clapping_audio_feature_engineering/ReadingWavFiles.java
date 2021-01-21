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


public class ReadingWavFiles {

    private static final double MAX_16_BIT = Short.MAX_VALUE; // 32,767

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


    // (2) convert byte array -> to double array
    private static double[] read(byte[]file) {
        byte[] data = new byte[file.length];
        int N = data.length;
        double[] d = new double[N/2];
        for (int i = 0; i < N/2; i++) {
            d[i] = ((short) (((data[2*i+1] & 0xFF) << 8) + (data[2*i] & 0xFF))) / ((double) MAX_16_BIT);
        }
        return d;
    }

    // (3) chunk the extracted array in 50ms size window
    // --> (sample rate * 0.05) 개씩 element 를 뽑아내서 나누면 됨!




    // HELPER FUNCTIONS // --------------------------------------------------------------------

    // get sample rate for calculating window size for 50ms chunk
    private int getSampleRate(String path) throws IOException {
        MediaExtractor mex = new MediaExtractor();
        try {
            mex.setDataSource(path);// the addresss location of the sound on sdcard.
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MediaFormat mf = mex.getTrackFormat(0);
        // int bitRate = mf.getInteger(MediaFormat.KEY_BIT_RATE);
        int sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        return sampleRate;
    }


}
