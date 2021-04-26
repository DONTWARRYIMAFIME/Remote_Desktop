package org.remoteDesktop.server;

import javafx.application.Platform;
import org.remoteDesktop.NotificationsController;
import org.remoteDesktop.Verification;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Objects;

public class ServerThread extends Thread implements Closeable {

    private final Socket socket;
    private final String serverPassword;
    private final LinkedList<Closeable> threads;

    private SendScreen sEvent;
    private ReceiveEvents rEvent;

    private void checkConnection() throws IOException {
        while (true) {
            if (!sEvent.isInterrupted() && !rEvent.isInterrupted()) {
                continue;
            }
            close();
            break;
        }
    }

    public ServerThread(LinkedList<Closeable> threads, Socket socket, String serverPassword) {
        this.threads = threads;
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

                sEvent = new SendScreen(dos, robot, rectangle);
                rEvent = new ReceiveEvents(dis, robot);

                checkConnection();

            } else {
                dos.writeUTF(Verification.INCORRECT.toString());
                close();
            }
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void close() throws IOException {
        System.out.println("Client disconnected");
        Platform.runLater(NotificationsController::showUserDisconnected);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        interrupt();

        threads.remove(this);
    }
}
