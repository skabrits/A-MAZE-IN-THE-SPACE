package com.example.sevak.themaze;

import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class StartPage extends AppCompatActivity {

    private GestureDetector mDetector;
    private float ConvDPtoPX(float dp){
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        return dp*((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private HashMap<View, Integer> maps = new HashMap<>();
    private HashMap<int[][], String> transitions = new HashMap<>();
    public static final int CELLSIZE = 140;
    public static final int TURN_NA = 0;
    public static final int TURN_UP = 1;
    public static final int TURN_RIGHT = 2;
    public static final int TURN_DOWN = 3;
    public static final int TURN_LEFT = 4;
    public static final int TELEPORT = 100;
    public static final int OFFSET_LEFT = 400;
    public static final int OFFSET_TOP = 400;
    public static final int OFFSET_BETWEEN = 10;
    public static final int MINOTAUR = 5;
    public static final int HOSPITAL = 6;
    public static final int BULLET = 4;
    public static final int BFG = 3;
    public static final int KEY = 2;
    public static final int DEAD_MINOTAUR = 7;
    public static final int USED_BULLET = 8;
    private Integer bolLayout;
    private Integer exitLayout;
    private Integer layoutAmount = 1;
    private Integer CurrentLayout = 1;
    private Integer NativeLayout = 1;
    private Integer ThisLayout = 0;
    private Integer bulletAmount = 0;
    private Boolean isAnYweapon = false;
    private Boolean isAnYkey = false;
    private Boolean bolnitsaDescovered = false;
    private Boolean exitDescovered = false;
//TODO: merge current location and global map pages
    private int[] zerocor = new int[2];
    private int[] BolLaycor = new int[2];
    private int[] exitLaycor = new int[2];
    private int[] CurBasicCord  = new int[2];
    public static int vX, vY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        findViewById(R.id.rev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Predbannik.class));
            }
        });

        int Cellsize = (int) ConvDPtoPX(41);

        Maze.init();
        ImageView c1 = (ImageView) findViewById(R.id.C1);
        zerocor[0]=(int) ConvDPtoPX(150);
        zerocor[1]=(int) ConvDPtoPX(180);
        CurBasicCord[0] = Maze.YourCordInMaze[0];
        CurBasicCord[1] = Maze.YourCordInMaze[1];
        changeIdCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.milkiipidoras, R.id.Me);
        changeTagIdCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.milkiipidoras);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.p0l);

        layout.setOnDragListener(new View.OnDragListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                ImageView iv = (ImageView) findViewById(R.id.Trash_Can);
                View view1 = (View) dragEvent.getLocalState();
                ScrollView vv = (ScrollView) findViewById(R.id.VV);
                HorizontalScrollView hv = (HorizontalScrollView) findViewById(R.id.HV);
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // do nothing
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        if ((dragEvent.getX() >= -hv.getX()+iv.getX()+hv.getScrollX()) && (dragEvent.getX() <= (-hv.getX()+iv.getX()+hv.getScrollX() + iv.getWidth())) && (dragEvent.getY() >= -hv.getY()+iv.getY()+vv.getScrollY()) && (dragEvent.getY() <= (-hv.getY()+iv.getY()+vv.getScrollY() + iv.getHeight()))) {
                            if (!view1.getTag().equals ("p"+NativeLayout+"l")) {
                                deleteLayout((String) view1.getTag());
                            }
                        } else if ((dragEvent.getX() >= OFFSET_LEFT) && (dragEvent.getX() <= (OFFSET_LEFT + Maze.SIZE_X*Cellsize)) && (dragEvent.getY() >= OFFSET_TOP) && (dragEvent.getY() <= (OFFSET_TOP + Maze.SIZE_Y*Cellsize))) {
                            view1.bringToFront();
                            view1.animate()
                                    .x(((int)(dragEvent.getX() - OFFSET_LEFT) / Cellsize)*Cellsize + OFFSET_LEFT - (Maze.SIZE_X - 1) * Cellsize)
                                    .y(((int)(dragEvent.getY() - OFFSET_TOP) / Cellsize)*Cellsize + OFFSET_TOP - (Maze.SIZE_Y - 1) * Cellsize)
                                    .setDuration(700)
                                    .start();
                        } else {
                            view1.animate()
                                    .x(dragEvent.getX() - (Maze.SIZE_X - 1) * Cellsize)
                                    .y(dragEvent.getY() - (Maze.SIZE_Y - 1) * Cellsize)
                                    .setDuration(700)
                                    .start();
                        }
                        int oldcord = maps.get(view1);
                        int rasnitsa = (int) Math.abs(dragEvent.getY() - oldcord);
                        maps.replace(view1, ((int) dragEvent.getY()));

                        view1.setVisibility(View.VISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                    default:
                        break;
                }
                return true;
            }
        });
        ConstraintLayout layoutbd = (ConstraintLayout) findViewById(R.id.bigDaddy);

        layoutbd.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // do nothing
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        // Dropped, reassign View to ViewGroup
                        View view1 = (View) dragEvent.getLocalState();
                        view1.setVisibility(View.VISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                    default:
                        break;
                }
                return true;
            }
        });


        for (int i = 0; i < ((Maze.Maze.length-1)/2); i++) {
            for (int j = 0; j < ((Maze.Maze[1].length-1)/2); j++) {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.walls0);
                RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
                imageView.setAlpha(50);
                rules.setMargins(OFFSET_LEFT +Cellsize*j, OFFSET_TOP +Cellsize*i, 0, 0);
                layout.addView(imageView, rules);
            }
        }

        layout.findViewWithTag("p1l").getBackground().setAlpha(0);
        ViewGroup.MarginLayoutParams trules1 = (ViewGroup.MarginLayoutParams) layout.findViewWithTag("p1l").getLayoutParams();
        trules1.setMargins(OFFSET_LEFT - Cellsize, (int) (OFFSET_TOP + Cellsize * Maze.SIZE_Y + OFFSET_BETWEEN), 0, 0);
        layout.findViewWithTag("p1l").requestLayout();
        maps.put(new RelativeLayout(getApplicationContext()), OFFSET_TOP);
        maps.put(layout.findViewWithTag("p1l"), ((ViewGroup.MarginLayoutParams) layout.findViewWithTag("p1l").getLayoutParams()).topMargin);

        final RelativeLayout relativeLayout = (RelativeLayout) layout.findViewWithTag("p1l");

        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                            view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    view.setVisibility(View.INVISIBLE);
                    return true;
                } else {
                    return false;
                }
            }
        });

        ImageView imageView = (ImageView) relativeLayout.findViewWithTag("C1");
        ViewGroup.MarginLayoutParams trules1c = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
        trules1c.setMargins((Maze.SIZE_X - 1) * Cellsize, (Maze.SIZE_Y - 1) * Cellsize, 0, 0);
        imageView.requestLayout();

        RelativeLayout touch = (RelativeLayout) findViewById(R.id.p1l);
        mDetector = new GestureDetector(this, new MyGestureListener());
        touch.setClickable(true);
        touch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return true;
            }
        });


        Button p0 = (Button) findViewById(R.id.SP1);
        p0.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ImageView iv = (ImageView) findViewById(R.id.Trash_Can);
                iv.setVisibility(View.GONE);
                goToThisLayout(1);
            }
        });

        Button start = (Button) findViewById(R.id.GM1);
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ImageView iv = (ImageView) findViewById(R.id.Trash_Can);
                iv.setVisibility(View.VISIBLE);
                goToThisLayout(0);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private int positionCount (MotionEvent e) {
            int Cellsize = (int) ConvDPtoPX(41);
            int x = (int)e.getX();
            int y = (int)e.getY();
            int curx = zerocor[1] - (CurBasicCord[1] - Maze.YourCordInMaze[1]) * Cellsize/2;
            int cury = zerocor[0] - (CurBasicCord[0] - Maze.YourCordInMaze[0]) * Cellsize/2;
            int xin = (x>curx + Cellsize) ? 1 : ((x<curx) ? -1 : 0);
            int yin = (y>cury + Cellsize) ? 1 : ((y<cury) ? -1 : 0);
            int xin1 = (x - (curx + Cellsize/2) > y - (cury + Cellsize/2)) ? 1 : -1;
            int yin1 = (x - (curx + Cellsize/2) < - y + (cury + Cellsize/2)) ? 1 : -1;
            if (xin == 0 && yin == 0) {
                return (TURN_NA);
            } else {
                if (xin1 < 0) {
                    if (yin1 < 0)
                        return (TURN_DOWN);
                    else
                        return (TURN_LEFT);
                } else {
                    if (yin1 < 0)
                        return (TURN_RIGHT);
                    else
                        return (TURN_UP);
                }
            }
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            shoot(positionCount(e));
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            turn(positionCount(e));
            return true;
        }
    }

    static int[] Bulletcoord = new int[2];
    private void shoot(int side) {
        if (bulletAmount > 0) {
            if (Maze.isWeapon) {
                if (isAnYweapon) {
                    shotbody(side);
                    bulletAmount --;
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), "You have no weapon", Toast.LENGTH_SHORT);
                    t.show();
                }
            } else {
                shotbody(side);
                bulletAmount --;
            }
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "Not enough bullets, baby", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    private void shotbody(int side) {
        boolean end = true;
        Toast t = Toast.makeText(getApplicationContext(), "Boom, Shaka-laka", Toast.LENGTH_SHORT);
        t.show();
        Bulletcoord = new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]};
        while (end) {
            switch (side) {
                case TURN_UP: {
                    if (Maze.Maze[Bulletcoord[0] - 1][Bulletcoord[1]] == 0) {
                        goforBullet(Bulletcoord, (int) Math.pow(10, side - 1));
                    } else {
                        end = false;
                    }
                    break;
                }
                case TURN_RIGHT: {
                    if (Maze.Maze[Bulletcoord[0]][Bulletcoord[1] + 1] == 0) {
                        goforBullet(Bulletcoord, (int) Math.pow(10, side - 1));
                    } else {
                        end = false;
                    }
                    break;
                }
                case TURN_DOWN: {
                    if (Maze.Maze[Bulletcoord[0] + 1][Bulletcoord[1]] == 0) {
                        goforBullet(Bulletcoord, (int) Math.pow(10, side - 1));
                    } else {
                        end = false;
                    }
                    break;
                }
                case TURN_LEFT: {
                    if (Maze.Maze[Bulletcoord[0]][Bulletcoord[1] - 1] == 0) {
                        goforBullet(Bulletcoord, (int) Math.pow(10, side - 1));
                    } else {
                        end = false;
                    }
                    break;
                }
            }
        }
    }

    private void goforBullet(int[] cord, int side) {
        switch (side) {
            case 1: {
                if (Maze.Maze[cord[0] - 2][cord[1]] == MINOTAUR) {
                    Maze.Maze[cord[0] - 2][cord[1]] = DEAD_MINOTAUR;
                }
                Bulletcoord[0] = cord[0] - 2;
                break;
            }
            case 10: {
                if (Maze.Maze[cord[0]][cord[1] + 2] == MINOTAUR) {
                    Maze.Maze[cord[0]][cord[1] + 2] = DEAD_MINOTAUR;
                }
                Bulletcoord[1] = cord[1] + 2;
                break;
            }
            case 100: {
                if (Maze.Maze[cord[0] + 2][cord[1]] == MINOTAUR) {
                    Maze.Maze[cord[0] + 2][cord[1]] = DEAD_MINOTAUR;
                }
                Bulletcoord[0] = cord[0] + 2;
                break;
            }
            case 1000: {
                if (Maze.Maze[cord[0]][cord[1] - 2] == MINOTAUR) {
                    Maze.Maze[cord[0]][cord[1] - 2] = DEAD_MINOTAUR;
                }
                Bulletcoord[1] = cord[1] - 2;
                break;
            }
        }
    }

    private void teleport(int[] cord) {
        moveTOnewlayout();
        int teleport_number = Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]];
        int[] teleportCoord = Maze.Teleports.get(teleport_number+1);
        if (teleportCoord == null)
            teleportCoord = Maze.Teleports.get(((int)teleport_number/100)*100+1);

        if (teleportCoord != null) {
            Maze.YourMazesholder.get(NativeLayout)[teleportCoord[0]][teleportCoord[1]] = 0;
            Maze.YourCordInMaze[0] = teleportCoord[0];
            Maze.YourCordInMaze[1] = teleportCoord[1];
        }
        CurBasicCord[0] = Maze.YourCordInMaze[0];
        CurBasicCord[1] = Maze.YourCordInMaze[1];
        Maze.YourMazesholder.get(NativeLayout)[CurBasicCord[0]][CurBasicCord[1]] = 0;
        changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.teleport1);
    }
