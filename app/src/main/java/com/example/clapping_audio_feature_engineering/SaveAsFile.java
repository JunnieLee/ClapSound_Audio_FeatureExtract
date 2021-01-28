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
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/JUNNIE"); // 저장 경로
        // 폴더 생성
        Boolean success = false;
        if(!root.exists()){ // 폴더 없을 경우
            success = root.mkdirs(); // 폴더 생성
        }
        System.out.println("Success? : "+ success);
        System.out.println("Path :" + Environment.getExternalStorageDirectory().getAbsolutePath());
        try {
            File file = new File(root+"/"+file_name +".txt");

            FileWriter writer = new FileWriter(file, true);
            writer.append(content);
            writer.flush();
            writer.close();

            /*
            BufferedWriter buf = new BufferedWriter(new FileWriter(root+"/"+file_name+ ".txt", true));
            buf.write(content);
            buf.write("\n");
            // buf.append(content); // 파일 쓰기
            buf.newLine(); // 개행
            buf.flush();
            buf.close();
             */
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("SAVING NOT WORKING!!!!!!!!!!!!!!!!!!");
            e.printStackTrace();
        }
    }
}
