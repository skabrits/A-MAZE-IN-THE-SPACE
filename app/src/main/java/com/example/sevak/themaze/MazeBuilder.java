package com.example.sevak.themaze;

import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static com.example.sevak.themaze.StartPage.BFG;
import static com.example.sevak.themaze.StartPage.BULLET;
import static com.example.sevak.themaze.StartPage.CELLSIZE;
import static com.example.sevak.themaze.StartPage.HOSPITAL;
import static com.example.sevak.themaze.StartPage.KEY;
import static com.example.sevak.themaze.StartPage.MINOTAUR;
import static com.example.sevak.themaze.StartPage.OFFSET_LEFT;
import static com.example.sevak.themaze.StartPage.OFFSET_TOP;

public class MazeBuilder extends AppCompatActivity {

    private float ConvDPtoPX(float dp){
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        return dp*((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private GestureDetector mDetector;
    private RelativeLayout thisView;
    private Integer[][] TMaze = new Integer[21][21];
    private HashMap<String, Integer> tp_num = new HashMap<String, Integer>();
    private int[] BC = new int[2];
    private boolean IamPlaced = false;
    private boolean exitPlaced = false;
    private boolean keyPlaced = false;
    private int minotaurAmmount = 0;
    private boolean hospitalPlaced = false;
    private int bulletAmmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze_builder);

        findViewById(R.id.TheBegin).getBackground().setAlpha(255);
        int Cellsize = (int) ConvDPtoPX(1)*41;

        BC = new int[] {1, 1};

        for (int i = 0; i < TMaze.length; i++) {
            for (int j = 0; j < TMaze[0].length; j++) {
                TMaze[i][j] = 0;
            }
        }

        Button fin = (Button) findViewById(R.id.end);
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView wc = (TextView) findViewById(R.id.WCounter);
                TextView hc = (TextView) findViewById(R.id.HCounter);
                int[][] PMaze = new int[2*Integer.parseInt(hc.getText().toString())+1][2*Integer.parseInt(wc.getText().toString())+1];
                for (int i = 0; i < PMaze.length; i++) {
                    for (int j = 0; j < PMaze[0].length; j++) {
                        PMaze[i][j] = TMaze[i][j];
                    }
                }

                if (AllIsOk(PMaze)) {
                    MazeExample Mazik = new MazeExample();
                    Mazik.Maze = PMaze;
                    Mazik.BasicCordinats = BC;
                    final RelativeLayout rl = (RelativeLayout) findViewById(R.id.workspace);
                    final EditText et = new EditText(rl.getContext());
                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    p.setMargins(300, 10, 10, 10);
                    et.setHint("write maze name");
                    rl.addView(et, p);
                    final Button bt = new Button(rl.getContext());
                    RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    bt.setText("confirm");
                    p1.setMargins(500, 10, 10, 10);
                    rl.addView(bt, p1);
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Mazik.name = et.getText().toString();
                            MazeHolder.MazeArr.add(Mazik);

                            Gson gson = new Gson();
                            SharedPreferences sharedPreferencesForMholder;
                            sharedPreferencesForMholder = getSharedPreferences("mazehold", MODE_PRIVATE);
                            SharedPreferences.Editor ed = sharedPreferencesForMholder.edit();
                            ed.putString("mazehold", gson.toJson(MazeHolder.MazeArr));
                            ed.apply();

                            Toast t = Toast.makeText(getApplicationContext(), "A maze has been created", Toast.LENGTH_SHORT);
                            t.show();
                            et.setVisibility(View.GONE);
                            bt.setVisibility(View.GONE);
                        }
                    });
                }
            }

            private boolean AllIsOk(int[][] pMaze) {
                boolean res = true;
                if (exitPlaced && IamPlaced) {
                    if ((minotaurAmmount > 0) && ((bulletAmmount < minotaurAmmount) || (!hospitalPlaced))){
                        res = false;
                        Toast t = Toast.makeText(getApplicationContext(), "You haven't placed enough bullets or hospital", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    for (Integer i : tp_num.values()) {
                        ArrayList<Integer> tpAr = new ArrayList<Integer>();
                        tpAr.add(i);
                        for (Integer j : tp_num.values()) {
                            if ((((int) (i / 100)) == ((int) (j / 100))) && (!i.equals(j))){
                                tpAr.add(j);
                            }
                        }
                        Collections.sort(tpAr,
                                (m1, m2) -> (int) (m1 - m2));
                        for (int j = 1; j < tpAr.size(); j++) {
                            if (tpAr.get(j) != (tpAr.get(j - 1) + 1)){
                                res = false;
                                Toast t = Toast.makeText(getApplicationContext(), "You haven't placed teleports correctly", Toast.LENGTH_SHORT);
                                t.show();
                            }
                        }
                        tpAr.clear();
                    }
                    for (int i = 0; i <= (int) (pMaze.length / 2); i++) {
                        if (2*i == 0 || 2*i == pMaze.length - 1) {
                            for (int j = 0; j < (int) (pMaze[0].length / 2); j++) {
                                if (pMaze[2*i][j*2+1] != 1 && pMaze[i*2][j*2+1] != 2){
                                    res = false;
                                    Toast t = Toast.makeText(getApplicationContext(), "You haven't placed all walls yet", Toast.LENGTH_SHORT);
                                    t.show();
                                }
                            }
                        } else {
                            if ((pMaze[i*2+1][0] != 1 && pMaze[i*2+1][0] != 2) || (pMaze[i*2+1][pMaze[0].length - 1] != 1 && pMaze[i*2+1][pMaze[0].length - 1] != 2)){
                                res = false;
                                Toast t = Toast.makeText(getApplicationContext(), "You haven't placed all walls yet", Toast.LENGTH_SHORT);
                                t.show();
                            }
                        }
                    }
                } else {
                    res = false;
                    Toast t = Toast.makeText(getApplicationContext(), "You haven't placed exit or start point yet", Toast.LENGTH_SHORT);
                    t.show();
                }
                return res;
            }
        });

        final LinearLayout addl = (LinearLayout) findViewById(R.id.AddNEWView);
        final LinearLayout dell = (LinearLayout) findViewById(R.id.DelAView);
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addl.setVisibility(View.GONE);
                dell.setVisibility(View.GONE);
                findViewById(R.id.cancel).setVisibility(View.GONE);
            }
        });
        for (int i = 0; i < addl.getChildCount(); i++) {
            final int finalI = i;
            addl.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (thisView.findViewWithTag(String.valueOf(finalI)) == null) {
                        int[] cordin = new int[]{Integer.parseInt(thisView.getTag().toString().split(" ")[0]), Integer.parseInt(thisView.getTag().toString().split(" ")[1])};
                        if ((finalI == 15) && (checkWAS(finalI, cordin))) {
                            if (!hospitalPlaced) {
                                TMaze[cordin[0] * 2 + 1][(cordin[1] * 2 + 1)] = HOSPITAL;
                                AddView(thisView, finalI);
                                hospitalPlaced = true;
                            } else {
                                Toast t = Toast.makeText(getApplicationContext(), "You have placed hospital already", Toast.LENGTH_SHORT);
                                t.show();
                            }
                        } else {
                            if ((finalI == 18)  && (checkWAS(finalI, cordin))) {
                                if (!keyPlaced) {
                                    TMaze[cordin[0] * 2 + 1][(cordin[1] * 2 + 1)] = KEY;
                                    AddView(thisView, finalI);
                                    keyPlaced = true;
                                } else {
                                    Toast t = Toast.makeText(getApplicationContext(), "You have placed key already", Toast.LENGTH_SHORT);
                                    t.show();
                                }
                            } else {
                                if ((finalI == 9) && (checkWAS(finalI, cordin))) {
                                    if (!IamPlaced) {
                                        BC[0] = cordin[0] * 2 + 1;
                                        BC[1] = (cordin[1] * 2 + 1);
                                        AddView(thisView, finalI);
                                        IamPlaced = true;
                                    } else {
                                        Toast t = Toast.makeText(getApplicationContext(), "You have placed start point already", Toast.LENGTH_SHORT);
                                        t.show();
                                    }
                                } else {
                                    if (finalI >= 10 && finalI <= 13) {
                                        if (!exitPlaced) {
                                            if (wallcheck(finalI, cordin)) {
                                                switch (finalI) {
                                                    case 10: {
                                                        TMaze[cordin[0] * 2][(cordin[1] * 2 + 1)] = 2;
                                                        AddView(thisView, finalI);
                                                        break;
                                                    }
                                                    case 11: {
                                                        RelativeLayout tl = (RelativeLayout) findViewById(R.id.workspace).findViewWithTag(String.valueOf(cordin[0]) + " " + String.valueOf(cordin[1] + 1));
                                                        TMaze[cordin[0] * 2 + 1][(cordin[1] * 2 + 2)] = 2;
                                                        try {
                                                            AddView(tl, 13);
                                                        } catch (NullPointerException e){
                                                            AddView(thisView, finalI);
                                                        }
                                                        break;
                                                    }
                                                    case 12: {
                                                        TMaze[cordin[0] * 2 + 2][(cordin[1] * 2 + 1)] = 2;
                                                        RelativeLayout tl = (RelativeLayout) findViewById(R.id.workspace).findViewWithTag(String.valueOf(cordin[0] + 1) + " " + String.valueOf(cordin[1]));
                                                        try {
                                                            AddView(tl, 10);
                                                        } catch (NullPointerException e){
                                                            AddView(thisView, finalI);
                                                        }
                                                        break;
                                                    }
                                                    case 13: {
                                                        TMaze[cordin[0] * 2 + 1][(cordin[1] * 2)] = 2;
                                                        AddView(thisView, finalI);
                                                        break;
                                                    }
                                                }
                                                exitPlaced = true;
                                            } else {
                                                Toast t = Toast.makeText(getApplicationContext(), "can't place exit", Toast.LENGTH_SHORT);
                                                t.show();
                                            }
                                        } else {
                                            Toast t = Toast.makeText(getApplicationContext(), "You have placed exit already", Toast.LENGTH_SHORT);
                                            t.show();
                                        }
                                    } else {
                                        if ((((finalI >= 0) && (finalI < 2)) || ((finalI >= 6) && (finalI < 8)))) {
                                            if (exitcheck(finalI, cordin)) {
                                                switch (finalI) {
                                                    case 0: {
                                                        TMaze[cordin[0] * 2][(cordin[1] * 2 + 1)] = 1;
                                                        AddView(thisView, finalI);
                                                        break;
                                                    }
                                                    case 1: {
                                                        RelativeLayout tl = (RelativeLayout) findViewById(R.id.workspace).findViewWithTag(String.valueOf(cordin[0] + 1) + " " + String.valueOf(cordin[1]));
                                                        TMaze[cordin[0] * 2 + 2][(cordin[1] * 2 + 1)] = 1;
                                                        try {
                                                            AddView(tl, 0);
                                                        } catch (NullPointerException e){
                                                            AddView(thisView, finalI);
                                                        }
                                                        break;
                                                    }
                                                    case 6: {
                                                        TMaze[(cordin[0] * 2 + 1)][cordin[1] * 2] = 1;
                                                        AddView(thisView, finalI);
                                                        break;
                                                    }
                                                    case 7: {
                                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 2)] = 1;
                                                        RelativeLayout tl = (RelativeLayout) findViewById(R.id.workspace).findViewWithTag(String.valueOf(cordin[0]) + " " + String.valueOf(cordin[1] + 1));
                                                        try {
                                                            AddView(tl,6);
                                                        } catch (NullPointerException e){
                                                            AddView(thisView, finalI);
                                                        }
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Toast t = Toast.makeText(getApplicationContext(), "can't place wall", Toast.LENGTH_SHORT);
                                                t.show();
                                            }
                                        } else {
                                            if ((checkWAS(finalI, cordin))) {
                                                switch (finalI) {
                                                    case 2: {
                                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 12;
                                                        break;
                                                    }
                                                    case 3: {
                                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 13;
                                                        break;
                                                    }
                                                    case 4: {
                                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 14;
                                                        break;
                                                    }
                                                    case 5: {
                                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 11;
                                                        break;
                                                    }
                                                    case 8: {
                                                        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.workspace);
                                                        final LinearLayout dc = (LinearLayout) findViewById(R.id.DelAView);
                                                        final LinearLayout ac = (LinearLayout) findViewById(R.id.AddNEWView);
                                                        for (int j = 0; j < rl.getChildCount(); j++) {
                                                            rl.getChildAt(j).setClickable(false);
                                                        }
                                                        for (int j = 0; j < ac.getChildCount(); j++) {
                                                            ac.getChildAt(j).setClickable(false);
                                                        }
                                                        for (int j = 0; j < dc.getChildCount(); j++) {
                                                            dc.getChildAt(j).setClickable(false);
                                                        }
                                                        final EditText et = new EditText(rl.getContext());
                                                        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                                                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                                                        p.setMargins(300, 10, 10, 10);
                                                        et.setHint("write tp number in form xyy, x - series number, yy - teleport number in series");
                                                        rl.addView(et, p);
                                                        final Button bt = new Button(rl.getContext());
                                                        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(
                                                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                                                        bt.setText("confirm");
                                                        p1.setMargins(500, 10, 10, 10);
                                                        rl.addView(bt, p1);
                                                        bt.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Integer tpnum = 0;
                                                                tpnum = tryParse(et.getText().toString());
                                                                if (tpnum == null) {
                                                                    tpnum = 0;
                                                                }
                                                                if (checkTPexist(tpnum) && ((int) (tpnum / 100) >= 1) && ((int) (tpnum / 100) <= 9) && (tpnum - ((int) (tpnum / 100)) * 100 >= 1) && (tpnum - ((int) (tpnum / 100)) * 100 <= 99)) {
                                                                    TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = tpnum;
                                                                    tp_num.put(thisView.getTag().toString(), tpnum);
                                                                    AddView(thisView, finalI);
                                                                    for (int j = 0; j < rl.getChildCount(); j++) {
                                                                        rl.getChildAt(j).setClickable(true);
                                                                    }
                                                                    for (int j = 0; j < ac.getChildCount(); j++) {
                                                                        ac.getChildAt(j).setClickable(true);
                                                                    }
                                                                    for (int j = 0; j < dc.getChildCount(); j++) {
                                                                        dc.getChildAt(j).setClickable(true);
                                                                    }
                                                                    rl.removeView(et);
                                                                    rl.removeView(bt);
                                                                } else {
                                                                    Toast t = Toast.makeText(getApplicationContext(), "Incorrect input", Toast.LENGTH_SHORT);
                                                                    t.show();
                                                                }

                                                            }

                                                            private boolean checkTPexist(int tpnum) {
                                                                boolean res = true;
                                                                for (Integer i : tp_num.values()) {
                                                                    if (tpnum == i) {
                                                                        res = false;
                                                                        Toast t = Toast.makeText(getApplicationContext(), "Tp was", Toast.LENGTH_SHORT);
                                                                        t.show();
                                                                    }
                                                                }
                                                                return res;
                                                            }
                                                        });
                                                        break;
                                                    }
                                                    case 14: {
                                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = MINOTAUR;
                                                        minotaurAmmount++;
                                                        break;
                                                    }
                                                    case 16: {
                                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = BFG;
                                                        break;
                                                    }
                                                    case 17: {
                                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = BULLET;
                                                        bulletAmmount++;
                                                        break;
                                                    }
                                                }
                                                if (finalI != 8) {
                                                    AddView(thisView, finalI);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                private void AddView(RelativeLayout thisView, int finalI) {
                    ImageView iv = (ImageView) addl.getChildAt(finalI);
                    ImageView imageView = new ImageView(thisView.getContext());
                    imageView.setImageDrawable(iv.getDrawable());
                    imageView.setTag(String.valueOf(finalI));
                    thisView.addView(imageView);
                }

                private boolean wallcheck(int finalI, int[] cord) {
                    boolean res;
                    res = true;
                    if ((TMaze[cord[0]*2][cord[1]*2+1] != 0) && ((finalI - 10) == 0)) {
                        res = false;
                    }
                    if ((TMaze[cord[0]*2+2][cord[1]*2+1] != 0) && ((finalI - 10) == 2)) {
                        res = false;
                    }
                    if ((TMaze[cord[0]*2+1][cord[1]*2] != 0) && ((finalI - 10) == 3)) {
                        res = false;
                    }
                    if ((TMaze[cord[0]*2+1][cord[1]*2+2] != 0) && ((finalI - 10) == 1)) {
                        res = false;
                    }
                    return res;
                }

                private boolean exitcheck(int finalI, int[] cord) {
                    boolean res;
                    res = true;
                    if ((TMaze[cord[0]*2][cord[1]*2+1] != 0) && (finalI == 0)) {
                        res = false;
                    }
                    if ((TMaze[cord[0]*2+2][cord[1]*2+1] != 0) && (finalI == 1)) {
                        res = false;
                    }
                    if ((TMaze[cord[0]*2+1][cord[1]*2] != 0) && ((finalI + 6) == 12)) {
                        res = false;
                    }
                    if ((TMaze[cord[0]*2+1][cord[1]*2+2] != 0) && ((finalI + 6) == 13)) {
                        res = false;
                    }

                    return res;
                }

                private Integer tryParse(String text) {
                    try {
                        return Integer.parseInt(text);
                    } catch (NumberFormatException e) {
                        Toast t = Toast.makeText(getApplicationContext(), "No letters!", Toast.LENGTH_SHORT);
                        t.show();
                        return null;
                    }
                }

                private Boolean checkWAS(int finalI, int[] cord) {
                    Boolean res;
                    res = true;
                    if (TMaze[cord[0]*2+1][cord[1]*2+1] != 0) {
                        res = false;
                    }
                    return res;
                }
            });
        }
        for (int i = 0; i < dell.getChildCount(); i++) {
            final int finalI = i;
            dell.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KillCellElem(finalI);
                }
            });
        }

        mDetector = new GestureDetector(this, new MyGestureListener());

        TMaze[1][1] = 0;
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.workspace);
        final RelativeLayout relativeLayout = new RelativeLayout(this);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.walls0);
        relativeLayout.addView(imageView);
        relativeLayout.setTag(0 + " " + 0);
        relativeLayout.setClickable(true);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (relativeLayout.isClickable()) {
                    mDetector.onTouchEvent(motionEvent);
                    thisView = relativeLayout;
                }
                return true;
            }
        });
        RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        rules.setMargins(OFFSET_LEFT, OFFSET_TOP, 0, 0);
        layout.addView(relativeLayout, rules);

        final TextView wc = (TextView) findViewById(R.id.WCounter);

        ImageView wl = (ImageView) findViewById(R.id.WArL);
        wl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Integer.parseInt(wc.getText().toString()) > 1) {
                    delcol();
                    wc.setText(String.valueOf(Integer.parseInt(wc.getText().toString()) - 1));
                }
            }
        });
        ImageView wr = (ImageView) findViewById(R.id.WArR);
        wr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Integer.parseInt(wc.getText().toString()) < 10) {
                    wc.setText(String.valueOf(Integer.parseInt(wc.getText().toString()) + 1));
                    addcol(Cellsize);
                }
            }
        });


        final TextView hc = (TextView) findViewById(R.id.HCounter);

        ImageView hl = (ImageView) findViewById(R.id.HArL);
        hl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Integer.parseInt(hc.getText().toString()) > 1) {
                    delline();
                    hc.setText(String.valueOf(Integer.parseInt(hc.getText().toString()) - 1));
                }
            }
        });
        ImageView hr = (ImageView) findViewById(R.id.HArR);
        hr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Integer.parseInt(hc.getText().toString()) < 10) {
                    hc.setText(String.valueOf(Integer.parseInt(hc.getText().toString()) + 1));
                    addline(Cellsize);
                }
            }
        });

    }

    private void KillCellElem(int finalI) {
        final int[] cordin = new int[]{Integer.parseInt(thisView.getTag().toString().split(" ")[0]), Integer.parseInt(thisView.getTag().toString().split(" ")[1])};
        if (thisView.findViewWithTag(String.valueOf(finalI)) != null) {
            switch (finalI) {
                case 0: {
                    TMaze[cordin[0] * 2][(cordin[1] * 2 + 1)] = 0;
                    break;
                }
                case 1: {
                    TMaze[cordin[0] * 2 + 2][(cordin[1] * 2 + 1)] = 0;
                    break;
                }
                case 2: {
                    TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 0;
                    break;
                }
                case 3: {
                    TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 0;
                    break;
                }
                case 4: {
                    TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 0;
                    break;
                }
                case 5: {
                    TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 0;
                    break;
                }
                case 6: {
                    TMaze[(cordin[0] * 2 + 1)][cordin[1] * 2] = 0;
                    break;
                }
                case 7: {
                    TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 2)] = 0;
                    break;
                }
                case 8: {
                    TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 0;
                    tp_num.remove(thisView.getTag().toString());
                    break;
                }
                case 9: {
                    if (IamPlaced) {
                        BC = new int[]{0, 0};
                        thisView.removeView(thisView.findViewWithTag(String.valueOf(finalI)));
                        IamPlaced = false;
                    }
                    break;
                }
                case 10: {
                    if (exitPlaced) {
                        TMaze[(cordin[0] * 2)][(cordin[1] * 2 + 1)] = 0;
                        thisView.removeView(thisView.findViewWithTag(String.valueOf(finalI)));
                        exitPlaced = false;
                    }
                    break;
                }
                case 11: {
                    if (exitPlaced) {
                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 2)] = 0;
                        thisView.removeView(thisView.findViewWithTag(String.valueOf(finalI)));
                        exitPlaced = false;
                    }
                    break;
                }
                case 12: {
                    if (exitPlaced) {
                        TMaze[(cordin[0] * 2 + 2)][(cordin[1] * 2 + 1)] = 0;
                        thisView.removeView(thisView.findViewWithTag(String.valueOf(finalI)));
                        exitPlaced = false;
                    }
                    break;
                }
                case 13: {
                    if (exitPlaced) {
                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2)] = 0;
                        thisView.removeView(thisView.findViewWithTag(String.valueOf(finalI)));
                        exitPlaced = false;
                    }
                    break;
                }
                case 14: {
                    TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 0;
                    minotaurAmmount--;
                    break;
                }
                case 15: {
                    if (hospitalPlaced) {
                        TMaze[(cordin[0] * 2) + 1][(cordin[1] * 2 + 1)] = 0;
                        thisView.removeView(thisView.findViewWithTag(String.valueOf(finalI)));
                        hospitalPlaced = false;
                    }
                    break;
                }
                case 16: {
                    TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 0;
                    break;
                }
                case 17: {
                    TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 0;
                    bulletAmmount--;
                    break;
                }
                case 18: {
                    if (keyPlaced) {
                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2) + 1] = 0;
                        thisView.removeView(thisView.findViewWithTag(String.valueOf(finalI)));
                        keyPlaced = false;
                    }
                    break;
                }
            }
            if ((finalI <= 13 || finalI >= 9) && (finalI != 15) && (finalI != 18)) {
                thisView.removeView(thisView.findViewWithTag(String.valueOf(finalI)));
            }
        } else {
            try {
                switch (finalI) {
                    case 0: {
                        RelativeLayout tl = (RelativeLayout) findViewById(R.id.workspace).findViewWithTag(String.valueOf(cordin[0] - 1) + " " + String.valueOf(cordin[1]));
                        if (tl.findViewWithTag(String.valueOf(1)) != null) {
                            TMaze[cordin[0] * 2][(cordin[1] * 2 + 1)] = 0;
                            tl.removeView(tl.findViewWithTag(String.valueOf(1)));
                        }
                        break;
                    }
                    case 1: {
                        RelativeLayout tl = (RelativeLayout) findViewById(R.id.workspace).findViewWithTag(String.valueOf(cordin[0] + 1) + " " + String.valueOf(cordin[1]));
                        if (tl.findViewWithTag(String.valueOf(0)) != null) {
                            TMaze[cordin[0] * 2 + 2][(cordin[1] * 2 + 1)] = 0;
                            tl.removeView(tl.findViewWithTag(String.valueOf(0)));
                        }
                        break;
                    }
                    case 6: {
                        RelativeLayout tl = (RelativeLayout) findViewById(R.id.workspace).findViewWithTag(String.valueOf(cordin[0]) + " " + String.valueOf(cordin[1] - 1));
                        if (tl.findViewWithTag(String.valueOf(7)) != null) {
                            TMaze[(cordin[0] * 2 + 1)][cordin[1] * 2] = 0;
                            tl.removeView(tl.findViewWithTag(String.valueOf(7)));
                        }
                        break;
                    }
                    case 7: {
                        RelativeLayout tl = (RelativeLayout) findViewById(R.id.workspace).findViewWithTag(String.valueOf(cordin[0]) + " " + String.valueOf(cordin[1] + 1));
                        if (tl.findViewWithTag(String.valueOf(6)) != null) {
                            TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 2)] = 0;
                            tl.removeView(tl.findViewWithTag(String.valueOf(6)));
                        }
                        break;
                    }
                    case 10: {
                        RelativeLayout tl = (RelativeLayout) findViewById(R.id.workspace).findViewWithTag(String.valueOf(cordin[0] - 1) + " " + String.valueOf(cordin[1]));
                        if (tl.findViewWithTag(String.valueOf(12)) != null) {
                            if (exitPlaced) {
                                TMaze[(cordin[0] * 2 + 2)][(cordin[1] * 2 + 1)] = 0;
                                tl.removeView(tl.findViewWithTag(String.valueOf(12)));
                                exitPlaced = false;
                            }
                        }
                        break;
                    }
                    case 11: {
                        RelativeLayout tl = (RelativeLayout) findViewById(R.id.workspace).findViewWithTag(String.valueOf(cordin[0]) + " " + String.valueOf(cordin[1] + 1));
                        if (tl.findViewWithTag(String.valueOf(13)) != null) {
                            if (exitPlaced) {
                                TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2)] = 0;
                                tl.removeView(tl.findViewWithTag(String.valueOf(13)));
                                exitPlaced = false;
                            }
                        }
                        break;
                    }
                    case 12: {
                        RelativeLayout tl = (RelativeLayout) findViewById(R.id.workspace).findViewWithTag(String.valueOf(cordin[0] + 1) + " " + String.valueOf(cordin[1]));
                        if (tl.findViewWithTag(String.valueOf(10)) != null) {
                            if (exitPlaced) {
                                TMaze[(cordin[0] * 2)][(cordin[1] * 2 + 1)] = 0;
                                tl.removeView(tl.findViewWithTag(String.valueOf(10)));
                                exitPlaced = false;
                            }
                        }
                        break;
                    }
                    case 13: {
                        RelativeLayout tl = (RelativeLayout) findViewById(R.id.workspace).findViewWithTag(String.valueOf(cordin[0]) + " " + String.valueOf(cordin[1] - 1));
                        if (tl.findViewWithTag(String.valueOf(11)) != null) {
                            if (exitPlaced) {
                                TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 2)] = 0;
                                tl.removeView(tl.findViewWithTag(String.valueOf(11)));
                                exitPlaced = false;
                            }
                        }
                        break;
                    }
                }
            } catch (NullPointerException er){
                System.out.println(er);
            }
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            findViewById(R.id.AddNEWView).setVisibility(View.VISIBLE);
            findViewById(R.id.DelAView).setVisibility(View.VISIBLE);
            findViewById(R.id.cancel).setVisibility(View.VISIBLE);
            return true;
        }
    }

    private void delline() {
        TextView wc = (TextView) findViewById(R.id.WCounter);
        TextView hc = (TextView) findViewById(R.id.HCounter);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.workspace);
        for (int i = 0; i < Integer.parseInt(wc.getText().toString()); i++) {
            RelativeLayout view = (RelativeLayout) layout.findViewWithTag((Integer.parseInt(hc.getText().toString()) - 1) + " " + String.valueOf(i));
            DelBody(layout, view);
        }
    }

    private void DelBody(RelativeLayout layout, RelativeLayout view) {
        RelativeLayout rl = thisView;
        if (rl == null){
            rl = layout.findViewWithTag(0 + " " + 0);
        } else if (thisView.equals(view)){
            rl = layout.findViewWithTag(0 + " " + 0);
        }
        thisView = view;
        for (int j = 1; j < thisView.getChildCount(); j++) {
            KillCellElem(Integer.parseInt(thisView.getChildAt(j).getTag().toString()));
        }
        layout.removeView(view);
        thisView = rl;
    }

    private void addline(int Cellsize) {
        TextView wc = (TextView) findViewById(R.id.WCounter);
        TextView hc = (TextView) findViewById(R.id.HCounter);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.workspace);
        for (int i = 0; i < Integer.parseInt(wc.getText().toString()); i++) {
            final RelativeLayout relativeLayout = new RelativeLayout(this);
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.walls0);
            relativeLayout.addView(imageView);
            relativeLayout.setTag((Integer.parseInt(hc.getText().toString()) - 1) + " " + String.valueOf(i));
            relativeLayout.setClickable(true);
            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (relativeLayout.isClickable()) {
                        mDetector.onTouchEvent(motionEvent);
                        thisView = relativeLayout;
                    }
                    return true;
                }
            });
            RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            rules.setMargins(OFFSET_LEFT + Cellsize * i, OFFSET_TOP + Cellsize * (Integer.parseInt(hc.getText().toString()) - 1), 100, 100);
            layout.addView(relativeLayout, rules);
        }
    }

    private void addcol(int Cellsize) {
        TextView wc = (TextView) findViewById(R.id.WCounter);
        TextView hc = (TextView) findViewById(R.id.HCounter);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.workspace);
        for (int i = 0; i < Integer.parseInt(hc.getText().toString()); i++) {
            final RelativeLayout relativeLayout = new RelativeLayout(this);
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.walls0);
            relativeLayout.addView(imageView);
            relativeLayout.setTag(String.valueOf(i) + " " + (Integer.parseInt(wc.getText().toString()) - 1));
            relativeLayout.setClickable(true);
            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (relativeLayout.isClickable()) {
                        mDetector.onTouchEvent(motionEvent);
                        thisView = relativeLayout;
                    }
                    return true;
                }
            });
            RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            rules.setMargins(OFFSET_LEFT +Cellsize * (Integer.parseInt(wc.getText().toString()) - 1), OFFSET_TOP +Cellsize * i, 100, 100);
            layout.addView(relativeLayout, rules);
        }
    }

    private void delcol() {
        TextView wc = (TextView) findViewById(R.id.WCounter);
        TextView hc = (TextView) findViewById(R.id.HCounter);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.workspace);
        for (int i = 0; i < Integer.parseInt(hc.getText().toString()); i++) {
            RelativeLayout view = (RelativeLayout) layout.findViewWithTag(String.valueOf(i) + " " + (Integer.parseInt(wc.getText().toString()) - 1));
            DelBody(layout, view);
        }
    }
}
