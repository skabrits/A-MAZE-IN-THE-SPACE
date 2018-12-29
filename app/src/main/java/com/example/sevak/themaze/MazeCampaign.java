package com.example.sevak.themaze;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.example.sevak.themaze.StartPage.BFG;
import static com.example.sevak.themaze.StartPage.HOSPITAL;
import static com.example.sevak.themaze.StartPage.KEY;
import static com.example.sevak.themaze.StartPage.TELEPORT;

/**
 * Created by sevak on 21.03.2018.
 */

public class MazeCampaign {
    public static int[][] Maze;
    public static int SIZE_X;
    public static int SIZE_Y;

    public static int[] YourCordInMaze = new int[2];

    public static int rand;
    public static int[][] YourMaze;
    public static Map<Integer,int[]> Teleports = new HashMap<Integer, int[]>();
    public static Map<Integer,int[][]> YourMazesholder = new HashMap<Integer, int[][]>();
    public static Map<Integer, int[]> hospital = new HashMap<Integer, int[]>();
    public static Boolean isWeapon = false;
    public static Boolean isKey = false;

    public static void  init() {
        Gson gson = new Gson();
        MazeExample myMaze = gson.fromJson(LevelHolderCampaign.LevelArr.get(rand).data, MazeExample.class);
        Maze = new int[myMaze.Maze.length][myMaze.Maze[1].length];
        for (int i = 0; i < myMaze.Maze.length; i++) {
            for (int j = 0; j < myMaze.Maze[1].length; j++) {
                Maze[i][j] = myMaze.Maze[i][j];
            }
        }
        SIZE_X = (Maze[0].length - 1)/2;
        SIZE_Y = (Maze.length - 1)/2;
        YourMaze = new int[Maze.length][Maze[0].length];
        YourMazesholder.clear();
        YourCordInMaze[0] = myMaze.BasicCordinats[0];
        YourCordInMaze[1] = myMaze.BasicCordinats[1];
        for (int i = 0; i < Maze.length; i++) {
            for (int j = 0; j < Maze[1].length; j++) {
                YourMaze[i][j] = -1;
            }
        }
        YourMaze[myMaze.BasicCordinats[0]][myMaze.BasicCordinats[1]] = 0;
        YourMazesholder.put(1, YourMaze);
        Teleports.clear();
        for (int i = 0; i < Maze.length; i++) {
            for (int j = 0; j < Maze[i].length; j++) {
                if (Maze[i][j] >= TELEPORT) {
                    Teleports.put(Maze[i][j], new int[]{i, j});
                }
            }
        }
        hospital.clear();
        for (int i = 0; i < Maze.length; i++) {
            for (int j = 0; j < Maze[i].length; j++) {
                if (Maze[i][j] == HOSPITAL) {
                    hospital.put(Maze[i][j], new int[]{i, j});
                }
            }
        }
        isWeapon = false;
        for (int i = 0; i < Maze.length; i++) {
            for (int j = 0; j < Maze[i].length; j++) {
                if (Maze[i][j] == BFG) {
                    isWeapon = true;
                }
            }
        }
        isKey = false;
        for (int i = 0; i < (Maze.length - 1) / 2; i++) {
            for (int j = 0; j < (Maze[i].length - 1) / 2; j++) {
                if (Maze[i * 2 + 1][j * 2 + 1] == KEY) {
                    isKey = true;
                }
            }
        }
    }
}
