package org.remoteDesktop.server;

import org.remoteDesktop.Verification;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ServerThread extends Thread implements Closeable {

    private final Socket socket;
    private final String serverPassword;

    public ServerThread(Socket socket, String serverPassword) {
        this.socket = socket;
        this.serverPassword = serverPassword;

        setDaemon(true);
        start();
    }

    @Override
    public void run() {

        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String receivedPassword = dis.readUTF();

            if (Objects.equals(receivedPassword, serverPassword)) {
                dos.writeUTF(Verification.CORRECT.toString());

                System.out.println("Server: new client connected");

                Robot robot = new Robot();
                Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

                dos.writeDouble(rectangle.getWidth());
                dos.writeDouble(rectangle.getHeight());

                new SendScreen(dos, robot, rectangle);
                new ReceiveEvents(dis, robot);

            } else {
                dos.writeUTF(Verification.INCORRECT.toString());
            }
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void close() {
        interrupt();
    }
}
