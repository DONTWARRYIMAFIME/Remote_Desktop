package org.infinityConnection.scenes.server;

import org.infinityConnection.utils.EventsChangeListener;
import org.infinityConnection.utils.ShutDownable;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements ShutDownable {

    private int port;
    private final String serverPassword;
    private final int maxUsers = 10;

    private ServerSocket socket;
    private final ExecutorService service = Executors.newCachedThreadPool();
    private boolean stopWasRequested = false;
    private final List<EventsChangeListener> listeners = new ArrayList<>();

    private final LinkedList<ShutDownable> clients = new LinkedList<>();

    private String generatePassword() {
        return "1111";
    }

    private void fireChangeEvent() {
        for (EventsChangeListener listener : listeners) {
            listener.onReadingChange();
            if (listener.isAutoCloasable()) {
                listeners.remove(listener);
            }
        }
    }

    private void shutDownClients() {
        for (ShutDownable client : clients) {
            client.shutDown();
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

    public void removeListener(EventsChangeListener listener) {
        listeners.remove(listener);
    }

    public void start() {
        stopWasRequested = false;
        service.submit(() -> {
            try {
                socket = new ServerSocket(port, maxUsers);

                while (!stopWasRequested) {
                    Thread.sleep(1000);
                    clients.add(new ServerThread(clients, socket.accept(), serverPassword));
                    fireChangeEvent();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void shutDown() {
        stopWasRequested = true;

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        shutDownClients();
        service.shutdown();
    }

}
