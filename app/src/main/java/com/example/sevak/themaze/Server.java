package com.example.sevak.themaze;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    private HashMap<Socket, List<Integer>> players_cords = new HashMap<>();
    private HashMap<Socket, String> players_condition = new HashMap<>();
    private ServerSocket ss; // сам сервер-сокет
    private Thread serverThread; // главная нить обработки сервер-сокета
    private int port; // порт сервер сокета.
    //очередь, где храняться все SocketProcessorы для рассылки
    BlockingQueue<SocketProcessor> q = new LinkedBlockingQueue<SocketProcessor>();
    private HashMap<Socket, String> usernames = new HashMap<Socket, String>();
    private String mn;

    /**
     * Конструктор объекта сервера
     * @param port Порт, где будем слушать входящие сообщения.
     * @throws IOException Если не удасться создать сервер-сокет, вылетит по эксепшену, объект Сервера не будет создан
     */
    public Server(int port) throws IOException {
        Client.isServBase = true;
        ss = new ServerSocket(port); // создаем сервер-сокет
        this.port = port; // сохраняем порт.
    }

    /**
     * главный цикл прослушивания/ожидания коннекта.
     */
    void run() {
        serverThread = Thread.currentThread(); // со старта сохраняем нить (чтобы можно ее было interrupt())
        while (true) { //бесконечный цикл, типа...
            Socket s = getNewConn(); // получить новое соединение или фейк-соедиение
            if (serverThread.isInterrupted()) { // если это фейк-соединение, то наша нить была interrupted(),
                // надо прерваться
                break;
            } else if (s != null){ // "только если коннект успешно создан"...
                try {
                    players_cords.put(s, null);
                    players_condition.put(s, null);
                    final SocketProcessor processor = new SocketProcessor(s); // создаем сокет-процессор
                    final Thread thread = new Thread(processor); // создаем отдельную асинхронную нить чтения из сокета
                    thread.setDaemon(true); //ставим ее в демона (чтобы не ожидать ее закрытия)
                    thread.start(); //запускаем
                    q.offer(processor); //добавляем в список активных сокет-процессоров
                } //тут прикол в замысле. Если попытка создать (new SocketProcessor()) безуспешна,
                // то остальные строки обойдем, нить запускать не будем, в список не сохраним
                catch (IOException ignored) {}  // само же исключение создания коннекта нам не интересно.
            }
        }
    }

    /**
     * Ожидает новое подключение.
     * @return Сокет нового подключения
     */
    private Socket getNewConn() {
        Socket s = null;
        try {
            s = ss.accept();
        } catch (IOException e) {
            shutdownServer(); // если ошибка в момент приема - "гасим" сервер
        }
        return s;
    }

    /**
     * метод "глушения" сервера
     */
    private synchronized void shutdownServer() {
        // обрабатываем список рабочих коннектов, закрываем каждый
        for (SocketProcessor s: q) {
            s.close();
        }
        if (!ss.isClosed()) {
            try {
                ss.close();
            } catch (IOException ignored) {}
        }
    }

