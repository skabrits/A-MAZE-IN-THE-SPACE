package com.example.sevak.themaze;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import static com.example.sevak.themaze.StartPage.CELLSIZE;
import static com.example.sevak.themaze.StartPage.OFFSET_LEFT;
import static com.example.sevak.themaze.StartPage.OFFSET_TOP;

public class Predbannik extends AppCompatActivity {

    private int chmap = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predbannik);

        LinearLayout mc = (LinearLayout) findViewById(R.id.MapChooser);

        mc.removeAllViews();

        for (int i = 0; i < MazeHolder.MazeArr.size(); i++) {
            TextView txt = new TextView(this);
            txt.setText(MazeHolder.MazeArr.get(i).name);
            txt.setTextSize(25);
            txt.setPadding(50, 20, 10, 10);
            txt.setTextColor(Color.GREEN);
            RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            mc.addView(txt, rules);
            int finalI = i;
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chmap = finalI;
                }
            });
        }

        ImageView rand = (ImageView) findViewById(R.id.random);
        rand.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Random r = new Random();
                if (MazeHolder.MazeArr.size() == 1){
                    Maze.rand = 0;
                } else {
                    Maze.rand = r.nextInt(MazeHolder.MazeArr.size() - 1);
                }
                startActivity(new Intent(getApplicationContext(), StartPage.class));
            }
        });
        ImageView chose = (ImageView) findViewById(R.id.chooselayout);
        chose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (chmap != -1) {
                    Maze.rand = chmap;
                    startActivity(new Intent(getApplicationContext(), StartPage.class));
                }
            }
        });
    }
}
