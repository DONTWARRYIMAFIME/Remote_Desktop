package org.remoteDesktop.client;

import org.remoteDesktop.remoteDesktopScene.RemoteDesktop;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private final String ip;
    private final int port;
    private final String password;

    public Client(String ip, int port, String password) {
        this.ip = ip;
        this.port = port;
        this.password = password;

        connect();
    }

    private void connect() {

        try {
            Socket socket = new Socket(ip, port);
            new RemoteDesktop(socket, password);
        } catch (IOException e) {
            //System.out.println("Connection timed out");
            e.printStackTrace();
        }
    }

//    @Override
//    public void run() {
//        connect();
//    }

}