//TODO: waitin g before teleporting to draw everything
//    try {
//        Thread.sleep(500);
//    } catch (InterruptedException e) {
//        e.printStackTrace();
//    }
    private void moveTOnewlayout() {
        ImageView iMv = (ImageView) findViewById(R.id.Me);
        delView(iMv);
        ImageView iMvc = (ImageView) findViewById(R.id.Mec);
        delTagView(iMvc);
        prepareNEWlayout();
        setLayoutAScurrent(layoutAmount);
        CurrentLayout = layoutAmount;
        NativeLayout = CurrentLayout;
        goToThisLayout(layoutAmount);
        ThisLayout = NativeLayout;
    }

    private void setLayoutAScurrent(final Integer ln){
        Button button = (Button) findViewById(R.id.SP1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToThisLayout(ln);
            }
        });
    }

    private void prepareNEWlayout() {
        int Cellsize = (int) ConvDPtoPX(41);
        int[][] YourMaze = new int[Maze.YourMaze.length][Maze.YourMaze[1].length];
        for (int i = 0; i < YourMaze.length; i++) {
            for (int j = 0; j < YourMaze[1].length; j++) {
                YourMaze[i][j] = -1;
            }
        }
        layoutAmount += 1;
        Maze.YourMazesholder.put(layoutAmount, YourMaze);

        ConstraintLayout container = (ConstraintLayout) findViewById(R.id.Container);
        RelativeLayout Rlc = new RelativeLayout(this);
        Rlc.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGreen));
        String nameStr1c = "p" + layoutAmount.toString() + "ln";
        Rlc.setTag(nameStr1c);
        Rlc.setClickable(true);
        ConstraintLayout.LayoutParams rules1c = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        Rlc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
        Rlc.setMinimumHeight((int) ConvDPtoPX(443));
        Rlc.setMinimumWidth((int) ConvDPtoPX(368));
        container.addView(Rlc, rules1c);

        ImageView imageViewc = new ImageView(this);
        imageViewc.setImageResource(R.drawable.walls0);
        RelativeLayout.LayoutParams rules2c = new RelativeLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        rules2c.setMargins((int) zerocor[1], (int) zerocor[0], 100, 100);
        Rlc.addView(imageViewc, rules2c);

        RelativeLayout layoutbd = (RelativeLayout) findViewById(R.id.p0l);
        final RelativeLayout Rl = new RelativeLayout(this);
        String idStr1 = "p" + layoutAmount.toString() + "l";
        Rl.setClickable(true);
        ConstraintLayout.LayoutParams rules1 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        rules1.setMargins(OFFSET_LEFT - Cellsize, (int) (Collections.max(maps.values()) + 2 * (Cellsize * Maze.SIZE_Y + OFFSET_BETWEEN)),
                0, 100);

        Rl.setTag(idStr1);

        Rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                            view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    view.setVisibility(View.INVISIBLE);
                    return true;
                } else {
                    return false;
                }
            }
        });

        layoutbd.addView(Rl, rules1);

        maps.put(Rl, ((ViewGroup.MarginLayoutParams) Rl.getLayoutParams()).topMargin);

        ImageView imageView = new ImageView(this);
        imageView.setTag("C"+layoutAmount);
        imageView.setImageResource(R.drawable.walls0);
        RelativeLayout.LayoutParams rules2 = new RelativeLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        rules2.setMargins(Cellsize * (Maze.SIZE_X - 1), Cellsize * (Maze.SIZE_Y -1), 0, 100);
        Rl.addView(imageView, rules2);
        ImageView me = new ImageView(this);
        me.setId(R.id.Mec);
        me.setImageResource(R.drawable.milkiipidoras);
        Rl.addView(me, rules2);
    }

    private void deleteLayout(String res) {
        if (Integer.parseInt(res.substring(1).split("l")[0]) == bolLayout) {
            bolnitsaDescovered = false;
        }
        if (Integer.parseInt(res.substring(1).split("l")[0]) == exitLayout) {
            exitDescovered = false;
        }
        ConstraintLayout l = (ConstraintLayout) findViewById(R.id.Container);
        l.removeView(l.findViewWithTag((res+"n")));
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.p0l);
        maps.remove(layout.findViewWithTag(res));
        layout.removeView(layout.findViewWithTag(res));
    }

    private void goToThisLayout(Integer LayoutNumber) {
        ConstraintLayout mlay = (ConstraintLayout) findViewById(R.id.Container);
        String tname = "p" + ThisLayout.toString() + "ln";
        String cname = "p" + LayoutNumber.toString() + "ln";
        RelativeLayout thisL = (RelativeLayout) mlay.findViewWithTag(tname);
        RelativeLayout changeL = (RelativeLayout) mlay.findViewWithTag(cname);
        thisL.setVisibility(View.GONE);
        changeL.setVisibility(View.VISIBLE);
        ThisLayout = LayoutNumber;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void river(int[] cord){
        switch (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]]) {
            case 11:{
                if (Maze.Maze[Maze.YourCordInMaze[0] - 2][Maze.YourCordInMaze[1]] > 10) {
                    goFORriver(cord, 1);
                } else {
                    moveTOnewlayout();
                    CurBasicCord[0] = Maze.YourCordInMaze[0];
                    CurBasicCord[1] = Maze.YourCordInMaze[1];
                    Maze.YourMazesholder.get(NativeLayout)[CurBasicCord[0]][CurBasicCord[1]] = 0;
                    changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.river);
                    go(Maze.YourCordInMaze, 1);
                }
                //changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.riverup);
                break;
            }
            case 12:{
                if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1] + 2] > 10) {
                    goFORriver(cord, 10);
                } else {
                    moveTOnewlayout();
                    CurBasicCord[0] = Maze.YourCordInMaze[0];
                    CurBasicCord[1] = Maze.YourCordInMaze[1];
                    Maze.YourMazesholder.get(NativeLayout)[CurBasicCord[0]][CurBasicCord[1]] = 0;
                    changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.river);
                    go(Maze.YourCordInMaze, 10);
                }
                //changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.riverleft);
                break;
            }
            case 13:{
                if (Maze.Maze[Maze.YourCordInMaze[0] + 2][Maze.YourCordInMaze[1]] > 10) {
                    goFORriver(cord, 100);
                } else {
                    moveTOnewlayout();
                    CurBasicCord[0] = Maze.YourCordInMaze[0];
                    CurBasicCord[1] = Maze.YourCordInMaze[1];
                    Maze.YourMazesholder.get(NativeLayout)[CurBasicCord[0]][CurBasicCord[1]] = 0;
                    changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.river);
                    go(Maze.YourCordInMaze, 100);
                }
                //changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.riverdown);
                break;
            }
            case 14:{
                if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1] - 2] > 10) {
                    goFORriver(cord, 1000);
                } else {
                    moveTOnewlayout();
                    CurBasicCord[0] = Maze.YourCordInMaze[0];
                    CurBasicCord[1] = Maze.YourCordInMaze[1];
                    Maze.YourMazesholder.get(NativeLayout)[CurBasicCord[0]][CurBasicCord[1]] = 0;
                    changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.river);
                    go(Maze.YourCordInMaze, 1000);
                }
                //changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.riverright);
                break;
            }
        }
    }
    //TODO: simplify walking interface (triangle areas instead of rectangles)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void go(int[] cord, int side){
        boolean b = false;
        switch (side){
            case 1: {
                if (Maze.YourMazesholder.get(NativeLayout)[cord[0] - 1][cord[1]] != 0) {
                    Maze.YourMazesholder.get(NativeLayout)[cord[0] - 1][cord[1]] = 0;
                    Maze.YourMazesholder.get(NativeLayout)[cord[0] - 2][cord[1]] = 0;
                } else {
                    b = true;
                }
                Maze.YourCordInMaze[0] = cord[0] - 2;
                if (!b) {
                    changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.walls0);
                }
                GoEvents();
                break;
            }
            case 10: {
                if (Maze.YourMazesholder.get(NativeLayout)[cord[0]][cord[1] + 1] != 0) {
                    Maze.YourMazesholder.get(NativeLayout)[cord[0]][cord[1] + 1] = 0;
                    Maze.YourMazesholder.get(NativeLayout)[cord[0]][cord[1] + 2] = 0;
                } else {
                    b = true;
                }
                Maze.YourCordInMaze[1] = cord[1] + 2;
                if (!b) {
                    changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.walls0);
                }
                GoEvents();
                break;
            }
            case 100: {
                if (Maze.YourMazesholder.get(NativeLayout)[cord[0] + 1][cord[1]] != 0) {
                    Maze.YourMazesholder.get(NativeLayout)[cord[0] + 1][cord[1]] = 0;
                    Maze.YourMazesholder.get(NativeLayout)[cord[0] + 2][cord[1]] = 0;
                } else {
                    b = true;
                }
                Maze.YourCordInMaze[0] = cord[0] + 2;
                if (!b) {
                    changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.walls0);
                }
                GoEvents();
                break;
            }
            case 1000: {
                if (Maze.YourMazesholder.get(NativeLayout)[cord[0]][cord[1] - 1] != 0) {
                    Maze.YourMazesholder.get(NativeLayout)[cord[0]][cord[1] - 1] = 0;
                    Maze.YourMazesholder.get(NativeLayout)[cord[0]][cord[1] - 2] = 0;
                } else {
                    b = true;
                }
                Maze.YourCordInMaze[1] = cord[1] - 2;
                if (!b) {
                    changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.walls0);
                }
                GoEvents();
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void GoEvents() {
        ImageView iv = (ImageView) findViewById(R.id.Me);
        delView(iv);
        changeIdCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.milkiipidoras, R.id.Me);
        ImageView ivc = (ImageView) findViewById(R.id.Mec);
        delTagView(ivc);
        changeTagIdCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.milkiipidoras);
        checkTheRvrORtp();
        checkMinotaur();
        checkBullet();
        checkHospital();
        if (Maze.isWeapon) {
            checkBFG();
        }
        if (Maze.isKey) {
            checkKEY();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkHospital() {
        if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] == HOSPITAL) {
            if (!bolnitsaDescovered) {
                bolLayout = ThisLayout;
                BolLaycor[0] = CurBasicCord[0];
                BolLaycor[1] = CurBasicCord[1];
                bolnitsaDescovered = true;
                changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.hospital);
            } else {
                if (!NativeLayout.equals(bolLayout)) {
                    mergeLayouts(bolLayout, CurrentLayout);
                    bolLayout = NativeLayout;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void mergeLayouts(Integer l1, Integer l2) {
        ImageView iMv = (ImageView) findViewById(R.id.Me);
        delView(iMv);
        ImageView iMvc = (ImageView) findViewById(R.id.Mec);
        delTagView(iMvc);
        prepareNEWlayout();
        CurrentLayout = layoutAmount;
        NativeLayout = CurrentLayout;
        goToThisLayout(layoutAmount);
        ThisLayout = NativeLayout;
        setLayoutAScurrent(layoutAmount);
        for (int i = 0; i < Maze.YourMazesholder.get(l1).length; i++) {
            for (int j = 0; j < Maze.YourMazesholder.get(l1)[0].length; j++) {
                if (Maze.YourMazesholder.get(l1)[i][j] == -1) {
                    Maze.YourMazesholder.get(l1)[i][j] = Maze.YourMazesholder.get(l2)[i][j];
                }
            }
        }
        Maze.YourMazesholder.replace(NativeLayout, Maze.YourMazesholder.get(l1));

        for (int i = 0; i < Maze.YourMazesholder.get(NativeLayout).length; i++) {
            for (int j = 0; j < Maze.YourMazesholder.get(NativeLayout)[0].length; j++) {
                if (Maze.YourMazesholder.get(NativeLayout)[i][j] != -1) {
                    if (i % 2 == 1) {
                        if (j % 2 == 1)
                            changeCell(new int[]{i, j}, R.drawable.walls0);
                    }
                }
            }
        }

        for (int i = 0; i < Maze.YourMazesholder.get(NativeLayout).length; i++) {
            for (int j = 0; j < Maze.YourMazesholder.get(NativeLayout)[0].length; j++) {
                if (Maze.YourMazesholder.get(NativeLayout)[i][j] != -1) {
                    if (i % 2 == 1) {
                        if (j % 2 == 1)
                            checkForContent(new int[]{i, j}, Maze.Maze);
                        else {
                            try {
                                if (Maze.YourMazesholder.get(NativeLayout)[i][j - 1] != -1)
                                    checkForTheWalls(new int[]{i, j}, Maze.Maze, R.drawable.wall1rightpr, R.drawable.exitright, new int[]{i, j-1});
                            } catch (ArrayIndexOutOfBoundsException e) {
                                if (Maze.YourMazesholder.get(NativeLayout)[i][j + 1] != -1)
                                    checkForTheWalls(new int[]{i, j}, Maze.Maze, R.drawable.wall1leftpr, R.drawable.exitleft, new int[]{i, j+1});
                            }
                            try {
                                if (Maze.YourMazesholder.get(NativeLayout)[i][j + 1] != -1)
                                    checkForTheWalls(new int[]{i, j}, Maze.Maze, R.drawable.wall1leftpr, R.drawable.exitleft, new int[]{i, j+1});
                            } catch (ArrayIndexOutOfBoundsException e) {
                                if (Maze.YourMazesholder.get(NativeLayout)[i][j - 1] != -1)
                                    checkForTheWalls(new int[]{i, j}, Maze.Maze, R.drawable.wall1rightpr, R.drawable.exitright, new int[]{i, j-1});
                            }
                        }
                    } else {
                        if (j % 2 == 1) {
                            try {
                                if (Maze.YourMazesholder.get(NativeLayout)[i - 1][j] != -1)
                                    checkForTheWalls(new int[]{i, j}, Maze.Maze, R.drawable.wall1downpr, R.drawable.exitdown, new int[]{i-1, j});
                            } catch (ArrayIndexOutOfBoundsException e) {
                                if (Maze.YourMazesholder.get(NativeLayout)[i + 1][j] != -1)
                                    checkForTheWalls(new int[]{i, j}, Maze.Maze, R.drawable.wall1uppr, R.drawable.exitup, new int[]{i+1, j});
                            }
                            try {
                                if (Maze.YourMazesholder.get(NativeLayout)[i + 1][j] != -1)
                                    checkForTheWalls(new int[]{i, j}, Maze.Maze, R.drawable.wall1uppr, R.drawable.exitup, new int[]{i+1, j});
                            } catch (ArrayIndexOutOfBoundsException e) {
                                if (Maze.YourMazesholder.get(NativeLayout)[i - 1][j] != -1)
                                    checkForTheWalls(new int[]{i, j}, Maze.Maze, R.drawable.wall1downpr, R.drawable.exitdown, new int[]{i-1, j});
                            }
                        }
                    }
                }
            }
        }
        deleteLayout("p"+l1+"l");
        deleteLayout("p"+l2+"l");
    }

    private void checkForContent(int[] cord, int[][] maze) {
        switch (maze[cord[0]][cord[1]]) {
            case 11: {
                changeCell(cord, R.drawable.river);
                break;
            }
            case 12: {
                changeCell(cord, R.drawable.river);
                break;
            }
            case 13: {
                changeCell(cord, R.drawable.river);
                break;
            }
            case 14: {
                changeCell(cord, R.drawable.river);
                break;
            }
            case DEAD_MINOTAUR: {
                changeCell(cord, R.drawable.deadminotavr);
                break;
            }
            case MINOTAUR: {
                changeCell(cord, R.drawable.minotavr);
                break;
            }
            case HOSPITAL: {
                changeCell(cord, R.drawable.hospital);
                break;
            }
            case BFG: {
                changeCell(cord, R.drawable.bfg);
                break;
            }
            case BULLET: {
                changeCell(cord, R.drawable.bullet);
                break;
            }
            case KEY: {
                changeCell(cord, R.drawable.key);
                break;
            }
            default:{
                if (maze[cord[0]][cord[1]] > TELEPORT && maze[cord[0]][cord[1]] < 1000){
                    changeCell(cord, R.drawable.teleport1);
                }
            }
        }
    }


    private void checkForTheWalls(int[] cord, int[][] maze, int res, int res1, int[] placecord) {
        switch (maze[cord[0]][cord[1]]) {
            case 1: {
                changeCell(placecord, res);
                break;
            }
            case 2: {
                changeCell(placecord, res1);
                break;
            }
        }
    }


    private void checkKEY() {
        if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] == KEY) {
            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.key);
            Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] = 0;
            isAnYkey = true;
        }
    }

    private void checkBFG() {
        if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] == BFG) {
            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.bfg);
            Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] = 0;
            isAnYweapon = true;
        }
    }

    private void checkBullet() {
        if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] == BULLET) {
            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.bullet);
            Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] = 8;
