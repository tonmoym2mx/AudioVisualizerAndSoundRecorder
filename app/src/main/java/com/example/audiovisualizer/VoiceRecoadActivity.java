package com.example.audiovisualizer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.tonmoym2mx.audiovisualizer.AudioVisualizer;
import com.tonmoym2mx.audiovisualizer.Utility.AudioPlayerDialog;
import com.tonmoym2mx.audiovisualizer.Utility.AudioRecorder;
import com.tonmoym2mx.audiovisualizer.Utility.MaxAmplitudeListener;
import com.tonmoym2mx.audiovisualizer.Utility.TimerUpdateListener;


import java.io.IOException;
import java.text.SimpleDateFormat;

//import android.media.MicrophoneInfo;

public class VoiceRecoadActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 345;
    private Button recoad,stop,play;
    private MediaRecorder mediaRecorder;
    private MediaPlayer player;
    private ProgressBar progressBar;
    private Button check;
    private AudioVisualizer visualizer;
    private String filePath;
    private boolean isRecoad;
    private boolean isSave;

    //for mediaPlayer aleartDialog
   private ImageButton CloseButton;
   private FloatingActionButton playStop;
   private SeekBar seekBar;
   private TextView totalTime;
   private TextView currentTime;

    private FloatingActionButton floatingActionButton,floatingActionButtonPlay;
    private TextView statusText;
    private TextView timeCountText;
    private int togolCount=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recoad);
        floatingActionButton = findViewById(R.id.floatingActionButton_recoder);
        floatingActionButtonPlay = findViewById(R.id.floatingActionButtonPLAY);
        statusText = findViewById(R.id.recodText);
        timeCountText = findViewById(R.id.textView_timeCount);


        visualizer = findViewById(R.id.visualizer);
        if(isPermission()) {
            init();
        }else {
            requestPermissions();
        }


    }


    private void init(){

        filePath =getExternalCacheDir().getAbsolutePath()+"/audio.mp3";
        final AudioRecorder audioRecorder = new AudioRecorder(VoiceRecoadActivity.this,filePath);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!audioRecorder.isRecord){

                    visualizer.reset();
                    audioRecorder.startRecord();

                    floatingActionButton.setImageResource(R.drawable.ic_stop_black_24dp);
                }else {

                    audioRecorder.stopRecord();

                    floatingActionButton.setImageResource(R.drawable.ic_mic_black_24dp);
                }
            }
        });
        audioRecorder.setMaxAmplitudeListener(new MaxAmplitudeListener() {
            @Override
            public void getMaxAmplitude(int amplitude) {
                visualizer.addAmplitude(amplitude);
            }
        },50);
        audioRecorder.setTimerUpdateListener(new TimerUpdateListener() {
            @Override
            public void onTimeChange(long time) {
               String timeCount = new SimpleDateFormat("mm:ss").format(time);
               timeCountText.setText(timeCount);
            }
        });

        floatingActionButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (audioRecorder.isSave && !audioRecorder.isRecord) {
                    AudioPlayerDialog audioPlayerDialog = new AudioPlayerDialog(VoiceRecoadActivity.this,filePath);
                    audioPlayerDialog.show();
                }else {
                    Toast.makeText(VoiceRecoadActivity.this, "Record First", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean isPermission(){
        if ((ContextCompat.checkSelfPermission(VoiceRecoadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                &&
                (ContextCompat.checkSelfPermission(VoiceRecoadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                &&
                (ContextCompat.checkSelfPermission(VoiceRecoadActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        ) {
            return true;
        }else {
            return  false;
        }
    }
    private void requestPermissions(){
        if (!isPermission()){
            ActivityCompat.requestPermissions(VoiceRecoadActivity.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
            },REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==REQUEST_CODE){
            if(isPermission()) {
                init();
            }else {
                requestPermissions();
            }
        }
    }
}
