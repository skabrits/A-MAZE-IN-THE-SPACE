package com.example.sevak.themaze;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sevak on 21.03.2018.
 */

public class MazeHolder {
    public static class MazeExample {
        public static int[][] Maze;
        public static int[] BasicCordinats;
    }
    // -1=месторасположение; 2n=клетка; 2n+1=стенка: 1=стенка, 2=выход; 0=пустая клетка; 1=пулька: 2=минотавр; 11/12/13/14=река вверх по часовой стрелке; 10x/20x... телепорт: первая цифра номер серии телепорта, вторая номер телепорта в серии;
    public static int[][] Maze = {
            {1,   1,   1,   1,   1,   2,   1},
            {1,  -1,   0,   0,   1,   0,   1},
            {1,   1,   1,   0,   1,   0,   1},
            {1, 102,   1, 101,   1,   0,   1},
            {1,   0,   1,   1,   1,   0,   1},
            {1,  12,   0,   0,   0,   0,   1},
            {1,   1,   1,   1,   1,   1,   1}
    };
    public static int[] BasicCordinats = {1,1};
    // для стенок 1=up, 10=right, 100=down, 1000=left
    public static void init(){
        MazeExample FirstMaze = new MazeExample();
        FirstMaze.Maze = Maze;
        FirstMaze.BasicCordinats = BasicCordinats;
        MazeArr.add(FirstMaze);
    }
    public static ArrayList<MazeExample> MazeArr;
}
