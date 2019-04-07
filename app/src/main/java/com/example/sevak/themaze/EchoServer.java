package com.example.sevak.themaze;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class EchoServer extends Thread {
 
    private DatagramSocket socket;
    public boolean running;
    private byte[] buf = new byte[256];
 
    public EchoServer() throws SocketException {
        socket = new DatagramSocket(4445);
    }
 
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void run() {
        running = true;
 
        while (running) {
            DatagramPacket packet
              = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received 
              = new String(packet.getData(), 0, packet.getLength());
             
            if (received.split("//////")[0].equals("\\\\SERV")) {
                if (!Client.runningServIPcontainer.contains(received.split("//////")[1])) {
                    if (Client.runningServs.get(received.split("//////")[1]) == null || !Client.runningServs.get(received.split("//////")[1])) {
                        Client.avalibleServers.put(received.split("//////")[1], received.split("//////")[2]);
                        Client.runningServIPcontainer.add(received.split("//////")[1]);
                        Client.runningServs.put(received.split("//////")[1], true);
                    }
                    try {
                        Client.gotBroadCast.put("ipv4");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                continue;
            }
            if (received.split("//////")[0].equals("\\\\DIED")) {
                Client.avalibleServers.remove(received.split("//////")[1]);
                Client.runningServIPcontainer.remove(received.split("//////")[1]);
                Client.runningServs.replace(received.split("//////")[1], false);
                try {
                    Client.gotBroadCast.put("ipv4");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}