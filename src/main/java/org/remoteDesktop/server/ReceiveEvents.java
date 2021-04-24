package org.remoteDesktop.server;

import org.remoteDesktop.KeyEvents;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ReceiveEvents extends Thread {

    private final DataInputStream dis;
    private final Robot robot;

    public ReceiveEvents(DataInputStream dis, Robot robot) {
        this.dis = dis;
        this.robot = robot;

        setDaemon(true);
        start();
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(dis);

        try {
            while (!interrupted()) {
                KeyEvents command = KeyEvents.values()[scanner.nextInt()];

                switch (command) {
                    case PRESS_MOUSE -> robot.mousePress(scanner.nextInt());
                    case RELEASE_MOUSE -> robot.mouseRelease(scanner.nextInt());
                    case PRESS_KEY -> robot.keyPress(scanner.nextInt());
                    case RELEASE_KEY -> robot.keyRelease(scanner.nextInt());
                    case MOVE_MOUSE -> robot.mouseMove(scanner.nextInt(), scanner.nextInt());
                    default -> System.out.println("ReceiveEvents. Unknown event");
                }
            }

        } catch (NoSuchElementException e) {
            System.out.println("Input stream is closed");
            try {
                dis.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

}
