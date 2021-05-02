package org.infinityConnection.client;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.infinityConnection.utils.KeyEvents;

import java.io.DataOutputStream;
import java.io.PrintWriter;

public class SendEvents {

    private final double ssWidth;
    private final double ssHeight;
    private final PrintWriter printWriter;

    private int convertMouseButton(MouseButton mouseButton) {
        switch (mouseButton) {
            case PRIMARY -> { return 16; }
            case MIDDLE -> { return 5; }
            case SECONDARY -> { return 4;}
            default -> { return 0; }
        }
    }

    private void onMouseMove(MouseEvent e, double iwWidth, double iwHeight, double iwFitWidth, double iwFitHeight) {

        double aspectRatio = iwWidth / iwHeight;
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

    private void onKeyPressed(KeyEvent e) {
        printWriter.println(KeyEvents.PRESS_KEY.getEventID());
        printWriter.println(java.awt.event.KeyEvent.getExtendedKeyCodeForChar(e.getCharacter().charAt(0)));
        printWriter.flush();
    }

    private void onKeyReleased(KeyEvent e) {
//        printWriter.println(KeyEvents.RELEASE_KEY.getEventID());
//        printWriter.println(java.awt.event.KeyEvent.getExtendedKeyCodeForChar(e.getCharacter().charAt(0)));
//        printWriter.flush();
    }

    public SendEvents(DataOutputStream dos, double ssWidth, double ssHeight) {
        this.printWriter = new PrintWriter(dos);
        this.ssWidth = ssWidth;
        this.ssHeight = ssHeight;
//        iw.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseMove);
//        iw.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
//        iw.addEventHandler(MouseEvent.MOUSE_RELEASED, this::onMouseReleased);
//        iw.addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
//        iw.addEventHandler(KeyEvent.KEY_RELEASED, this::onKeyReleased);
//
//        Platform.runLater(() -> iw.requestFocus());
    }

    public EventHandler getMouseMovedEH(double iwWidth, double iwHeight, double iwFitWidth, double iwFitHeight) {
        return event -> onMouseMove((MouseEvent) event, iwWidth, iwHeight, iwFitWidth, iwFitHeight);
    }

    public EventHandler getMousePressedEH() {
        return event -> onMousePressed((MouseEvent) event);
    }

    public EventHandler getMouseReleasedEH() {
        return event -> onMouseReleased((MouseEvent) event);
    }

    public EventHandler getKeyPressedEH() {
        return event -> onKeyPressed((KeyEvent) event);
    }

    public EventHandler getKeyReleasedEH() {
        return event -> onKeyReleased((KeyEvent) event);
    }

}
