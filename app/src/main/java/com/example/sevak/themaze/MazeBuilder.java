package com.example.sevak.themaze;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.sevak.themaze.StartPage.BFG;
import static com.example.sevak.themaze.StartPage.BULLET;
import static com.example.sevak.themaze.StartPage.CELLSIZE;
import static com.example.sevak.themaze.StartPage.HOSPITAL;
import static com.example.sevak.themaze.StartPage.KEY;
import static com.example.sevak.themaze.StartPage.MINOTAUR;
import static com.example.sevak.themaze.StartPage.OFFSET_LEFT;
import static com.example.sevak.themaze.StartPage.OFFSET_TOP;

public class MazeBuilder extends AppCompatActivity {

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
                            if ((((int) (i / 100)) == ((int) (j / 100))) && (i != j)){
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
                        if ((finalI == 15) && (checkWAS(finalI) == null)) {
                            if (!hospitalPlaced) {
                                int[] cordin = new int[]{Integer.parseInt(thisView.getTag().toString().split(" ")[0]), Integer.parseInt(thisView.getTag().toString().split(" ")[1])};
                                TMaze[cordin[0] * 2 + 1][(cordin[1] * 2 + 1)] = HOSPITAL;
                                ImageView iv = (ImageView) addl.getChildAt(finalI);
                                ImageView imageView = new ImageView(thisView.getContext());
                                imageView.setImageDrawable(iv.getDrawable());
                                imageView.setTag(String.valueOf(finalI));
                                thisView.addView(imageView);
                                hospitalPlaced = true;
                            } else {
                                Toast t = Toast.makeText(getApplicationContext(), "You have placed hospital already", Toast.LENGTH_SHORT);
                                t.show();
                            }
                        } else {
                            if ((finalI == 18)  && (checkWAS(finalI) == null)) {
                                if (!keyPlaced) {
                                    int[] cordin = new int[]{Integer.parseInt(thisView.getTag().toString().split(" ")[0]), Integer.parseInt(thisView.getTag().toString().split(" ")[1])};
                                    TMaze[cordin[0] * 2 + 1][(cordin[1] * 2 + 1)] = KEY;
                                    ImageView iv = (ImageView) addl.getChildAt(finalI);
                                    ImageView imageView = new ImageView(thisView.getContext());
                                    imageView.setImageDrawable(iv.getDrawable());
                                    imageView.setTag(String.valueOf(finalI));
                                    thisView.addView(imageView);
                                    keyPlaced = true;
                                } else {
                                    Toast t = Toast.makeText(getApplicationContext(), "You have placed key already", Toast.LENGTH_SHORT);
                                    t.show();
                                }
                            } else {
                                if ((finalI == 9) && (checkWAS(finalI) == null)) {
                                    if (!IamPlaced) {
                                        int[] cordin = new int[]{Integer.parseInt(thisView.getTag().toString().split(" ")[0]), Integer.parseInt(thisView.getTag().toString().split(" ")[1])};
                                        BC[0] = cordin[0] * 2 + 1;
                                        BC[1] = (cordin[1] * 2 + 1);
                                        ImageView iv = (ImageView) addl.getChildAt(finalI);
                                        ImageView imageView = new ImageView(thisView.getContext());
                                        imageView.setImageDrawable(iv.getDrawable());
                                        imageView.setTag(String.valueOf(finalI));
                                        thisView.addView(imageView);
                                        IamPlaced = true;
                                    } else {
                                        Toast t = Toast.makeText(getApplicationContext(), "You have placed start point already", Toast.LENGTH_SHORT);
                                        t.show();
                                    }
                                } else {
                                    if (finalI >= 10 && finalI <= 13) {
                                        if (!exitPlaced) {
                                            if (wallcheck()) {
                                                final int[] cordin = new int[]{Integer.parseInt(thisView.getTag().toString().split(" ")[0]), Integer.parseInt(thisView.getTag().toString().split(" ")[1])};
                                                switch (finalI) {
                                                    case 10: {
                                                        TMaze[cordin[0] * 2][(cordin[1] * 2 + 1)] = 2;
                                                        break;
                                                    }
                                                    case 11: {
                                                        TMaze[cordin[0] * 2 + 1][(cordin[1] * 2 + 2)] = 2;
                                                        break;
                                                    }
                                                    case 12: {
                                                        TMaze[cordin[0] * 2 + 2][(cordin[1] * 2 + 1)] = 2;
                                                        break;
                                                    }
                                                    case 13: {
                                                        TMaze[cordin[0] * 2 + 1][(cordin[1] * 2)] = 2;
                                                        break;
                                                    }
                                                }
                                                ImageView iv = (ImageView) addl.getChildAt(finalI);
                                                ImageView imageView = new ImageView(thisView.getContext());
                                                imageView.setImageDrawable(iv.getDrawable());
                                                imageView.setTag(String.valueOf(finalI));
                                                thisView.addView(imageView);
                                                exitPlaced = true;
                                            } else {
                                                Toast t = Toast.makeText(getApplicationContext(), "can't place", Toast.LENGTH_SHORT);
                                                t.show();
                                            }
                                        } else {
                                            Toast t = Toast.makeText(getApplicationContext(), "You have placed exit already", Toast.LENGTH_SHORT);
                                            t.show();
                                        }
                                    } else {
                                        if ((((finalI >= 0) && (finalI < 2)) || ((finalI >= 6) && (finalI < 8)))) {
                                            if (exitcheck()) {
                                                final int[] cordin = new int[]{Integer.parseInt(thisView.getTag().toString().split(" ")[0]), Integer.parseInt(thisView.getTag().toString().split(" ")[1])};
                                                switch (finalI) {
                                                    case 0: {
                                                        TMaze[cordin[0] * 2][(cordin[1] * 2 + 1)] = 1;
                                                        break;
                                                    }
                                                    case 1: {
                                                        TMaze[cordin[0] * 2 + 2][(cordin[1] * 2 + 1)] = 1;
                                                        break;
                                                    }
                                                    case 6: {
                                                        TMaze[(cordin[0] * 2 + 1)][cordin[1] * 2] = 1;
                                                        break;
                                                    }
                                                    case 7: {
                                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 2)] = 1;
                                                        break;
                                                    }
                                                }
                                                ImageView iv = (ImageView) addl.getChildAt(finalI);
                                                ImageView imageView = new ImageView(thisView.getContext());
                                                imageView.setImageDrawable(iv.getDrawable());
                                                imageView.setTag(String.valueOf(finalI));
                                                thisView.addView(imageView);
                                            } else {
                                                Toast t = Toast.makeText(getApplicationContext(), "can't place", Toast.LENGTH_SHORT);
                                                t.show();
                                            }
                                        } else {
                                            if ((checkWAS(finalI) == null)) {
                                                final int[] cordin = new int[]{Integer.parseInt(thisView.getTag().toString().split(" ")[0]), Integer.parseInt(thisView.getTag().toString().split(" ")[1])};
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
                                                                    ImageView iv = (ImageView) addl.getChildAt(finalI);
                                                                    ImageView imageView = new ImageView(thisView.getContext());
                                                                    imageView.setImageDrawable(iv.getDrawable());
                                                                    imageView.setTag(String.valueOf(finalI));
                                                                    thisView.addView(imageView);
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
                                                    ImageView iv = (ImageView) addl.getChildAt(finalI);
                                                    ImageView imageView = new ImageView(thisView.getContext());
                                                    imageView.setImageDrawable(iv.getDrawable());
                                                    imageView.setTag(String.valueOf(finalI));
                                                    thisView.addView(imageView);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                private boolean wallcheck() {
                    ImageView res;
                    res = null;
                    if ((ImageView) thisView.findViewWithTag(String.valueOf(0)) != null && ((finalI - 10) == 0)) {
                        res = (ImageView) thisView.findViewWithTag(String.valueOf(0));
                    }
                    if ((ImageView) thisView.findViewWithTag(String.valueOf(1)) != null && ((finalI - 10) == 2)) {
                        res = (ImageView) thisView.findViewWithTag(String.valueOf(1));
                    }
                    if ((ImageView) thisView.findViewWithTag(String.valueOf(6)) != null && ((finalI - 10) == 3)) {
                        res = (ImageView) thisView.findViewWithTag(String.valueOf(6));
                    }if ((ImageView) thisView.findViewWithTag(String.valueOf(7)) != null && ((finalI - 10) == 1)) {
                        res = (ImageView) thisView.findViewWithTag(String.valueOf(7));
                    }
                    return (res == null);
                }

                private boolean exitcheck() {
                    ImageView res;
                    res = null;
                    if ((ImageView) thisView.findViewWithTag(String.valueOf(10)) != null && ((finalI + 10) == 10)) {
                        res = (ImageView) thisView.findViewWithTag(String.valueOf(10));
                    }
                    if ((ImageView) thisView.findViewWithTag(String.valueOf(12)) != null && ((finalI + 10) == 11)) {
                        res = (ImageView) thisView.findViewWithTag(String.valueOf(12));
                    }
                    if ((ImageView) thisView.findViewWithTag(String.valueOf(13)) != null && ((finalI + 6) == 12)) {
                        res = (ImageView) thisView.findViewWithTag(String.valueOf(13));
                    }
                    if ((ImageView) thisView.findViewWithTag(String.valueOf(11)) != null && ((finalI + 6) == 13)) {
                        res = (ImageView) thisView.findViewWithTag(String.valueOf(11));
                    }

                    return (res == null);
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

                private ImageView checkWAS(int finalI) {
                    ImageView res;
                    res = null;
                    for (int j = 2; j < 6; j++) {
                        if ((ImageView) thisView.findViewWithTag(String.valueOf(j)) != null) {
                            res = (ImageView) thisView.findViewWithTag(String.valueOf(j));
                        }
                    }
                    for (int j = 8; j < 10; j++) {
                        if ((ImageView) thisView.findViewWithTag(String.valueOf(j)) != null) {
                            res = (ImageView) thisView.findViewWithTag(String.valueOf(j));
                        }
                    }

                    for (int j = 14; j < addl.getChildCount(); j++) {
                        if ((ImageView) thisView.findViewWithTag(String.valueOf(j)) != null) {
                            res = (ImageView) thisView.findViewWithTag(String.valueOf(j));
                        }
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
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                thisView = relativeLayout;
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
                    addcol();
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
                    addline();
                }
            }
        });

    }

    private void KillCellElem(int finalI) {
        if (thisView.findViewWithTag(String.valueOf(finalI)) != null) {
            final int[] cordin = new int[]{Integer.parseInt(thisView.getTag().toString().split(" ")[0]), Integer.parseInt(thisView.getTag().toString().split(" ")[1])};
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
                        BC = new int[] {0, 0};
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
                    minotaurAmmount --;
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
                    bulletAmmount --;
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

    private void addline() {
        TextView wc = (TextView) findViewById(R.id.WCounter);
        TextView hc = (TextView) findViewById(R.id.HCounter);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.workspace);
        for (int i = 0; i < Integer.parseInt(wc.getText().toString()); i++) {
            final RelativeLayout relativeLayout = new RelativeLayout(this);
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.walls0);
            relativeLayout.addView(imageView);
            relativeLayout.setTag((Integer.parseInt(hc.getText().toString()) - 1) + " " + String.valueOf(i));
            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mDetector.onTouchEvent(motionEvent);
                    thisView = relativeLayout;
                    return true;
                }
            });
            RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            rules.setMargins(OFFSET_LEFT + CELLSIZE * i, OFFSET_TOP + CELLSIZE * (Integer.parseInt(hc.getText().toString()) - 1), 100, 100);
            layout.addView(relativeLayout, rules);
        }
    }

    private void addcol() {
        TextView wc = (TextView) findViewById(R.id.WCounter);
        TextView hc = (TextView) findViewById(R.id.HCounter);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.workspace);
        for (int i = 0; i < Integer.parseInt(hc.getText().toString()); i++) {
            final RelativeLayout relativeLayout = new RelativeLayout(this);
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.walls0);
            relativeLayout.addView(imageView);
            relativeLayout.setTag(String.valueOf(i) + " " + (Integer.parseInt(wc.getText().toString()) - 1));
            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mDetector.onTouchEvent(motionEvent);
                    thisView = relativeLayout;
                    return true;
                }
            });
            RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            rules.setMargins(OFFSET_LEFT +CELLSIZE * (Integer.parseInt(wc.getText().toString()) - 1), OFFSET_TOP +CELLSIZE * i, 100, 100);
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
