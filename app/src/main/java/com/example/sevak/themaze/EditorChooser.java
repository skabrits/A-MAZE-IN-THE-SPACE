package com.example.sevak.themaze;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Random;

import static com.example.sevak.themaze.MazeHolder.sharedPreferencesForMholder;

public class EditorChooser extends AppCompatActivity {

    private TextView currentTextView = null;
    private int chmap = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_chooser);

        findViewById(R.id.rev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditMapShlus.class));
            }
        });

        MazeHolder.init(getApplicationContext());

        LinearLayout mc = (LinearLayout) findViewById(R.id.MapChooser);

        mc.removeAllViews();

        for (int i = 0; i < MazeHolder.MazeArr.size(); i++) {
            TextView txt = new TextView(this);
            txt.setTag(MazeHolder.MazeArr.get(i).name);
            txt.setText(MazeHolder.MazeArr.get(i).name);
            ColorHolder.CHOOSE_VIEW_CUSTOMIZE(txt);
            RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            mc.addView(txt, rules);
            int finalI = i;
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(currentTextView != null) {
                        currentTextView.setTextColor(ColorHolder.CHOOSER_LIST);
                    }
                    chmap = finalI;
                    txt.setTextColor(ColorHolder.CHOOSER_LIST_SELECTED);
                    currentTextView = txt;
                }
            });
        }

        ImageView chose = (ImageView) findViewById(R.id.chooselayout);
        chose.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (chmap != -1) {
                    EditMazeHolder.MarNum = chmap;
                    int[][] a = MazeHolder.MazeArr.get(chmap).Maze;
                    EditMazeHolder.enterMaze = new Integer[a.length][a[0].length];
                    for (int i = 0; i < a.length; i++) {
                        for (int j = 0; j < a[0].length; j++) {
                            EditMazeHolder.enterMaze[i][j] = a[i][j];
                        }
                    }
                    EditMazeHolder.bs = MazeHolder.MazeArr.get(chmap).BasicCordinats;
                    startActivity(new Intent(getApplicationContext(), MazeBuilder.class));
                }
            }
        });
    }
}
