package org.infinityConnection.scenes.server;

import javafx.application.Platform;
import org.infinityConnection.utils.EventsChangeListener;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int port;
    private final String serverPassword;
    private final int maxUsers = 10;

    private ServerSocket serverSocket;
    private ExecutorService serverService = Executors.newCachedThreadPool();
    private ExecutorService listenerService = Executors.newCachedThreadPool();

    private boolean stopWasRequested = false;
    private final List<EventsChangeListener> listeners = new ArrayList<>();

    private final LinkedList<ServerThread> clients = new LinkedList<>();

    private String generatePassword() {
        return "1111";
    }

    private void fireChangeEvent() {
        for (EventsChangeListener listener : listeners) {
            listener.onReadingChange();
            if (listener.isAutoCloasable()) {
                Platform.runLater(() -> listeners.remove(listener));
            }
        }
    }

    private void removeListeners() {
        listeners.clear();
    }

    private void shutDownClients() {
        for (ServerThread client : clients) {
            client.shutDown();
        }
    }

    private void interviewClients() {
        for (ServerThread client : clients) {
            if (client.isStopped()) {
                clients.remove(client);
            }
        }
    }

    public Server() {
        this.port = 8001;
        this.serverPassword = generatePassword();
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

    public int getClientsCount() {
        return clients.size();
    }

    public void addListener(EventsChangeListener listener) {
        listeners.add(listener);
    }

    public void start() {
        stopWasRequested = false;
        serverService = Executors.newCachedThreadPool();
        listenerService = Executors.newCachedThreadPool();

        serverService.submit(() -> {
            try {
                serverSocket = new ServerSocket(port, maxUsers);
                while (!stopWasRequested) {
                    clients.add(new ServerThread(serverSocket.accept(), serverPassword));
                }
            }catch (SocketException e) {
                System.out.println("Socket closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        listenerService.submit(() -> {
            while (!stopWasRequested) {
                fireChangeEvent();
                interviewClients();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void shutDown() {
        stopWasRequested = true;
        removeListeners();

        shutDownClients();

        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listenerService.shutdown();
        serverService.shutdown();
    }

}
