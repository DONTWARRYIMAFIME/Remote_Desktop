package org.remoteDesktop.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.URL;
import java.util.LinkedList;

public class Server extends Thread implements Closeable {

    private final LinkedList<Closeable> threads = new LinkedList<>();
    private int port = 8001;
    private ServerSocket socket = null;

    private String serverPassword;
    private int maxUsers = 10;


    public Server(int port, String serverPassword) {
        this.port = port;
        this.serverPassword = serverPassword;

        try {
            socket = new ServerSocket(port, maxUsers);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setDaemon(true);
        start();
    }

    public String getIP() {

        String ip = null;

        try {
            URL url = new URL("http://checkip.amazonaws.com/");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            ip = br.readLine();
        } catch (Exception e) {
            System.out.println("Amazon server is not available");
        }

        return ip;
    }

    @Override
    public void run() {

        try {
            while (!interrupted()) {
                threads.add(new ServerThread(socket.accept(), serverPassword));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        for (Closeable thread : threads) {
            try {
                thread.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        interrupt();
    }
}
