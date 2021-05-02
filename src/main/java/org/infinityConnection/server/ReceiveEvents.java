package org.infinityConnection.server;

import org.infinityConnection.utils.KeyEvents;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.Closeable;
import java.io.DataInputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ReceiveEvents extends Thread implements Closeable {

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
                if (scanner.hasNext()) {
                    KeyEvents command = KeyEvents.values()[scanner.nextInt()];

                    System.out.println(command.toString());

                    //robot.mouseMove(0, 0);

                    //System.out.println(KeyEvent.getKeyText(scanner.nextInt()));

                    switch (command) {
                        case PRESS_MOUSE -> robot.mousePress(scanner.nextInt());
                        case RELEASE_MOUSE -> robot.mouseRelease(scanner.nextInt());
                        case PRESS_KEY -> robot.keyPress(scanner.nextInt());
                        case RELEASE_KEY -> robot.keyRelease(scanner.nextInt());
                        case MOVE_MOUSE -> robot.mouseMove(scanner.nextInt(), scanner.nextInt());
                        default -> System.out.println("ReceiveEvents. Unknown event");
                    }



                    //robot.delay(1000);
                }
            }

        } catch (NoSuchElementException e) {
            System.out.println("The client dropped the connection");
            close();
        }

    }

    @Override
    public void close() {
        System.out.println("Server: receive event interrupted");
        interrupt();
    }
}
