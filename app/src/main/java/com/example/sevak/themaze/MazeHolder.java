package com.example.sevak.themaze;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sevak on 21.03.2018.
 */

public class MazeHolder {

    static SharedPreferences sharedPreferencesForMholder;

    // -1=месторасположение; 2n=клетка; 2n+1=стенка: 1=стенка, 2=выход; 0=пустая клетка; 2=ключ; 3=bfg; 4=пулька: 5=минотавр; 6=больница; 7=мертвый минотавр; 8=собранная пулька; 11/12/13/14=река вверх по часовой стрелке; 10x/20x... телепорт: первая цифра номер серии телепорта, вторая номер телепорта в серии;
    public static int[][] Maze;
    public static int[][] Maze1;
    public static ArrayList<MazeExample> MazeArr = new ArrayList<MazeExample>();
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
        BasicCordinats = new int[]{1, 1};
        MazeExample FirstMaze = new MazeExample();
        FirstMaze.Maze = Maze;
        FirstMaze.BasicCordinats = BasicCordinats;
        FirstMaze.name = "Begining";
        BasicCordinats1 = new int[]{3, 5};
        MazeExample FirstMaze1 = new MazeExample();
        FirstMaze1.Maze = Maze1;
        FirstMaze1.BasicCordinats = BasicCordinats1;
        FirstMaze1.name = "Second Maze";

        Gson gson = new Gson();

        sharedPreferencesForMholder = context.getSharedPreferences("mazehold", MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<MazeExample>>() {}.getType();
        MazeArr = gson.fromJson(sharedPreferencesForMholder.getString("mazehold", null), type);

        if (MazeArr == null) {
            MazeArr = new ArrayList<MazeExample>();
            MazeArr.add(FirstMaze);
            MazeArr.add(FirstMaze1);

            sharedPreferencesForMholder = context.getSharedPreferences("mazehold", MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedPreferencesForMholder.edit();
            ed.putString("mazehold", gson.toJson(MazeArr));
            ed.apply();
        }
    }
}
