package com.example.sevak.themaze;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MazeHolder.init(getApplicationContext());

        ImageView start = (ImageView) findViewById(R.id.StartGame);
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SingleplayerVilka.class));
            }
        });
        ImageView levelb = (ImageView) findViewById(R.id.LevelBuilder);
        levelb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditMapShlus.class));
            }
        });
        ImageView multiplayer = (ImageView) findViewById(R.id.StartMultiplayer);
        multiplayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Client.class));
            }
        });
    }
}
