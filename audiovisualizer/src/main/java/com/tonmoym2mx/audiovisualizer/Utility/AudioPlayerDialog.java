package com.tonmoym2mx.audiovisualizer.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.tonmoym2mx.audiovisualizer.R;


import java.io.IOException;

public class AudioPlayerDialog {

    private Context context;
    private String filePath;

    private MediaPlayer player;
    private ImageButton CloseButton;
    private FloatingActionButton playStop;
    private SeekBar seekBar;
    private TextView totalTime;
    private TextView currentTime;

    private int togolCount=2;

    public AudioPlayerDialog(Context context, String filePath) {
        this.context = context;
        this.filePath = filePath;
        this.player = new MediaPlayer();
    }

    public void show() {
        if (filePath != null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            View view = ((Activity)context).getLayoutInflater().inflate(R.layout.audioplaylayout, null);
            alertDialog.setView(view);
            CloseButton = view.findViewById(R.id.closeButton);
            playStop = view.findViewById(R.id.playStopButton);
            seekBar = view.findViewById(R.id.seekBarAudio);
            totalTime = view.findViewById(R.id.currentText);
            currentTime = view.findViewById(R.id.totalTimeText);
            try {
                player.setDataSource(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
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


        }else
        {
            Toast.makeText(context, "No file Found", Toast.LENGTH_SHORT).show();
        }
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
        },50);
    }
}
