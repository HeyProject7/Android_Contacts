package com.example.playme;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    TextView song;
    ImageView prev,play,next;
    MediaPlayer mp;
    // Intent Values
    ArrayList<File> songs;
    String textContent;
    int position;
    Thread updateSeek;
    SeekBar seekBar;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
    }

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        song = findViewById(R.id.songname);
        prev=findViewById(R.id.prev);
        play=findViewById(R.id.play);
        next=findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);

        // GEtting Intent
        Intent myIntent = getIntent();
        Bundle b = myIntent.getExtras();
        songs = (ArrayList)b.getParcelableArrayList("mySongs");
        textContent = myIntent.getStringExtra("currentSong");
        song.setText(textContent);
        position = myIntent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mp = MediaPlayer.create(this,uri);
        mp.start();
        seekBar.setMax(mp.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //mp.seekTo(pos);-> Thread
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
        updateSeek = new Thread() {
            public void run() {
                int currentPos = 0;
                try {
                    while(currentPos<mp.getDuration()){
                        currentPos=mp.getDuration();
                        seekBar.setProgress(currentPos);
                        sleep(1000);
                    }
                }catch(Exception e){
                    Toast.makeText(MainActivity2.this, "Playing Wait...", Toast.LENGTH_SHORT).show();
                }
            }
        };
        updateSeek.start();

        // Click Events
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp.isPlaying()){
                    play.setImageResource(R.drawable.play);
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mp.start();
                }
            }
        });
        next.setOnClickListener(v-> {

            });
    }
}