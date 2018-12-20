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

public class MazeHolderCampaign {

    static SharedPreferences sharedPreferencesForMholder;

    // -1=месторасположение; 2n=клетка; 2n+1=стенка: 1=стенка, 2=выход; 0=пустая клетка; 2=ключ; 3=bfg; 4=пулька: 5=минотавр; 6=больница; 7=мертвый минотавр; 8=собранная пулька; 11/12/13/14=река вверх по часовой стрелке; 10x/20x... телепорт: первая цифра номер серии телепорта, вторая номер телепорта в серии;
    public static int[][] Maze;
    public static int[][] Maze1;
    public static ArrayList<Object> MazeArr = new ArrayList<Object>();
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
        MazeExample t1 = gson.fromJson("{\"BasicCordinats\":[1,1],\"Maze\":[[0,1,0,1,0,2,0],[1,0,0,0,1,0,1],[0,1,0,0,0,0,0],[1,0,0,0,1,0,1],[0,0,0,1,0,0,0],[1,0,0,0,0,0,1],[0,1,0,1,0,1,0]],\"name\":\"First steps\"}", MazeExample.class);
        MazeExample t2 = gson.fromJson("{\"BasicCordinats\":[1,1],\"Maze\":[[0,1,0,1,0,1,0,1,0,1,0],[1,0,0,101,1,102,0,0,0,0,2],[0,1,0,1,0,1,0,1,0,1,0]],\"name\":\"Teleport rush\"}", MazeExample.class);
        MazeExample t3 = gson.fromJson("{\"BasicCordinats\":[1,3],\"Maze\":[[0,1,0,1,0,1,0],[1,0,0,0,0,0,1],[0,0,0,1,0,1,0],[1,12,0,13,0,101,1],[0,0,0,0,0,0,0],[1,102,0,12,0,0,1],[0,2,0,1,0,1,0]],\"name\":\"A river\"}", MazeExample.class);
        MazeExample t4 = gson.fromJson("{\"BasicCordinats\":[1,1],\"Maze\":[[0,1,0,1,0,1,0,1,0],[1,0,0,6,0,4,0,2,1],[0,1,0,1,0,0,0,1,0],[2,5,0,4,0,4,0,3,1],[0,1,0,1,0,1,0,1,0]],\"name\":\"Shoot him down\"}", MazeExample.class);
        BasicCordinats = new int[]{1, 1};
        MazeExample FirstMaze = new MazeExample();
        FirstMaze.Maze = Maze;
        FirstMaze.BasicCordinats = BasicCordinats;
        FirstMaze.name = "The begining";
        BasicCordinats1 = new int[]{3, 5};
        MazeExample FirstMaze1 = new MazeExample();
        FirstMaze1.Maze = Maze1;
        FirstMaze1.BasicCordinats = BasicCordinats1;
        FirstMaze1.name = "Second Maze";

        sharedPreferencesForMholder = context.getSharedPreferences("mazeholdC", MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<Object>>() {}.getType();
        MazeArr = gson.fromJson(sharedPreferencesForMholder.getString("mazeholdC", null), type);
        if (MazeArr == null) {
            MazeArr = new ArrayList<Object>();
        }
        if (MazeArr.size() == 0) {
                MazeArr = new ArrayList<Object>();
                MazeArr.add(t1);
                MazeArr.add(t2);
                MazeArr.add(t3);
                MazeArr.add(t4);
                MazeArr.add(FirstMaze);
                MazeArr.add(FirstMaze1);

                sharedPreferencesForMholder = context.getSharedPreferences("mazeholdC", MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferencesForMholder.edit();
                ed.putString("mazeholdC", gson.toJson(MazeArr));
                ed.apply();
        }
    }
}
