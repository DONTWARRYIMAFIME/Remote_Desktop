package org.infinityConnection.scenes.server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendScreen {

    private final DataOutputStream dos;
    private final Robot robot;
    private final Rectangle rectangle;

    private boolean stopWasRequested = false;
    private final ExecutorService service = Executors.newCachedThreadPool();

    private void start() {

        try {
            BufferedImage image = robot.createScreenCapture(rectangle);
            ImageIO.write(image, "jpeg", dos);

            Thread.sleep(10);
        } catch (IOException e) {
            shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            shutdown();
        }

    }

    public SendScreen(DataOutputStream dos, Robot robot, Rectangle rectangle) {
        this.dos = dos;
        this.robot = robot;
        this.rectangle = rectangle;

        service.submit(() -> {
            while(!stopWasRequested) {
                start();
            }
        });
    }

    public boolean isStopped() {
        return stopWasRequested;
    }

    public void shutdown() {
        stopWasRequested = true;

        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        service.shutdown();
        System.out.println("Server: send screen service shut downed");
    }

}
