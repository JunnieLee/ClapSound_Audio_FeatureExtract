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

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;


public class ReadingWavFiles {

    private static AssetManager AM = null;
    private static String audioFile = null; // "./AudioFiles/1.pcm"형태로 생성자를 통해 들어갈것임.

    private static ArrayList<double[]> arrOfDoubleArrs = new ArrayList<double[]>();
    private static ArrayList<ArrayList<double[]>> arrOfarrOfDoubleArrs = new ArrayList<ArrayList<double[]>>();

    private static final double MAX_16_BIT = Short.MAX_VALUE; // 32,767
    private static final int SAMPLE_RATE = 16000;
    // 16000
    private static final int ELEMENT_NUM_PER_WINDOW = 800;


    // ** 고민거리 1 : constructor 에서 모든 데이터들에 대해 아래 과정이 다 되도록 만들까?!
    // ** 결론: X. 생성자에서는 그냥 과정에 필요한 데이터들만 넣어주고, over-all 함수는 그냥 MAIN으로 하나 따로 만들자!

    // 고민거리 2: 오디오파일 1~10까지의 정보를 하나의 array 에 담을 것인가 (fancier code)
    // 아니면 10개의 variable 을 따로 만들것인가(좀 노가다스럽지만 복잡도 감소)
    // ** 결론: 전자!!



    // 생성자
    ReadingWavFiles(AssetManager am, String file_name){ // constructor
        AM = am;
        audioFile = file_name;
    }


    // --> 모든 데이터들에 대해 아래 procedure 를 모두 실행하는 함수는 또 따로 만들자
    // OVERALL RUN FUNCTION --> 맨 아래에 GetDoubleArray라는 이름으로 만들었음


    // (1) read the audio file into byte array
    private static byte[] read_file(String filePath) throws IOException
    {
        if (filePath==null) return null;
        //Reading the file..
        byte[] byteData = null;

        // AssetFileDescriptor afd = AM.openFd(filePath);
        // int size = (int) afd.getLength();
        // byteData = new byte[size]; // 153600
        // System.out.println("array size :"+size);

        InputStream in = null;
        // FileInputStream in = null;
        // for debugging
        //byteData[0] = 1;
        //System.out.println("print the byteData [BEFORE] ");
        //System.out.println(Arrays.toString(byteData));
        //System.out.println("----------------------------------------------------------------------------------------------");

        /* SD 카드 내에 존재하는 데이터에 대해서는 이렇게 할 수 있음...
        final String strFolderName = "AudioFiles/"+filePath;
        final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + strFolderName;
        File file = new File(SD_PATH);
        */

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
        ShortBuffer shortbuf = ByteBuffer.wrap(src).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short [] dst = new short[shortbuf.remaining()];
        shortbuf.get(dst);

        int len = dst.length;
        int i=0;
        while (dst[i]==0){
            i++;
        } // 이 loop을 빠져나올때의 i값이 첫번째로 0 이 아닌 값의 idx

        /*
        for (short k : dst){
            if (k != 0)
                System.out.println("test" + k);
        }
         */
        short[] slice = Arrays.copyOfRange( dst, i, len-1); // 엥... 왜 안되는겨 ㅠㅠㅠ

        return slice;
        // return dst;

//        int dstLength = src.length >>> 2;
//        int[]dst = new int[dstLength];
//
//        for (int i=0; i<dstLength; i++) {
//            int j = i << 2;
//            int x = 0;
//            x += (src[j++] & 0xff) << 0;
//            x += (src[j++] & 0xff) << 8;
//            x += (src[j++] & 0xff) << 16;
//            x += (src[j++] & 0xff) << 24;
//            dst[i] = x;
//        }
//        return dst;
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

    private static ArrayList<double[]> ChunkByWindows(double[] arr){
        ArrayList<double[]> result = new ArrayList<double[]>();
        int loop_cnt = arr.length/ ELEMENT_NUM_PER_WINDOW; // loop를 몇번돌지 (chunk 몇개를 만들지) 결정
        int cursor = 0;
        for (int i=0; i<loop_cnt; i++) {
            // (ex) 4개씩 나누고 싶으면 a[0]~a[3], a[4]~a[7]
            result.add(Arrays.copyOfRange(arr, cursor, cursor + ELEMENT_NUM_PER_WINDOW -1 ));
            cursor++;
        }
        return result;
    }


    // MAIN FUNCTION // --------------------------------------------------------------------
    // --> 본 클래스 외부에서는 이 function을 통해서만 결과값에 접근할 수 있음!!

    // 여기서 모든 작업들이 일어나서 최종적으로 계산된 double array를 반환해주면 됨!!
    public ArrayList<double[]> GetFFTInputFormat() throws IOException {

        return ChunkByWindows(Short2Double(Byte2Short(read_file(audioFile))));

    }


    // TESTING FUNCTION // -----------------------------------------------------------------

    
    /* [1] 전체 process 다 마치고 한꺼번에 print 확인하기
    public static void main(String[] args) throws IOException{

        for (int i=0; i<10; i++){
            audioFiles[i] = "src/PCM/"+(i+1)+".pcm";
        } // audio file 배열에 file path 채워넣기

        int i = 1;
        ArrayList<ArrayList<double[]>> helperArr = GetDoubleArray();
        for (ArrayList<double[]> arr_x : helperArr){
            System.out.println("no."+ (i++) + " PCM file in array of double arrays:");
            System.out.println("[");
            for (double[]arr_y:arr_x){
            System.out.println(Arrays.toString(arr_y));
            }
             System.out.println("]");
        }
     */
    
    
    
    /* [2] process 하나하나씩 print 하기
    public static void main(String[] args) throws IOException{

        // (1) testing function "read_file"
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("PCM file in a byte array:");
        System.out.print(Arrays.toString(read_file("src/PCM/1.pcm")));
        // (2) testing function "Byte2Short" ====> no longer valid
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("PCM file in a short array:"); // 지금 왜인진 모르겠지만 이 위치의 한 줄이 잡아먹히고 있음
        System.out.println("PCM file in a short array:");
        System.out.println(Arrays.toString(Byte2Short(read_file("src/PCM/1.pcm"))));

        // (3) testing function "Short2Double"
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("PCM file in a double array:");
        System.out.println(Arrays.toString(Short2Double(Byte2Short(read_file("src/PCM/1.pcm")))));

        // (4) testing function "ChunkByWindows"
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("PCM file in sliced double arrays:");
        ArrayList<double[]> tmp = ChunkByWindows(Short2Double(Byte2Short(read_file("src/PCM/1.pcm"))));
        System.out.println("[");
        for (double[]arr:tmp){
            System.out.println(Arrays.toString(arr));
        }
        System.out.println("]");

    }
     */


}
