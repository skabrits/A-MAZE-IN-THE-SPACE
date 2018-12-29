package com.example.sevak.themaze;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import static com.example.sevak.themaze.MazeHolder.sharedPreferencesForMholder;
import static com.example.sevak.themaze.StartPage.CELLSIZE;
import static com.example.sevak.themaze.StartPage.OFFSET_LEFT;
import static com.example.sevak.themaze.StartPage.OFFSET_TOP;

public class Predbannik extends AppCompatActivity {

    private TextView currentTextView = null;
    private int chmap = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predbannik);

        findViewById(R.id.rev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SingleplayerVilka.class));
            }
        });

        MazeHolder.init(getApplicationContext());

        LinearLayout mc = (LinearLayout) findViewById(R.id.MapChooser);

        mc.removeAllViews();

        for (int i = 0; i < MazeHolder.MazeArr.size(); i++) {
            TextView txt = new TextView(this);
            txt.setTag(MazeHolder.MazeArr.get(i).name);
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
                    if(currentTextView != null) {
                        currentTextView.setTextColor(Color.GREEN);
                    }
                    chmap = finalI;
                    txt.setTextColor(Color.YELLOW);
                    currentTextView = txt;
                }
            });

            ImageView txtdel = new ImageView(this);
            txtdel.setImageResource(getResources().getIdentifier("android:drawable/ic_menu_delete", null, null));
            txtdel.setPadding(10, 10, 50, 20);
            RelativeLayout.LayoutParams rules1 = new RelativeLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            mc.addView(txtdel, rules1);
            int finalI1 = i;
            txtdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteMap();
                }

                private void deleteMap() {
                    Toast t = Toast.makeText(getApplicationContext(), "A maze " + MazeHolder.MazeArr.get(finalI1).name + " has been deleted", Toast.LENGTH_SHORT);
                    t.show();
                    Gson gson = new Gson();
                    TextView txt = new TextView(getApplicationContext());
                    txt = (TextView) mc.findViewWithTag(MazeHolder.MazeArr.get(finalI1).name);
                    mc.removeView(txt);
                    SharedPreferences sharedPreferences = getSharedPreferences("mazehold", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sharedPreferencesForMholder.edit();
                    MazeHolder.MazeArr.remove(finalI1);
                    ed.putString("mazehold", gson.toJson(MazeHolder.MazeArr));
                    ed.apply();
                    mc.removeView(txtdel);
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
