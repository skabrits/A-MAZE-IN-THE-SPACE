package com.example.sevak.themaze;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Game extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.Layout);

        for (int i = 0; i < ((Maze.Maze.length-1)/2); i++) {
            for (int j = 0; j < ((Maze.Maze[1].length-1)/2); j++) {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.walls0);
                RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
                rules.setMargins(400+140*j, 400+140*i, 0, 0);
                layout.addView(imageView, rules);
            }
        }

        Button SP = (Button) findViewById(R.id.SP);
        SP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), StartPage.class));
            }
        });
        ImageButton Return = (ImageButton) findViewById(R.id.Return);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
