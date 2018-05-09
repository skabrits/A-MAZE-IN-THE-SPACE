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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.sevak.themaze.StartPage.CELLSIZE;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze_builder);

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
                int[][] PMaze = new int[Integer.parseInt(hc.getText().toString())][Integer.parseInt(wc.getText().toString())];
                for (int i = 0; i < PMaze.length; i++) {
                    for (int j = 0; j < PMaze[0].length; j++) {
                        PMaze[i][j] = TMaze[i][j];
                    }
                }

                if (AllIsOk(PMaze)) {
                    MazeHolder.MazeExample Mazik = new MazeHolder.MazeExample();
                    Mazik.Maze = PMaze;
                    Mazik.BasicCordinats = BC;
                    MazeHolder.MazeArr.add(Mazik);
                }
            }

            private boolean AllIsOk(int[][] pMaze) {
                boolean res = true;
                if (exitPlaced && IamPlaced) {
                    for (int i = 0; i < pMaze.length; i++) {
                        if (i == 0 || i == pMaze.length - 1) {
                            for (int j = 0; j < pMaze[0].length; j++) {
                                if (pMaze[i][j] != 1 && pMaze[i][j] != 2){
                                    res = false;
                                    Toast t = Toast.makeText(getApplicationContext(), "You haven't placed all walls yet", Toast.LENGTH_SHORT);
                                    t.show();
                                }
                            }
                        } else {
                            if ((pMaze[i][0] != 1 && pMaze[i][0] != 2) || (pMaze[i][pMaze[0].length - 1] != 1 && pMaze[i][pMaze[0].length - 1] != 2)){
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
                    if (finalI == 9) {
                        if (!IamPlaced) {
                            int[] cordin = new int[] {Integer.parseInt(thisView.getTag().toString().split(" ")[0]), Integer.parseInt(thisView.getTag().toString().split(" ")[1])};
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
                                final int[] cordin = new int[] {Integer.parseInt(thisView.getTag().toString().split(" ")[0]), Integer.parseInt(thisView.getTag().toString().split(" ")[1])};
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
                                Toast t = Toast.makeText(getApplicationContext(), "You have placed exit already", Toast.LENGTH_SHORT);
                                t.show();
                            }
                        } else {
                            if ((checkWAS() == null) || ((finalI >= 0) && (finalI < 2)) || ((finalI >= 6) && (finalI < 8))) {
                                final int[] cordin = new int[] {Integer.parseInt(thisView.getTag().toString().split(" ")[0]), Integer.parseInt(thisView.getTag().toString().split(" ")[1])};
                                switch (finalI) {
                                    case 0:{
                                        TMaze[cordin[0] * 2][(cordin[1] * 2 + 1)] = 1;
                                        break;
                                    }
                                    case 1:{
                                        TMaze[cordin[0] * 2 + 2][(cordin[1] * 2 + 1)] = 1;
                                        break;
                                    }
                                    case 2:{
                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 12;
                                        break;
                                    }
                                    case 3:{
                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 13;
                                        break;
                                    }
                                    case 4:{
                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 14;
                                        break;
                                    }
                                    case 5:{
                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = 11;
                                        break;
                                    }
                                    case 6:{
                                        TMaze[(cordin[0] * 2 + 1)][cordin[1] * 2] = 1;
                                        break;
                                    }
                                    case 7:{
                                        TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 2)] = 1;
                                        break;
                                    }
                                    case 8:{
                                        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.workspace);
                                        final EditText et = new EditText(rl.getContext());
                                        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        p.setMargins(300, 10, 0, 0);
                                        et.setHint("write tp number in form xyy, x - series number, yy - teleport number in series");
                                        rl.addView(et, p);
                                        final Button bt = new Button(rl.getContext());
                                        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(
                                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        p1.setMargins(350, 10, 0, 0);
                                        rl.addView(bt, p1);
                                        bt.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int tpnum = Integer.parseInt(et.getText().toString());
                                                if (checkTPexist(tpnum) && (tpnum % 100 >= 1) && (tpnum % 100 <= 9) && (tpnum - (tpnum % 100) * 100 >= 1) && (tpnum - (tpnum % 100) * 100 <= 99)) {
                                                    TMaze[(cordin[0] * 2 + 1)][(cordin[1] * 2 + 1)] = tpnum;
                                                    tp_num.put(thisView.getTag().toString(),tpnum);
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
                                                    if (tpnum == i){
                                                        res = false;
                                                    }
                                                }
                                                return res;
                                            }
                                        });
                                        break;
                                    }
                                    case 14:{

                                        break;
                                    }
                                    case 15:{

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

                private ImageView checkWAS() {
                    ImageView res;
                    res = null;
                    for (int j = 2; j < 6; j++) {
                        if ((ImageView) thisView.findViewWithTag(String.valueOf(j)) != null) {
                            res = (ImageView) thisView.findViewWithTag(String.valueOf(j));
                        }
                    }
                    for (int j = 8; j < addl.getChildCount(); j++) {
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
            }
            if (finalI <= 13 && finalI >= 9) {
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
            RelativeLayout rl = thisView;
            thisView = view;
            final int finalI = i;
            KillCellElem(finalI);
            layout.removeView(view);
            thisView = rl;
        }
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
            RelativeLayout rl = thisView;
            thisView = view;
            final int fi = i;
            KillCellElem(fi);
            layout.removeView(view);
            thisView = rl;
        }
    }
}
