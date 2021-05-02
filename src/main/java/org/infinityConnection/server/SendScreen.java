package org.infinityConnection.server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;

public class SendScreen extends Thread implements Closeable {

    private final DataOutputStream dos;
    private final Robot robot;
    private final Rectangle rectangle;

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

                Thread.sleep(40);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("The client dropped the connection");
            close();
        }

    }

    @Override
    public void close() {
        System.out.println("Server: send screen event interrupted");
        interrupt();
    }
}
