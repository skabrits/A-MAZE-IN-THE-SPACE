package com.example.sevak.themaze;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.example.sevak.themaze.MazeHolder.sharedPreferencesForMholder;

public class Client extends AppCompatActivity {

    private Set<List<Integer>> killstreat = new HashSet<List<Integer>>();

    private int chmap = -1;
    private int Mazenom = -1;

    static Boolean isSent = false;
    static Boolean isMyTurn = false;
    static Boolean isDead = false;

    static Boolean isMazegot = false;
    static Boolean isGotServInfo = false;
    static Boolean isServBase = false;
    static Boolean isSetupFineshed = false;
    static Boolean Predban = false;
    static Boolean Rmazeneed = false;


    static Boolean isMultiplayer = false;

    Socket s;  // это будет сокет для сервера
    BufferedReader socketReader; // буферизированный читатель с сервера
    BufferedWriter socketWriter; // буферизированный писатель на сервер
    BufferedReader userInput; // буферизированный читатель пользовательского ввода с консоли
    private Boolean isFirstConnect = true;
    private Boolean LoginUsed = false;
    static Boolean ServnotgotMazenom = false;

    /**
     * Конструктор объекта клиента
     * @param host - IP адрес или localhost или доменное имя
     * @param port - порт, на котором висит сервер
     * @throws IOException - если не смогли приконнектиться, кидается исключение, чтобы
     * предотвратить создание объекта
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void runClient(String host, int port) throws IOException {
        s = new Socket(host, port); // создаем сокет
        // создаем читателя и писателя в сокет с дефолной кодировкой UTF-8
        isMultiplayer = true;
        socketReader = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
        socketWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"));
        // создаем читателя с консоли (от пользователя)
        userInput = new BufferedReader(new InputStreamReader(System.in));
        new Thread(new Receiver()).start();// создаем и запускаем нить асинхронного чтения из сокета
        run();
    }

    /**
     * метод, где происходит главный цикл чтения сообщений с консоли и отправки на сервер
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void run() {
//        if (isFirstConnect) {
//            System.out.println("Write your name");
//            String username = null;
//            while (true) {
//                try {
//                    username = userInput.readLine();
//                } catch (IOException e) {
//                }
//                if (username != null) {
//                    break;
//                }
//            }
//            try {
//                socketWriter.write("\\\\username: " + username);
//                socketWriter.write("\n");
//                socketWriter.flush();
//            } catch (IOException e) {
//            }
//        }
//        isFirstConnect = false;
//        while (true) {
//            String userString = null;
//            try {
//                userString = userInput.readLine(); // читаем строку от пользователя
//            } catch (IOException ignored) {} // с консоли эксепшена не может быть в принципе, игнорируем
//            //если что-то не так или пользователь просто нажал Enter...
//            if (userString == null || userString.length() == 0 || s.isClosed()) {
//                close(); // ...закрываем коннект.
//                break; // до этого break мы не дойдем, но стоит он, чтобы компилятор не ругался
//            } else if (isFirstConnect) {
//                if (LoginUsed) {
//                    try {
//                        socketWriter.write("\\\\username: " + userString);
//                        socketWriter.write("\n");
//                        socketWriter.flush();
//                    } catch (IOException e) {}
//                    LoginUsed = false;
//                }
//            } else { //...иначе...
//                try {
//                    socketWriter.write(userString); //пишем строку пользователя
//                    socketWriter.write("\n"); //добавляем "новою строку", дабы readLine() сервера сработал
//                    socketWriter.flush(); // отправляем
//                } catch (IOException e) {
//                    close(); // в любой ошибке - закрываем.
//                }
//            }
//        }
        while (true) {
            if (isFirstConnect && isServBase && ServnotgotMazenom){
                try {
                    Gson gson = new Gson();
                    Thread.sleep(3000);
                    socketWriter.write("\\\\maze_nom: " + gson.toJson((MazeExample) (MazeHolder.MazeArr.get(Maze.rand)))); //пишем строку пользователя
                    socketWriter.write("\n"); //добавляем "новою строку", дабы readLine() сервера сработал
                    socketWriter.flush(); // отправляем
                } catch (IOException e) {
                    close(); // в любой ошибке - закрываем.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (Rmazeneed) {
                Rmazeneed = false;
                try {
                    socketWriter.write("\\\\rmaze_nom:"); //пишем строку пользователя
                    socketWriter.write("\n"); //добавляем "новою строку", дабы readLine() сервера сработал
                    socketWriter.flush(); // отправляем
                } catch (IOException e) {
                    close(); // в любой ошибке - закрываем.
                }
            }

            if (!isMyTurn && !isSent && !isFirstConnect) {
                isSent = true;
                Gson gson = new Gson();
                try {
                    socketWriter.write("\\\\next_turn://////" + gson.toJson(Maze.Maze) + "//////" + gson.toJson(Arrays.asList( Arrays.stream( Maze.YourCordInMaze ).boxed().toArray( Integer[]::new ))) + "//////" + gson.toJson(killstreat)); //пишем строку пользователя
                    socketWriter.write("\n"); //добавляем "новою строку", дабы readLine() сервера сработал
                    socketWriter.flush(); // отправляем
                    killstreat.clear();
                } catch (IOException e) {
                    close(); // в любой ошибке - закрываем.
                }
            }
        }
    }

    /**
     * метод закрывает коннект и выходит из
     * программы (это единственный  выход прервать работу BufferedReader.readLine(), на ожидании пользователя)
     */
    public synchronized void close() {//метод синхронизирован, чтобы исключить двойное закрытие.
        if (!s.isClosed()) { // проверяем, что сокет не закрыт...
            try {
                s.close(); // закрываем...
                System.exit(0); // выходим!
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
    }

//    public static void main(String[] args)  { // входная точка программы
//        try {
//            new Client("192.168.0.48", 8080).run(); // Пробуем приконнетиться...
//        } catch (IOException e) { // если объект не создан...
//            System.out.println("Unable to connect. Server not running?"); // сообщаем...
//        }
//    }

    /**
     * Вложенный приватный класс асинхронного чтения
     */
    private class Receiver implements Runnable{
        /**
         * run() вызовется после запуска нити из конструктора клиента чата.
         */
        public void run() {
            while (!s.isClosed()) { //сходу проверяем коннект.
                String line = null;
                try {
                    line = socketReader.readLine(); // пробуем прочесть
                } catch (IOException e) { // если в момент чтения ошибка, то...
                    // проверим, что это не банальное штатное закрытие сокета сервером
                    if ("Socket closed".equals(e.getMessage())) {
                        break;
                    }
                    System.out.println("Connection lost"); // а сюда мы попадем в случае ошибок сети.
                    close(); // ну и закрываем сокет (кстати, вызвается метод класса ChatClient, есть доступ)
                }
                if (line == null) {  // строка будет null если сервер прикрыл коннект по своей инициативе, сеть работает
                    System.out.println("Server has closed connection");
                    close(); // ...закрываемся
//                } else if (isFirstConnect) {
//                    while (true) {
//                        if (line.equals("\\\\nope")) {
//                            LoginUsed = true;
//                            System.out.println("Write another name, this has been already used");
//                        } else if (line.equals("\\\\okay")) {
//                            System.out.println("Type phrase(s) (hit Enter to exit):");
//                            isFirstConnect = false;
//                            break;
//                        }
//                        try {
//                            line = socketReader.readLine();
//                        } catch (IOException e) {}
//                    }
                } else if (line.split(" ")[0].equals("\\\\your_turn:")) {
                    isMyTurn = true;
                    isSent = false;
                    Gson gson = new Gson();
                    Type type = new TypeToken<int[][]>() {}.getType();
                    Maze.Maze = gson.fromJson(line.split(" ")[1], type);
                    if (line.split(" ")[2].equals("dead")){
                        isDead = true;
                    }
                } else if (line.split(" ")[0].equals("\\\\maze_nom:")) {
                    isMazegot = true;
                    Gson gson = new Gson();
                    MazeHolder.MazeArr.add(gson.fromJson(line.substring(line.split(" ")[0].length() + 1), MazeExample.class));
                    Mazenom = MazeHolder.MazeArr.size() - 1;
                } else if (line.split(" ")[0].equals("\\\\amaze_nom:")) {
                    if (line.split(" ")[1].equals("okay")) {
                        ServnotgotMazenom = false;
                    }
                } else { // иначе печатаем то, что прислал сервер.
                    System.out.println(line);
                }
            }
        }
    }





    private GestureDetector mDetector;
    private float ConvDPtoPX(float dp){
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        return dp*((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

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
    private Integer layoutAmount = 1;
    private Integer CurrentLayout = 1;
    private Integer NativeLayout = 1;
    private Integer ThisLayout = 0;
    private Integer bulletAmount = 0;
    private Boolean isAnYweapon = false;
    private Boolean isAnYkey = false;
    //TODO: merge current location and global map pages
    private int[] zerocor = new int[2];
    private int[] Laycor = new int[2];
    private int[] CurBasicCord  = new int[2];
    public static int vX, vY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_shlus);

        mDetector = new GestureDetector(this, new MyGestureListenerForMultiplayer());

        findViewById(R.id.Create_Server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread threadS = new Thread(null, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new Server(8080).run();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                threadS.start();

                Thread threadC = new Thread(null, new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            runClient(InetAddress.getLocalHost().getHostAddress(), 8080); // Пробуем приконнетиться...
                        } catch (IOException e) { // если объект не создан...
                            System.out.println("Unable to connect. Server not running?"); // сообщаем...
//                            Toast t = Toast.makeText(getApplicationContext(), "Unable to connect. Server not running?", Toast.LENGTH_SHORT);
//                            t.show();
                        } catch (NetworkOnMainThreadException e){
                            System.out.println("Unable to connect. Server not running?"); // сообщаем...
//                            Toast t = Toast.makeText(getApplicationContext(), "Unable to connect. Server not running?", Toast.LENGTH_SHORT);
//                            t.show();
                        }
                    }
                });

//                int eca = 0;
//                while (!isServBase || eca < 10) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    eca += 1;
//                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                threadC.start();

//                eca = 0;
//
//                while (!isMultiplayer || eca < 10) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                    eca += 1;
//                }
                if (isMultiplayer) {
                    predbannikAct();
                }
            }
        });
        findViewById(R.id.Connect_to_Server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread threadC = new Thread(null, new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            runClient(((EditText) findViewById(R.id.Server_port)).getText().toString(), 8080); // Пробуем приконнетиться...
                        } catch (IOException e) { // если объект не создан...
                            System.out.println("Unable to connect. Server not running?"); // сообщаем...
                        }
                    }
                });
                threadC.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Rmazeneed = true;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!isMazegot) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (isMultiplayer && isMazegot) {
                    Maze.rand = Mazenom;
                    startPageAct();

                }
            }
        });

    }

    private void runServer_clientStartpage() {
        if (isServBase && Predban) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (ServnotgotMazenom) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            isMyTurn = true;
            isFirstConnect = false;
            setContentView(R.layout.activity_multiplayer);
            String IP = Utils.getIPAddress(true);
            System.out.println(IP);
            Thread threadservinfo = new Thread(null, new Runnable() {
                @Override
                public void run() {
                    ((TextView) findViewById(R.id.Server_IP)).setText(IP);
                    isGotServInfo = true;

                }
            });
            threadservinfo.start();
            if (isGotServInfo) {
                threadservinfo.interrupt();
                isSetupFineshed = true;
            }
            findViewById(R.id.Play).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startPageAct();
                }
            });
        }
    }

    private void predbannikAct() {

        setContentView(R.layout.activity_multiplayer_predbannik);

        MazeHolder.init(getApplicationContext());

        LinearLayout mc = (LinearLayout) findViewById(R.id.MapChooser);

        mc.removeAllViews();

        for (int i = 0; i < MazeHolder.MazeArr.size(); i++) {
            TextView txt = new TextView(getApplicationContext());
            txt.setTag(MazeHolder.MazeArr.get(i).name);
            txt.setText(MazeHolder.MazeArr.get(i).name);
            txt.setTextSize(25);
            txt.setPadding(50, 20, 10, 10);
            txt.setTextColor(Color.GREEN);
            RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            mc.addView(txt, rules);
            int finalI = i;
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chmap = finalI;
                }
            });

            ImageView txtdel = new ImageView(getApplicationContext());
            txtdel.setImageResource(getResources().getIdentifier("android:drawable/ic_menu_delete", null, null));
            txtdel.setPadding(10, 10, 50, 20);
            RelativeLayout.LayoutParams rules1 = new RelativeLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            mc.addView(txtdel, rules1);
            int finalI1 = i;
            txtdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteMap();
                }

                private void deleteMap() {
                    Toast t = Toast.makeText(getApplicationContext(), "A maze" + MazeHolder.MazeArr.get(finalI1).name + "has been deleted", Toast.LENGTH_SHORT);
                    t.show();
                    Gson gson = new Gson();
                    TextView txt = new TextView(getApplicationContext());
                    txt = (TextView) mc.findViewWithTag(MazeHolder.MazeArr.get(finalI1).name);
                    mc.removeView(txt);
                    SharedPreferences sharedPreferences = getSharedPreferences("mazehold", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sharedPreferencesForMholder.edit();
                    MazeHolder.MazeArr.remove(finalI1);
                    ed.putString("mazehold", gson.toJson(MazeHolder.MazeArr));
                    ed.apply();
                    mc.removeView(txtdel);
                }
            });
        }

        ImageView rand = (ImageView) findViewById(R.id.random);
        rand.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Random r = new Random();
                if (MazeHolder.MazeArr.size() == 1){
                    Maze.rand = 0;
                } else {
                    Maze.rand = r.nextInt(MazeHolder.MazeArr.size() - 1);
                }
                Predban = true;
                ServnotgotMazenom = true;

                runServer_clientStartpage();
            }
        });
        ImageView chose = (ImageView) findViewById(R.id.chooselayout);
        chose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (chmap != -1) {
                    Maze.rand = chmap;
                    Predban = true;
                    ServnotgotMazenom = true;

                    runServer_clientStartpage();
                }
            }
        });
    }

    private void startPageAct() {
        setContentView(R.layout.activity_multiplayer_start_page);


        int Cellsize = (int) ConvDPtoPX(1)*41;

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
                ImageView imageView = new ImageView(getApplicationContext());
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

    private class MyGestureListenerForMultiplayer extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onLongPress(MotionEvent e) {
            if (isDead) {
                die(Maze.YourCordInMaze);
                isDead = false;
                return;
            }
            if (isMyTurn) {
                int Cellsize = (int) ConvDPtoPX(1) * 41;
                int x = (int) e.getX();
                int y = (int) e.getY();
                int curx = zerocor[1] - (CurBasicCord[1] - Maze.YourCordInMaze[1]) * Cellsize / 2;
                int cury = zerocor[0] - (CurBasicCord[0] - Maze.YourCordInMaze[0]) * Cellsize / 2;
                int xin = (x > curx + Cellsize) ? 1 : ((x < curx) ? -1 : 0);
                int yin = (y > cury + Cellsize) ? 1 : ((y < cury) ? -1 : 0);
                if (xin < 0) {
                    if (yin == 0)
                        shoot(TURN_LEFT);
                } else if (xin > 0) {
                    if (yin == 0)
                        shoot(TURN_RIGHT);
                } else if (xin == 0) {
                    if (yin < 0)
                        shoot(TURN_UP);
                    else if (yin > 0)
                        shoot(TURN_DOWN);
                }
            } else {
                Toast t = Toast.makeText(getApplicationContext(), "Your opponents' turn", Toast.LENGTH_SHORT);
                t.show();
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (isDead) {
                die(Maze.YourCordInMaze);
                isDead = false;
                return true;
            }
            if (isMyTurn) {
                int Cellsize = (int) ConvDPtoPX(1) * 41;
                int x = (int) e.getX();
                int y = (int) e.getY();
                int curx = zerocor[1] - (CurBasicCord[1] - Maze.YourCordInMaze[1]) * Cellsize / 2;
                int cury = zerocor[0] - (CurBasicCord[0] - Maze.YourCordInMaze[0]) * Cellsize / 2;
                int xin = (x > curx + Cellsize) ? 1 : ((x < curx) ? -1 : 0);
                int yin = (y > cury + Cellsize) ? 1 : ((y < cury) ? -1 : 0);
                if (xin < 0) {
                    if (yin == 0)
                        turn(TURN_LEFT);
                } else if (xin > 0) {
                    if (yin == 0)
                        turn(TURN_RIGHT);
                } else if (xin == 0) {
                    if (yin < 0)
                        turn(TURN_UP);
                    else if (yin > 0)
                        turn(TURN_DOWN);
                    else
                        turn(TURN_NA);
                }
            } else {
                Toast t = Toast.makeText(getApplicationContext(), "Your opponents' turn", Toast.LENGTH_SHORT);
                t.show();
            }
            return true;
        }
    }

    static int[] Bulletcoord = new int[2];
    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        endMultiplTurn();
    }

    private void endMultiplTurn() {
        isMyTurn = false;
        isFirstConnect = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void goforBullet(int[] cord, int side) {
        killstreat.add(Arrays.asList( Arrays.stream( Bulletcoord ).boxed().toArray( Integer[]::new )));
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
        endMultiplTurn();
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
        int Cellsize = (int) ConvDPtoPX(1)*41;
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
        rules1.setMargins(OFFSET_LEFT - Cellsize, (int) (OFFSET_TOP + (layoutAmount + 1) * (Cellsize * Maze.SIZE_Y + OFFSET_BETWEEN)),
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
        ConstraintLayout l = (ConstraintLayout) findViewById(R.id.Container);
        l.removeView(l.findViewWithTag((res+"n")));
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.p0l);
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
        endMultiplTurn();
    }

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

    private void checkHospital() {
        if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] == HOSPITAL) {
            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.hospital);
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
            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.usedbullet);
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
        moveTOnewlayout();
        Maze.YourCordInMaze[0] = Maze.hospital.get(HOSPITAL)[0];
        Maze.YourCordInMaze[1] = Maze.hospital.get(HOSPITAL)[1];
        CurBasicCord[0] = Maze.YourCordInMaze[0];
        CurBasicCord[1] = Maze.YourCordInMaze[1];
        Maze.YourMazesholder.get(NativeLayout)[CurBasicCord[0]][CurBasicCord[1]] = 0;
        changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.hospital);
    }

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

    private void finishgame(){
        if (Maze.isKey){
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
    private void checkTheRvrORtp(){
        if ((Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] < TELEPORT) && (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] > 10)) {
            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.river);
            river(Maze.YourCordInMaze);
        } else if (Maze.Maze[Maze.YourCordInMaze[0]][Maze.YourCordInMaze[1]] > TELEPORT) {
            changeCell(new int[]{Maze.YourCordInMaze[0], Maze.YourCordInMaze[1]}, R.drawable.teleport1);
            teleport(Maze.YourCordInMaze);
        }
    }

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
        int Cellsize = (int) ConvDPtoPX(1)*41;
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
        int Cellsize = (int) ConvDPtoPX(1)*41;
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
        int Cellsize = (int) ConvDPtoPX(1)*41;
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
