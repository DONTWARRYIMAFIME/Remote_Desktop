package org.infinityConnection.scenes.server;

import org.infinityConnection.utils.KeyEvents;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReceiveEvents {

    private final DataInputStream dis;
    private final Scanner scanner;
    private final Robot robot;

    private boolean stopWasRequested = false;
    private final ExecutorService service = Executors.newCachedThreadPool();

    private void start() {

        try {
            if (scanner.hasNext()) {
                KeyEvents command = KeyEvents.values()[scanner.nextInt()];

                System.out.println(command.toString());

                switch (command) {
                    case MOUSE_PRESS -> robot.mousePress(scanner.nextInt());
                    case MOUSE_RELEASED -> robot.mouseRelease(scanner.nextInt());
                    case KEY_RELEASED -> robot.keyPress(scanner.nextInt());
                    case MOUSE_MOVE -> robot.mouseMove(scanner.nextInt(), scanner.nextInt());
                    default -> System.out.println("ReceiveEvents. Unknown event");
                }

            }
        } catch (IllegalArgumentException e) {
            System.out.println("Could not recognize pressed key");
        } catch (NoSuchElementException e) {
            shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            shutdown();
        }

    }

    public ReceiveEvents(DataInputStream dis, Robot robot) {
        this.dis = dis;
        this.scanner = new Scanner(dis);
        this.robot = robot;

        service.submit(() -> {
            while (!stopWasRequested) {
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
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        service.shutdown();
        System.out.println("Server: receive event service shut downed");
    }

}
