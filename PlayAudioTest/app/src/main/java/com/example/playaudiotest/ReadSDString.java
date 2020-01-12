package com.example.playaudiotest;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ReadSDString {
    private static String TAG = "abc";

    public String load() {
//        File file = new File("/storage/emulated/0","data.txt");
        File file = new File(Environment.getExternalStorageDirectory(),"data.txt");
        String output="";
        try {
            String str ="";
            InputStream in = new FileInputStream(file);
            InputStreamReader input = new InputStreamReader(in, "UTF-8");
            BufferedReader reader = new BufferedReader(input);
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            while ((str = reader.readLine()) != null) {
                str+='\n';
                output+=str;
            }

            Log.d(TAG, "str: "+output);
            Log.d(TAG, Environment.getExternalStorageDirectory().getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
