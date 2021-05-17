package org.infinityConnection.scenes.client;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.infinityConnection.utils.KeyEvents;

import java.io.DataOutputStream;
import java.io.PrintWriter;

public class SendEvents {

    private double ssWidth;
    private double ssHeight;
    private double aspectRatio;
    private PrintWriter printWriter;

    private int convertMouseButton(MouseButton mouseButton) {
        switch (mouseButton) {
            case PRIMARY -> { return 16; }
            case MIDDLE -> { return 5; }
            case SECONDARY -> { return 4;}
            default -> { return 0; }
        }
    }

    private void onMouseMove(MouseEvent e, double iwFitWidth, double iwFitHeight) {

        double realWidth = Math.min(iwFitWidth, iwFitHeight * aspectRatio);
        double realHeight = Math.min(iwFitHeight, iwFitWidth / aspectRatio);

        double xScale = ssWidth / realWidth;
        double yScale = ssHeight / realHeight;

        printWriter.println(KeyEvents.MOVE_MOUSE.getEventID());
        printWriter.println((int) (e.getX() * xScale));
        printWriter.println((int) (e.getY() * yScale));
        printWriter.flush();
    }

    private void onMousePressed(MouseEvent e) {
        printWriter.println(KeyEvents.PRESS_MOUSE.getEventID());
        printWriter.println(convertMouseButton(e.getButton()));
        printWriter.flush();
    }

    private void onMouseReleased(MouseEvent e) {
        printWriter.println(KeyEvents.RELEASE_MOUSE.getEventID());
        printWriter.println(convertMouseButton(e.getButton()));
        printWriter.flush();
    }

    private void onKeyReleased(KeyEvent e) {
        try {
            int events = KeyEvents.RELEASE_KEY.getEventID();
            int key = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(e.getCharacter().charAt(0));

            printWriter.println(events);
            printWriter.println(key);
            printWriter.flush();
        } catch (Exception ex) {
            System.out.println("Could not recognize pressed key");
        }
    }

    public void setComponents(DataOutputStream dos, double ssWidth, double ssHeight) {
        this.printWriter = new PrintWriter(dos);
        this.ssWidth = ssWidth;
        this.ssHeight = ssHeight;

        this.aspectRatio = ssWidth / ssHeight;
    }

    public EventHandler getMouseMovedEH(double iwFitWidth, double iwFitHeight) {
        return event -> onMouseMove((MouseEvent) event, iwFitWidth, iwFitHeight);
    }

    public EventHandler getMousePressedEH() {
        return event -> onMousePressed((MouseEvent) event);
    }

    public EventHandler getMouseReleasedEH() {
        return event -> onMouseReleased((MouseEvent) event);
    }

    public EventHandler getKeyReleasedEH() {
        return event -> onKeyReleased((KeyEvent) event);
    }

}