//    /**
//     * входная точка программы
//     * @param args
//     * @throws IOException
//     */
//    public static void main(String[] args) throws IOException {
//        new Server(8080).run(); // если сервер не создался, программа
//        // вылетит по эксепшену, и метод run() не запуститься
//    }

    /**
     * вложенный класс асинхронной обработки одного коннекта.
     */
    private class SocketProcessor implements Runnable{
        Socket s; // наш сокет
        BufferedReader br; // буферизировнный читатель сокета
        BufferedWriter bw; // буферизированный писатель в сокет

        /**
         * Сохраняем сокет, пробуем создать читателя и писателя. Если не получается - вылетаем без создания объекта
         * @param socketParam сокет
         * @throws IOException Если ошибка в создании br || bw
         */
        SocketProcessor(Socket socketParam) throws IOException {
            s = socketParam;
            br = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
            bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8") );
        }

        /**
         * Главный цикл чтения сообщений/рассылки
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void run() {
            while (!s.isClosed()) { // пока сокет не закрыт...
                String line = null;
                try {
                    line = br.readLine(); // пробуем прочесть.
                } catch (IOException e) {
                    close(); // если не получилось - закрываем сокет.
                }

                if (line == null) { // если строка null - клиент отключился в штатном режиме.
                    close(); // то закрываем сокет
                } else if ("\\\\shutdown".equals(line)) { // если поступила команда "погасить сервер", то...
                    serverThread.interrupt(); // сначала возводим флаг у северной нити о необходимости прерваться.
                    try {
                        new Socket("10.1.201.104", port); // создаем фейк-коннект (чтобы выйти из .accept())
                    } catch (IOException ignored) { //ошибки неинтересны
                    } finally {
                        shutdownServer(); // а затем глушим сервер вызовом его метода shutdownServer().
                    }
                } else if (line.length()>12 && line.substring(0, 12).equals("\\\\username: ")) {
                    System.out.println("Got username");
                    boolean isUsed = false;
                    if (usernames.size() != 0) {
                        for (String un : usernames.values()) {
                            if (un.equals(line.substring(12))) {
                                isUsed = true;
                            }
                        }
                    }
                    if (isUsed) {
                        sendServerReport("\\\\nope");
                    } else {
                        sendServerReport("\\\\okay");
                        usernames.put(s, line.substring(12));
                    }
                } else if (line.split("//////")[0].equals("\\\\next_turn:")) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean isNext = false;
                    boolean isLast = true;
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Integer>>() {}.getType();
                    players_cords.replace(s, gson.fromJson(line.split("//////")[2], type));
                    players_condition.replace(s, "alive");
                    type = new TypeToken<HashSet<List<Integer>>>() {}.getType();
                    HashSet<List<Integer>> kilstr = gson.fromJson(line.split("//////")[3], type);
                    for (Socket i : players_cords.keySet()) {
                        if (kilstr.contains(players_cords.get(i))){
                            players_condition.replace(i, "dead");
                        }
                    }
                    for (SocketProcessor sp:q) {
                        if (isNext){
                            isLast = false;
                            sp.send("\\\\your_turn: " + line.split("//////")[1] + " " + players_condition.get(s));
                        }
                        if (sp.s.equals(s)) {
                            isNext = true;
                        }
                    }
                    if (isLast) {
                        for (SocketProcessor sp:q) {
                            sp.send("\\\\your_turn: " + line.split("//////")[1] + " " + players_condition.get(s));
                            break;
                        }
                    }
                } else if (line.split(" ")[0].equals("\\\\maze_nom:")) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (SocketProcessor sp:q) {
                        if (sp.s.equals(s)) {
                            sp.send("\\\\amaze_nom: okay");
                        }
                    }
                    mn = line.substring(line.split(" ")[0].length() + 1);
                } else if (line.split(" ")[0].equals("\\\\rmaze_nom:")) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    for (SocketProcessor sp:q) {
//                        if (sp.s.equals(s)) {
//                            sp.send("\\\\amaze_nom: okay");
//                        }
//                    }
                    for (SocketProcessor sp:q) {
                        if (sp.s.equals(s)) {
                            sp.send("\\\\maze_nom: " + mn);
                        }
                    }
                } else { // иначе - банальная рассылка по списку сокет-процессоров
                    for (SocketProcessor sp:q) {
                        if (!sp.s.equals(s)) {
                            sp.send(usernames.get(s) + ": " + line);
                        }
                    }
                }
            }
        }

        private void sendServerReport(String rep) {
            for (SocketProcessor sp : q) {
                if (sp.s.equals(s)) {
                    sp.send(rep);
                }
            }
        }

        /**
         * Метод посылает в сокет полученную строку
         * @param line строка на отсылку
         */
        public synchronized void send(String line) {
            try {
                bw.write(line); // пишем строку
                bw.write("\n"); // пишем перевод строки
                bw.flush(); // отправляем
            } catch (IOException e) {
                close(); //если глюк в момент отправки - закрываем данный сокет.
            }
        }

        /**
         * метод аккуратно закрывает сокет и убирает его со списка активных сокетов
         */
        public synchronized void close() {
            q.remove(this); //убираем из списка
            if (!s.isClosed()) {
                try {
                    s.close(); // закрываем
                } catch (IOException ignored) {}
            }
        }

        /**
         * финализатор просто на всякий случай.
         * @throws Throwable
         */
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            close();
        }
    }
}
