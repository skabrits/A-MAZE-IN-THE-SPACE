package com.example.sevak.themaze;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.Random;

import static com.example.sevak.themaze.MazeHolder.sharedPreferencesForMholder;

public class Campaign extends AppCompatActivity {

    private int chmap = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);

        MazeHolderCampaign.init(getApplicationContext());

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
                    chmap = finalI;
                }
            });
        }

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
