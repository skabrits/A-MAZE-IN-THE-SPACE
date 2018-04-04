package com.example.sevak.themaze;

import java.util.HashMap;
import java.util.Map;

import static com.example.sevak.themaze.StartPage.TELEPORT;

/**
 * Created by sevak on 21.03.2018.
 */

public class Maze {
    public static int[][] Maze = MazeHolder.Maze;
    public static int[] YourCordInMaze = new int[2];

    public static int[][] YourMaze = new int[Maze.length][Maze[1].length];
    public static Map<Integer,int[]> Teleports = new HashMap<Integer, int[]>();
    public static Map<Integer,int[][]> YourMazesholder = new HashMap<Integer, int[][]>();
    public static void  init() {
        YourMazesholder.clear();
        YourCordInMaze[0] = MazeHolder.BasicCordinats[0];
        YourCordInMaze[1] = MazeHolder.BasicCordinats[1];
        for (int i = 0; i < Maze.length; i++) {
            for (int j = 0; j < Maze[1].length; j++) {
                YourMaze[i][j] = -1;
            }
        }
        YourMaze[MazeHolder.BasicCordinats[0]][MazeHolder.BasicCordinats[1]] = 0;
        YourMazesholder.put(1, YourMaze);
        Teleports.clear();
        for (int i = 0; i < Maze.length; i++) {
            for (int j = 0; j < Maze[i].length; j++) {
                if (Maze[i][j] >= TELEPORT) {
                    Teleports.put(Maze[i][j], new int[]{i, j});
                }
            }
        }
    }
}
