package org.remoteDesktop.server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendScreen extends Thread {

    private DataOutputStream dos;
    private final Robot robot;
    private final Rectangle rectangle;

    private Socket sc;

    public SendScreen(DataOutputStream dos, Robot robot, Rectangle rectangle) {
        this.dos = dos;
        this.robot = robot;
        this.rectangle = rectangle;

        setDaemon(true);
        start();
    }

    @Override
    public void run() {

        try {
            while(!interrupted()) {
                BufferedImage image = robot.createScreenCapture(rectangle);
                ImageIO.write(image, "jpeg", dos);

                Thread.sleep(10);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Output stream is closed");
            System.out.println("Client disconnected");
            try {
                dos.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

}
