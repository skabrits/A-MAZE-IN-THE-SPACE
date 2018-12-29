package com.example.sevak.themaze;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import com.google.gson.Gson;

public class Catsciene extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catsciene);

        findViewById(R.id.rev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Campaign.class));
            }
        });

        Gson gson = new Gson();
        ((VideoView) findViewById(R.id.videoView)).setVideoURI(Uri.parse(LevelHolderCampaign.LevelArr.get(CscHolder.videoNum).data));
        ((VideoView) findViewById(R.id.videoView)).start();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!((VideoView) findViewById(R.id.videoView)).isPlaying()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (true) {
                    if (!((VideoView) findViewById(R.id.videoView)).isPlaying()) {
                        SharedPreferences sharedPreferences = getSharedPreferences("Mylvl", MODE_PRIVATE);
                        int Mylvl = Math.max(gson.fromJson(sharedPreferences.getString("Mylvl", null), int.class), CscHolder.videoNum + 1);
                        SharedPreferences.Editor ed = sharedPreferences.edit();
                        ed.putString("Mylvl", gson.toJson(Math.min(Mylvl, LevelHolderCampaign.LevelArr.size() - 1)));
                        ed.apply();
                        startActivity(new Intent(getApplicationContext(), Campaign.class));
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        thread.start();
    }
}
