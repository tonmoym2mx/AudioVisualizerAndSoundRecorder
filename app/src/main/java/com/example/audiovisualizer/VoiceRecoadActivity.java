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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.audiovisualizer.views.AudioVisualizer;

import java.io.IOException;

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
        recoad = (Button)findViewById(R.id.recoadBtn);
        stop = (Button)findViewById(R.id.stopBtn);
        play =(Button) findViewById(R.id.playBtn);
        check = (Button)findViewById(R.id.checkbtn);
        if(isPermission()) {
            init();
        }else {
            requestPermissions();
        }


    }


    private void init(){
        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton_recoder);
        floatingActionButtonPlay = (FloatingActionButton)findViewById(R.id.floatingActionButtonPLAY);
        statusText = (TextView)findViewById(R.id.recodText);
        timeCountText = (TextView)findViewById(R.id.textView_timeCount);
        visualizer = (AudioVisualizer)findViewById(R.id.visualizer);
        filePath =getExternalCacheDir().getAbsolutePath()+"/audiorecordtest.mp3";
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRecoad){

                    recoad_();
                    floatingActionButton.setImageResource(R.drawable.ic_stop_black_24dp);
                }else {
                    stop_();
                    floatingActionButton.setImageResource(R.drawable.ic_mic_black_24dp);
                }
            }
        });
        floatingActionButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSave && !isRecoad) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(VoiceRecoadActivity.this);
                    View view = getLayoutInflater().inflate(R.layout.audioplaylayout, null);
                    alertDialog.setView(view);
                    CloseButton = view.findViewById(R.id.closeButton);
                    playStop = view.findViewById(R.id.playStopButton);
                    seekBar = view.findViewById(R.id.seekBarAudio);
                    totalTime = view.findViewById(R.id.currentText);
                    currentTime = view.findViewById(R.id.totalTimeText);
                    play_();
                    int total = player.getDuration();
                    String totalStr = String.format("%d:%d", total / 1000 / 60, total / 1000 % 60);
                    totalTime.setText(totalStr);

                    playStop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (togolCount % 2 == 0) {
                                // player.seekTo();
                                player.start();
                                updatePlay();
                                playStop.setImageResource(R.drawable.ic_pause_black_24dp);
                            } else {
                                player.pause();
                                playStop.setImageResource(R.drawable.ic_play_arrow_black_24dp);

                            }
                            togolCount++;
                        }
                    });
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) {
                                player.seekTo(progress);
                            }

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    final AlertDialog ad = alertDialog.show();
                    CloseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (player != null) {
                                player.stop();
                                player.release();
                                player = null;
                            }
                            togolCount = 2;
                            ad.dismiss();
                        }
                    });


                }else {
                    Toast.makeText(VoiceRecoadActivity.this, "No audio Record", Toast.LENGTH_SHORT).show();
                }
            }
        });





        recoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoad_();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop_();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check.setText(String.valueOf(mediaRecorder.getMaxAmplitude()));

            }
        });
    }
    private void updatePlay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(player !=null && player.isPlaying()) {
                    int total = player.getDuration();
                    int current = player.getCurrentPosition();
                    String totalStr = String.format("%d:%d",total/1000/60,total/1000%60);
                    String currentStr = String.format("%d:%d",current/1000/60,current/1000%60);
                    seekBar.setMax(total);
                    seekBar.setProgress(current);

                    totalTime.setText(totalStr);
                    currentTime.setText(currentStr);
                    updatePlay();
                }
            }
        },100);
    }
    private void progControl(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isRecoad && mediaRecorder !=null) {
                    int a = mediaRecorder.getMaxAmplitude();
                    visualizer.addAmplitude(a);
                    check.setText(String.valueOf(a));
                    progControl();
                }
            }
        },40);
    }
    private void recoad_(){
        visualizer.reset();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {

        }

        mediaRecorder.start();
        isRecoad = true;
        statusText.setText("Audio Recording");
        progControl();
        mCountTimer.start();
    }
    private void stop_(){
        mediaRecorder.reset();
        mediaRecorder.release();
        isRecoad =false;
        mediaRecorder = null;
        isSave = true;
        statusText.setText("Stop");
        mCountTimer.cancel();
    }
    private boolean play_(){
        player = new MediaPlayer();
        try {
            player.setDataSource(filePath);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //player.start();
        return player.isPlaying();
    }
    int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    private CountDownTimer mCountTimer = new CountDownTimer(180000, 1000) {

        public void onTick(long millisUntilFinished) {
            int millis =(int) millisUntilFinished;
            int second = (int) millisUntilFinished/1000;
            int minute = (int) second/60;
            second = second %60;

            timeCountText.setText(String.format("%d:%02d",minute,second));

        }

        public void onFinish() {
            stop_();
            floatingActionButton.setImageResource(R.drawable.ic_mic_black_24dp);
        }

    };
    private void audioAdd(){
        if(isSave && !isRecoad){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("audioPath", filePath);
            setResult(RESULT_OK,resultIntent);
            finish();
        }else {
            Toast.makeText(this, "Record First", Toast.LENGTH_SHORT).show();
        }
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
