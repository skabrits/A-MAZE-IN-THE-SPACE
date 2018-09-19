package com.example.sevak.themaze;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sevak on 21.03.2018.
 */

public class MazeHolder {
    // -1=месторасположение; 2n=клетка; 2n+1=стенка: 1=стенка, 2=выход; 0=пустая клетка; 2=ключ; 3=bfg; 4=пулька: 5=минотавр; 6=больница; 7=мертвый минотавр; 8=собранная пулька; 11/12/13/14=река вверх по часовой стрелке; 10x/20x... телепорт: первая цифра номер серии телепорта, вторая номер телепорта в серии;
    public static int[][] Maze;
    public static int[][] Maze1;
    public static ArrayList<MazeExample> MazeArr = new ArrayList<MazeExample>();
    public static int[] BasicCordinats;
    public static int[] BasicCordinats1;
    // для стенок 1=up, 10=right, 100=down, 1000=left
    public static void init() {
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
        if (MazeArr.size() == 0) {
            MazeArr.add(FirstMaze);
            MazeArr.add(FirstMaze1);

        }
    }
}
