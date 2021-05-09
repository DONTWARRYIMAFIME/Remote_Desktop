package org.infinityConnection.scenes.server;

import org.infinityConnection.scenes.client.Verification;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThread {

    private final Socket socket;
    private final String serverPassword;

    private SendScreen sEvent;
    private ReceiveEvents rEvent;

    private DataInputStream dis;
    private DataOutputStream dos;

    private boolean stopWasRequested = false;
    private final ExecutorService service = Executors.newCachedThreadPool();

    private void checkConnection() {
        if (sEvent.isStopped() || rEvent.isStopped()) {
            shutDown();
        }
    }

    public ServerThread(Socket socket, String serverPassword) {
        this.socket = socket;
        this.serverPassword = serverPassword;

        service.submit(() -> {
           start();
        });
    }

    private void start() {

        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            String receivedPassword = dis.readUTF();

            if (Objects.equals(receivedPassword, serverPassword)) {
                dos.writeUTF(Verification.CORRECT.toString());

                System.out.println("Server: new client connected");

                Robot robot = new Robot();
                Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

                dos.writeDouble(rectangle.getWidth());
                dos.writeDouble(rectangle.getHeight());

                dos.writeUTF(InetAddress.getLocalHost().getHostName());

                sEvent = new SendScreen(dos, robot, rectangle);
                rEvent = new ReceiveEvents(dis, robot);

                while (!stopWasRequested) {
                    checkConnection();
                    Thread.sleep(1000);
                }

            } else {
                dos.writeUTF(Verification.INCORRECT.toString());
                shutDown();
            }
        } catch (IOException | AWTException | InterruptedException e) {
            e.printStackTrace();
            shutDown();
        }

    }

    public boolean isStopped() {
        return stopWasRequested;
    }


    public void shutDown() {
        stopWasRequested = true;

        if (!sEvent.isStopped()) {
            sEvent.shutDown();
        }

        if (!rEvent.isStopped()) {
            rEvent.shutDown();
        }

        service.shutdown();
        System.out.println("Client disconnected");
    }
}
