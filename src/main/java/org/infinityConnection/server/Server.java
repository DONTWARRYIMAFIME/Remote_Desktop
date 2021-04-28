package org.infinityConnection.server;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.infinityConnection.NotificationsController;
import org.infinityConnection.SceneController;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.LinkedList;

public class Server extends Thread implements Closeable {

    private final LinkedList<Closeable> threads = new LinkedList<>();
    private ServerSocket socket = null;

    private int port;
    private final String serverPassword;
    private final int maxUsers = 10;

    private String generatePassword() {
        return "1111";
    }

    public Server() {
        this.port = 8001;
        this.serverPassword = generatePassword();

        try {
            socket = new ServerSocket(port, maxUsers);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setDaemon(true);
        start();
    }

    public Server(int port) {
        this();
        this.port = port;

    }

    public String getServerPassword() {
        return serverPassword;
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

        return "127.0.0.1";
    }

    @Override
    public void run() {

        try {
            while (!interrupted()) {
                threads.add(new ServerThread(threads, socket.accept(), serverPassword));
                System.out.println(threads.size());
                Platform.runLater(NotificationsController::showNewUserNotification);
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
