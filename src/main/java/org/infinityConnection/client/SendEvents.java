package org.infinityConnection.client;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.infinityConnection.KeyEvents;

import java.io.DataOutputStream;
import java.io.PrintWriter;

public class SendEvents {

    private final PrintWriter printWriter;
    private final ImageView iw;
    private final double ssWidth;
    private final double ssHeight;

    private int convertMouseButton(MouseButton mouseButton) {
        switch (mouseButton) {
            case MIDDLE -> { return 5; }
            case SECONDARY -> { return 4;}
            default -> { return 16; }
        }
    }

    private void onMouseMove(MouseEvent e) {
        Image image = iw.getImage();
        double aspectRatio = image.getWidth() / image.getHeight();
        double realWidth = Math.min(iw.getFitWidth(), iw.getFitHeight() * aspectRatio);
        double realHeight = Math.min(iw.getFitHeight(), iw.getFitWidth() / aspectRatio);

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
        printWriter.println(java.awt.event.KeyEvent.VK_X);
        printWriter.flush();
    }

    private void onKeyReleased(KeyEvent e) {
        printWriter.println(KeyEvents.RELEASE_KEY.getEventID());
        printWriter.println(java.awt.event.KeyEvent.VK_X);
        printWriter.flush();
    }

    public SendEvents(DataOutputStream dos, ImageView iw, double ssWidth, double ssHeight) {
        this.printWriter = new PrintWriter(dos);
        this.iw = iw;
        this.ssWidth = ssWidth;
        this.ssHeight = ssHeight;

        iw.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseMove);
        iw.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
        iw.addEventHandler(MouseEvent.MOUSE_RELEASED, this::onMouseReleased);
        iw.addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        iw.addEventHandler(KeyEvent.KEY_RELEASED, this::onKeyReleased);

        Platform.runLater(() -> iw.requestFocus());
    }

}
