package com.example.filetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "abc";
    private static String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private TextView textView;
    private EditText inputEdit;
    private String tempText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button loadInput = findViewById(R.id.load_input);
        Button saveInput = findViewById(R.id.save_input);
        Button saveData = findViewById(R.id.save_data);
        Button restoreData = findViewById(R.id.restore_data);
        Button saveToSD =findViewById(R.id.save_to_sd);
        Button readSD = findViewById(R.id.read_sd_string);

        textView = findViewById(R.id.text_view);
        inputEdit = findViewById(R.id.input_edit);

        saveInput.setOnClickListener(this);
        loadInput.setOnClickListener(this);
        saveData.setOnClickListener(this);
        restoreData.setOnClickListener(this);
        saveToSD.setOnClickListener(this);
        readSD.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        } else {
//          doJob();
        }
        FileOfSD file=new FileOfSD();
        Log.d(TAG, "onCreate: "+file.getSdCardPath());
        file.write("/storage/emulated/0/data.txt");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    doJob();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_input:
                tempText = inputEdit.getText().toString();
                save(tempText);
                break;
            case R.id.load_input:
                tempText = load2();
                if (!TextUtils.isEmpty(tempText)) {
                    inputEdit.setText(tempText);
                    inputEdit.setSelection(tempText.length());
                    Toast.makeText(this, "Restoring succeeded!!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.save_data:
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("name", "Tom");
                editor.putInt("age", 28);
                editor.putBoolean("married", false);
                editor.apply();
                break;
            case R.id.restore_data:
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                Log.d(TAG, "name is " + pref.getString("name", ""));
                Log.d(TAG, "age is " + pref.getInt("age", 0));
                Log.d(TAG, "married is " + pref.getBoolean("married", false));
                break;
            case R.id.save_to_sd:
                tempText = inputEdit.getText().toString();
                break;
            case R.id.read_sd_string:
                tempText = new FileOfSD().read();
                if (!TextUtils.isEmpty(tempText)) {
                    inputEdit.setText(tempText);
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //保存输入文字到data中
    private void save(String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            //MODE_PRIVATE-覆盖模式
            // MODE_APPEND-添加模式
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //读取程序数据到输入框，有回车bug
    private String load() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            //readLine每次只能读一行
            while ((line = reader.readLine()) != null) {
                content.append(line);
                content.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    //作用同toad，但是load2需要预先设置好大小
    private String load2() {
        FileInputStream in = null;
        Reader reader = null;
        String content = "";
        try {
            in = openFileInput("data");
            reader = new InputStreamReader(in);
            char data[] = new char[1024];
            int len = reader.read(data);
            content = new String(data, 0, len);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

}