//            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.usedbullet);
            bulletAmount += 1;
        }
        if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] == USED_BULLET) {
            //changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.usedbullet);
        }
    }

    private void checkMinotaur() {
        if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] == MINOTAUR) {
            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.minotavr);
            die(Maze.YourCordInMaze);
        }
        if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] == DEAD_MINOTAUR) {
            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.deadminotavr);
        }
    }

    private void die(int[] yourCordInMaze) {
        Toast t = Toast.makeText(getApplicationContext(), "You were killed", Toast.LENGTH_SHORT);
        t.show();
        if (bolnitsaDescovered) {
            moveTOtheBolnitsa();
            hospitalPrep();
            CurBasicCord[0] = BolLaycor[0];
            CurBasicCord[1] = BolLaycor[1];
            changeTagIdCell(Maze.YourCordInMaze, R.drawable.milkiipidoras);
            changeIdCell(Maze.YourCordInMaze, R.drawable.milkiipidoras, R.id.Me);
        } else {
            BolLaycor[0] = Maze.hospital.get(HOSPITAL)[0];
            BolLaycor[1] = Maze.hospital.get(HOSPITAL)[1];
            moveTOnewlayout();
            bolnitsaDescovered = true;
            bolLayout = ThisLayout;
            hospitalPrep();
            CurBasicCord[0] = Maze.YourCordInMaze[0];
            CurBasicCord[1] = Maze.YourCordInMaze[1];
            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.hospital);
        }
    }

    private void hospitalPrep() {
        Maze.YourCordInMaze[0] = Maze.hospital.get(HOSPITAL)[0];
        Maze.YourCordInMaze[1] = Maze.hospital.get(HOSPITAL)[1];
//        Maze.YourMazesholder.get(NativeLayout)[CurBasicCord[0]][CurBasicCord[1]] = 0;
    }

    private void moveTOtheBolnitsa() {
        ImageView iMv = (ImageView) findViewById(R.id.Me);
        delView(iMv);
        ImageView iMvc = (ImageView) findViewById(R.id.Mec);
        delTagView(iMvc);
        setLayoutAScurrent(bolLayout);
        CurrentLayout = bolLayout;
        NativeLayout = CurrentLayout;
        goToThisLayout(bolLayout);
        ThisLayout = NativeLayout;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void goFORriver(int[] cord, int side){
        switch (side){
            case 1: {
                Maze.YourCordInMaze[0] = cord[0] - 2;
                checkTheRvrORtpFORriver();
                break;
            }
            case 10: {
                Maze.YourCordInMaze[1] = cord[1] + 2;
                checkTheRvrORtpFORriver();
                break;
            }
            case 100: {
                Maze.YourCordInMaze[0] = cord[0] + 2;
                checkTheRvrORtpFORriver();
                break;
            }
            case 1000: {
                Maze.YourCordInMaze[1] = cord[1] - 2;
                checkTheRvrORtpFORriver();
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void finishgame() {

        if (!exitDescovered) {
            exitLayout = ThisLayout;
            exitLaycor[0] = CurBasicCord[0];
            exitLaycor[1] = CurBasicCord[1];
            exitDescovered = true;
        } else {
            if (!NativeLayout.equals(exitLayout)) {
                mergeLayouts(exitLayout, CurrentLayout);
                exitLayout = NativeLayout;
                exitDescovered = true;
            }
        }


        if (Maze.isKey) {
            if (isAnYkey) {
                Toast t = Toast.makeText(getApplicationContext(), "You won", Toast.LENGTH_SHORT);
                t.show();
            } else {
                Toast t = Toast.makeText(getApplicationContext(), "You need key", Toast.LENGTH_SHORT);
                t.show();
            }
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "You won", Toast.LENGTH_SHORT);
            t.show();
        }
    }
    private void noact(int[] cord, int side){

        switch (side){
            case 1:{
                Maze.YourMazesholder.get(NativeLayout)[cord[0] - 1][cord[1]] = 1;
                changeCell(new int[]{cord[0], cord[1]}, R.drawable.wall1uppr);
                break;
            }
            case 10:{
                Maze.YourMazesholder.get(NativeLayout)[cord[0]][cord[1] + 1] = 1;
                changeCell(new int[]{cord[0], cord[1]},R.drawable.wall1rightpr);
                break;
            }
            case 100:{
                Maze.YourMazesholder.get(NativeLayout)[cord[0] + 1][cord[1]] = 1;
                changeCell(new int[]{cord[0], cord[1]}, R.drawable.wall1downpr);
                break;
            }
            case 1000:{
                Maze.YourMazesholder.get(NativeLayout)[cord[0]][cord[1] - 1] = 1;
                changeCell(new int[]{cord[0], cord[1]}, R.drawable.wall1leftpr);
                break;
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkTheRvrORtp(){
        if ((Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] < TELEPORT) && (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] > 10)) {
            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.river);
            river(Maze.YourCordInMaze);
        } else if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] > TELEPORT) {
            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.teleport1);
            teleport(Maze.YourCordInMaze);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkTheRvrORtpFORriver(){
        if ((Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] < TELEPORT) && (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] > 10)) {
            river(Maze.YourCordInMaze);
        } else if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] > TELEPORT) {
            teleport(Maze.YourCordInMaze);
        }
    }

    private void delTagView(View view){
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.p0l).findViewWithTag("p"+NativeLayout.toString()+"l");
        layout.removeView(view);
    }

    private void delView(View view){
        ConstraintLayout l = (ConstraintLayout) findViewById(R.id.Container);
        RelativeLayout layout = (RelativeLayout) l.findViewWithTag("p"+NativeLayout.toString()+"ln");
        layout.removeView(view);
    }

    private void changeTagIdCell(int[] cord, int cellType) {
        int Cellsize = (int) ConvDPtoPX(41);
        RelativeLayout subLayoutInGlobal = (RelativeLayout) findViewById(R.id.p0l).findViewWithTag("p"+NativeLayout.toString()+"l");
        ImageView imageViewc = new ImageView(this);
        imageViewc.setId(R.id.Mec);
        imageViewc.setImageResource(cellType);
        RelativeLayout.LayoutParams rulesc = new RelativeLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        ImageView v = subLayoutInGlobal.findViewWithTag("C"+NativeLayout);
        int x = (int) v.getX();
        int y = (int) v.getY();
        if (x==0 && y==0) {
            x = (Maze.SIZE_X - 1) * Cellsize;
            y = (Maze.SIZE_Y - 1) * Cellsize;
        }
        rulesc.setMargins(
                (int) (x - (CurBasicCord[1] - cord[1]) * Cellsize/2),
                (int) (y - (CurBasicCord[0] - cord[0]) * Cellsize/2),
                0, 100);
        subLayoutInGlobal.addView(imageViewc, rulesc);
    }

    private void changeIdCell(int[] cord, int cellType, int ident) {
        int Cellsize = (int) ConvDPtoPX(41);
        ConstraintLayout l = (ConstraintLayout) findViewById(R.id.Container);
        RelativeLayout layout = (RelativeLayout) l.findViewWithTag("p"+NativeLayout.toString()+"ln");
        ImageView imageView = new ImageView(this);
        imageView.setId(ident);
        imageView.setImageResource(cellType);
        RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        rules.setMargins((int) zerocor[1] - (CurBasicCord[1] - cord[1]) * Cellsize/2, (int) zerocor[0] - (CurBasicCord[0] - cord[0]) * Cellsize/2, 0, 0);
        layout.addView(imageView, rules);
    }
    private void changeCell(int[] cord, int cellType) {
        int Cellsize = (int) ConvDPtoPX(41);
        ConstraintLayout l = (ConstraintLayout) findViewById(R.id.Container);
        RelativeLayout layout = (RelativeLayout) l.findViewWithTag("p"+NativeLayout.toString()+"ln");
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(cellType);
        RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        rules.setMargins((int) zerocor[1] - (CurBasicCord[1] - cord[1]) * Cellsize/2, (int) zerocor[0] - (CurBasicCord[0] - cord[0]) * Cellsize/2, 1000, 1000);
        layout.addView(imageView, rules);
        ImageView iv = (ImageView) findViewById(R.id.Me);
        delView(iv);
        changeIdCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.milkiipidoras, R.id.Me);

        RelativeLayout subLayoutInGlobal = (RelativeLayout) findViewById(R.id.p0l).findViewWithTag("p"+NativeLayout.toString()+"l");
        ImageView imageViewc = new ImageView(this);
        imageViewc.setImageResource(cellType);
        RelativeLayout.LayoutParams rulesc = new RelativeLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        ImageView v = subLayoutInGlobal.findViewWithTag("C"+NativeLayout);
        int x = (int) v.getX();
        int y = (int) v.getY();
        if (x==0 && y==0) {
            x = (Maze.SIZE_X - 1) * Cellsize;
            y = (Maze.SIZE_Y - 1) * Cellsize;
        }
        rulesc.setMargins(
                (int) (x - (CurBasicCord[1] - cord[1]) * Cellsize/2),
                (int) (y - (CurBasicCord[0] - cord[0]) * Cellsize/2),
                100, 100);
        subLayoutInGlobal.addView(imageViewc, rulesc);
        ImageView ivc = (ImageView) findViewById(R.id.Mec);
        delTagView(ivc);
        changeTagIdCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.milkiipidoras);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void turn(int side) {
        switch (side) {
            case TURN_NA: {
                if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] >= TELEPORT) {
                    teleport(Maze.YourCordInMaze);
                }
                break;
            }
            case TURN_UP: {
                if (Maze.Maze[Maze.YourCordInMaze[0] - 1][Maze.YourCordInMaze[1]] == 0) {
                    go(Maze.YourCordInMaze, (int) Math.pow(10, side - 1));
                } else {
                    if (Maze.Maze[Maze.YourCordInMaze[0] - 1][Maze.YourCordInMaze[1]] == 2) {
                        changeCell(new int[] {Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.exitup);
                        Maze.YourMazesholder.get(NativeLayout)[Maze.YourCordInMaze[0] - 1][Maze.YourCordInMaze[1]] = 2;
                        finishgame();
                    } else {
                        noact(Maze.YourCordInMaze, (int) Math.pow(10, side - 1));
                    }
                }
                break;
            }
            case TURN_RIGHT: {
                if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1] + 1] == 0) {
                    go(Maze.YourCordInMaze, (int) Math.pow(10, side - 1));
                } else {
                    if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1] + 1] == 2) {
                        changeCell(new int[] {Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.exitright);
                        Maze.YourMazesholder.get(NativeLayout)[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1] + 1] = 2;
                        finishgame();
                    } else {
                        noact(Maze.YourCordInMaze, (int) Math.pow(10, side - 1));
                    }
                }
                break;
            }
            case TURN_DOWN: {
                if (Maze.Maze[Maze.YourCordInMaze[0] + 1][Maze.YourCordInMaze[1]] == 0) {
                    go(Maze.YourCordInMaze, (int) Math.pow(10, side - 1));
                } else {
                    if (Maze.Maze[Maze.YourCordInMaze[0] + 1][Maze.YourCordInMaze[1]] == 2) {
                        changeCell(new int[] {Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.exitdown);
                        Maze.YourMazesholder.get(NativeLayout)[Maze.YourCordInMaze[0] + 1][Maze.YourCordInMaze[1]] = 2;
                        finishgame();
                    } else {
                        noact(Maze.YourCordInMaze, (int) Math.pow(10, side - 1));
                    }
                }
                break;
            }
            case TURN_LEFT: {
                if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1] - 1] == 0) {
                    go(Maze.YourCordInMaze, (int) Math.pow(10, side - 1));
                } else {
                    if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1] - 1] == 2) {
                        changeCell(new int[] {Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.exitleft);
                        Maze.YourMazesholder.get(NativeLayout)[Maze.YourCordInMaze[0] + 1][Maze.YourCordInMaze[1]] = 2;
                        finishgame();
                    } else {
                        noact(Maze.YourCordInMaze, (int) Math.pow(10, side - 1));
                    }
                }
                break;
            }
        }
    }
}
