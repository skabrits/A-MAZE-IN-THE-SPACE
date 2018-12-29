package com.example.sevak.themaze;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sevak on 21.03.2018.
 */

public class LevelHolderCampaign {

    static SharedPreferences sharedPreferencesForMholder;
    static int Mylvl = 0;

    // -1=месторасположение; 2n=клетка; 2n+1=стенка: 1=стенка, 2=выход; 0=пустая клетка; 2=ключ; 3=bfg; 4=пулька: 5=минотавр; 6=больница; 7=мертвый минотавр; 8=собранная пулька; 11/12/13/14=река вверх по часовой стрелке; 10x/20x... телепорт: первая цифра номер серии телепорта, вторая номер телепорта в серии;
    public static int[][] Maze;
    public static int[][] Maze1;
    public static ArrayList<Level> LevelArr = new ArrayList<Level>();
    public static int[] BasicCordinats;
    public static int[] BasicCordinats1;
    // для стенок 1=up, 10=right, 100=down, 1000=left
    public static void init(Context context) {
        Maze = new int[][]{
                {1, 1, 1, 1, 1, 2, 1},
                {1, -1, 0, 0, 1, 5, 1},
                {1, 1, 1, 0, 1, 0, 1},
                {1, 102, 1, 101, 1, 4, 1},
                {1, 0, 1, 1, 1, 0, 1},
                {1, 12, 0, 0, 0, 6, 1},
                {1, 1, 1, 1, 1, 1, 1}
        };
        Maze1 = new int[][]{
                {1, 1, 1, 1, 1, 1, 1},
                {1, 102, 1, 5, 0, 2, 1},
                {1, 0, 1, 0, 1, 1, 1},
                {2, 0, 1, 101, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1},
                {1, 4, 1, 6, 0, 4, 1},
                {1, 1, 1, 1, 1, 1, 1}
        };

        Gson gson = new Gson();
        String t1 = "{\"BasicCordinats\":[1,1],\"Maze\":[[0,1,0,1,0,2,0],[1,0,0,0,1,0,1],[0,1,0,0,0,0,0],[1,0,0,0,1,0,1],[0,0,0,1,0,0,0],[1,0,0,0,0,0,1],[0,1,0,1,0,1,0]],\"name\":\"First steps\"}";
        String t2 = "{\"BasicCordinats\":[1,1],\"Maze\":[[0,1,0,1,0,1,0,1,0,1,0],[1,0,0,101,1,102,0,0,0,0,2],[0,1,0,1,0,1,0,1,0,1,0]],\"name\":\"Teleport rush\"}";
        String t3 = "{\"BasicCordinats\":[1,3],\"Maze\":[[0,1,0,1,0,1,0],[1,0,0,0,0,0,1],[0,0,0,1,0,1,0],[1,12,0,13,0,101,1],[0,0,0,0,0,0,0],[1,102,0,12,0,0,1],[0,2,0,1,0,1,0]],\"name\":\"A river\"}";
        String t4 = "{\"BasicCordinats\":[1,1],\"Maze\":[[0,1,0,1,0,1,0,1,0],[1,0,0,6,0,4,0,2,1],[0,1,0,1,0,0,0,1,0],[2,5,0,4,0,4,0,3,1],[0,1,0,1,0,1,0,1,0]],\"name\":\"Shoot him down\"}";
        BasicCordinats = new int[]{1, 1};
        MazeExample FirstMaze = new MazeExample();
        FirstMaze.Maze = Maze;
        FirstMaze.BasicCordinats = BasicCordinats;
        FirstMaze.name = "The begining";
        Level Thebeg = new Level(gson.toJson(FirstMaze), "lvl", FirstMaze.name);
        BasicCordinats1 = new int[]{3, 5};
        MazeExample FirstMaze1 = new MazeExample();
        FirstMaze1.Maze = Maze1;
        FirstMaze1.BasicCordinats = BasicCordinats1;
        FirstMaze1.name = "Second Maze";
        Level Secmaz = new Level(gson.toJson(FirstMaze1), "lvl", FirstMaze1.name);
        Level lt1 = new Level(t1, "lvl", gson.fromJson(t1, MazeExample.class).name);
        Level lt2 = new Level(t2, "lvl", gson.fromJson(t2, MazeExample.class).name);
        Level lt3 = new Level(t3, "lvl", gson.fromJson(t3, MazeExample.class).name);
        Level lt4 = new Level(t4, "lvl", gson.fromJson(t4, MazeExample.class).name);
        Level ctswalk = new Level("android.resource://" + context.getPackageName() + "/"
                + R.raw.walking_tut, "csc", "How to walk");
        Level ctsshot = new Level("android.resource://" + context.getPackageName() + "/"
                + R.raw.shoot_tut, "csc", "How to shoot somebody");
        Level ctsgm = new Level("android.resource://" + context.getPackageName() + "/"
                + R.raw.drag_tut, "csc", "How to deal with the global map");

        sharedPreferencesForMholder = context.getSharedPreferences("Mylvl", MODE_PRIVATE);
        Integer a = gson.fromJson(sharedPreferencesForMholder.getString("Mylvl", null), int.class);
        if (a == null) {
            Mylvl = -1;
        } else {
            Mylvl = a;
        }
        if (Mylvl == -1) {
            Mylvl = 0;
            sharedPreferencesForMholder = context.getSharedPreferences("Mylvl", MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedPreferencesForMholder.edit();
            ed.putString("Mylvl", gson.toJson(Mylvl));
            ed.apply();
        }

        sharedPreferencesForMholder = context.getSharedPreferences("mazeholdC", MODE_PRIVATE);
        sharedPreferencesForMholder.edit().remove("mazeholdC").apply();
        Type type = new TypeToken<ArrayList<Level>>() {}.getType();
        LevelArr = gson.fromJson(sharedPreferencesForMholder.getString("mazeholdC", null), type);

        if (LevelArr == null) {
            LevelArr = new ArrayList<Level>();
        }
        if (LevelArr.size() == 0) {
                LevelArr = new ArrayList<Level>();
                LevelArr.add(ctswalk);
                LevelArr.add(ctsgm);
                LevelArr.add(lt1);
                LevelArr.add(lt2);
                LevelArr.add(lt3);
                LevelArr.add(ctsshot);
                LevelArr.add(lt4);
                LevelArr.add(Thebeg);
                LevelArr.add(Secmaz);

                sharedPreferencesForMholder = context.getSharedPreferences("mazeholdC", MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferencesForMholder.edit();
                ed.putString("mazeholdC", gson.toJson(LevelArr));
                ed.apply();
        }
    }
}
