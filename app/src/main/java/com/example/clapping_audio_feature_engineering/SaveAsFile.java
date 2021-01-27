package com.example.clapping_audio_feature_engineering;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class SaveAsFile {

    public void save(String file_name, String content){
        /////////////////////// 파일 쓰기 ///////////////////////
        // 파일 생성
        File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FFTdata"); // 저장 경로
        // 폴더 생성
        if(!saveFile.exists()){ // 폴더 없을 경우
            saveFile.mkdir(); // 폴더 생성
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile+"/"+file_name+ ".txt", true));
            buf.append(content); // 파일 쓰기
            buf.newLine(); // 개행
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
