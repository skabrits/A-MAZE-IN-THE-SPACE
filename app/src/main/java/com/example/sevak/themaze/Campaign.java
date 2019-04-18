package com.example.sevak.themaze;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Campaign extends AppCompatActivity {

    private TextView currentTextView = null;
    private int chlvl = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);

        findViewById(R.id.rev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SingleplayerVilka.class));
            }
        });

        LevelHolderCampaign.init(getApplicationContext());

        LinearLayout mc = (LinearLayout) findViewById(R.id.MapChooser);

        mc.removeAllViews();

        for (int i = 0; i <= LevelHolderCampaign.Mylvl; i++) {
            TextView txt = new TextView(this);
            txt.setTag(LevelHolderCampaign.LevelArr.get(i).name);
            txt.setText(LevelHolderCampaign.LevelArr.get(i).name);
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
                    chlvl = finalI;
                    txt.setTextColor(ColorHolder.CHOOSER_LIST_SELECTED);
                    currentTextView = txt;
                }
            });
        }

        ImageView chose = (ImageView) findViewById(R.id.chooselayout);
        chose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (chlvl != -1) {
                    startLevel();
                }
            }
        });
    }

    private void startLevel() {
        if (LevelHolderCampaign.LevelArr.get(chlvl).type.equals("lvl")) {
            MazeCampaign.rand = chlvl;
            startActivity(new Intent(getApplicationContext(), StartPageCampaign.class));
        } else {
            if (LevelHolderCampaign.LevelArr.get(chlvl).type.equals("csc")){
                CscHolder.videoNum = chlvl;
                startActivity(new Intent(getApplicationContext(), Catsciene.class));
            }
        }
    }
}
