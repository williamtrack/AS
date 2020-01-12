package com.example.playaudiotest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private TextView textView;
    private EditText editText;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private static String TAG = "abc";
    private void doJob() {
        initText();
//        load();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Pro Begin");

        textView = (TextView) findViewById(R.id.read_edit);
        editText=(EditText)findViewById(R.id.save_edit);

        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        Button stop = (Button) findViewById(R.id.stop);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        } else {
            doJob();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doJob();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }


    private void initText() {
        String inputText = new ReadSDString().load();
//        String inputText="abc\nabc";
        if (!TextUtils.isEmpty(inputText)) {
//            textView.setText(inputText);
            textView.append(inputText);
        }
    }



    /**
     * @date 09/18/19
     * @author William
     * @Description 初始化MediaPlayer
    */
    private void initMediaPlayer() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "music.mp3");
            Log.d(TAG, "The directory is: " + file.getPath());
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception.");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    Log.d(TAG, "play ");
                }
                break;
            case R.id.pause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    Log.d(TAG, "pause ");
                }
                break;
            case R.id.stop:
                mediaPlayer.reset();
                Log.d(TAG, "stop ");
                initMediaPlayer();
                break;
            default:
                Log.d(TAG, "default ");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void load() {
//        File file = new File("/storage/emulated/0","data.txt");
        File file = new File(Environment.getExternalStorageDirectory(),"data.txt");
        String str = null;
        try {
            InputStream is = new FileInputStream(file);
            InputStreamReader input = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(input);
            while ((str = reader.readLine()) != null) {
                textView.append(str);
                textView.append("\n");
                }
            Log.d(TAG, "load: ");
            Log.d(TAG, Environment.getExternalStorageDirectory().getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
