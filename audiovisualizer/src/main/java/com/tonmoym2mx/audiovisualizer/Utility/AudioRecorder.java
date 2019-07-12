package com.tonmoym2mx.audiovisualizer.Utility;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

public class AudioRecorder {
    private Context context;
    private MediaRecorder mediaRecorder;
    private String filePath;
    public boolean isRecord =false;
    public boolean isSave =false;
    private long delayMillis =-1;
    private MaxAmplitudeListener listener;
    private long startTime=0;
    private TimerUpdateListener timerUpdateListener;

    public AudioRecorder(Context context, String filePath) {
        this.context = context;
        this.filePath = filePath;
    }

    public void startRecord(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
        }

        if(!isRecord) {
            mediaRecorder.start();
            isRecord = true;
            startTime = new Date().getTime();
            timeUpdate();
            progControl();
        }else {
            Toast.makeText(context, "Recoad is already start", Toast.LENGTH_SHORT).show();
        }
       
    }

    private void timeUpdate() {
        if(timerUpdateListener !=null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isRecord && mediaRecorder != null) {
                        timerUpdateListener.onTimeChange(new Date().getTime()-startTime);
                        timeUpdate();
                    }
                }
            }, 1000);
        }
    }

    public void stopRecord(){
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
        isRecord = false;
        isSave =true;
        startTime =0;
    }
    private void progControl(){
        if(listener !=null && delayMillis !=-1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isRecord && mediaRecorder != null) {
                        listener.getMaxAmplitude(mediaRecorder.getMaxAmplitude());
                        progControl();
                    }
                }
            }, delayMillis);
        }
    }

    public void setMaxAmplitudeListener(MaxAmplitudeListener listener,long delayMillis) {
        this.delayMillis = delayMillis;
        this.listener = listener;
    }

    public void setTimerUpdateListener(TimerUpdateListener timerUpdateListener) {
        this.timerUpdateListener = timerUpdateListener;
    }
}
