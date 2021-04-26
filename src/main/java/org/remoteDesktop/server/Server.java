package org.remoteDesktop.server;

import javafx.application.Platform;
import org.remoteDesktop.NotificationsController;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
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

        String ip;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                if(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    System.out.println(iface.getDisplayName() + " " + ip);
                    return ip;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return "Null";
    }

    @Override
    public void run() {

        try {
            while (!interrupted()) {
                threads.add(new ServerThread(threads, socket.accept(), serverPassword));
                System.out.println(threads.size());
                Platform.runLater(() -> NotificationsController.showNewUserNotification());
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
