package com.example.filetest;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileOfSD {
    private static String TAG = "abc";

    //如果public后面加static则为全局静态变量，外部文件引用FileOfSD.isSdCardExist()
    public boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public String getSdCardPath() {
        boolean exist = this.isSdCardExist();
        String sdpath = "";
        if (exist) {
            sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            sdpath = "不适用";
        }
        return sdpath;
    }


    public String read() {
//        File file = new File("/storage/emulated/0","data.txt");
        File file = new File(Environment.getExternalStorageDirectory(), "data.txt");
        String output = "";
        try {
            String str = "";
            InputStream in = new FileInputStream(file);
            InputStreamReader input = new InputStreamReader(in, "UTF-8");
            BufferedReader reader = new BufferedReader(input);
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            while ((str = reader.readLine()) != null) {
                str += '\n';
                output += str;
            }

            Log.d(TAG, "str: " + output);
            Log.d(TAG, Environment.getExternalStorageDirectory().getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public void write(String DEFAULT_FILENAME){
        try {
            File file = new File(Environment.getExternalStorageDirectory(),DEFAULT_FILENAME);
            FileOutputStream fos = new FileOutputStream(file);
            String info = "I am a chinanese!";
            fos.write(info.getBytes());
            fos.close();
            System.out.println("写入成功：");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
