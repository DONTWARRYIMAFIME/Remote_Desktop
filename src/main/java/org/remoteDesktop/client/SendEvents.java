package org.remoteDesktop.client;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.remoteDesktop.KeyEvents;

import java.io.DataOutputStream;
import java.io.PrintWriter;

public class SendEvents {

    private final PrintWriter printWriter;
    private final ImageView iw;
    private final double ssWidth;
    private final double ssHeight;

    private void onMouseMove(MouseEvent e) {
        double xScale = ssWidth / iw.getFitWidth();
        double yScale = ssHeight / iw.getFitHeight();

        printWriter.println(KeyEvents.MOVE_MOUSE.getEventID());
        printWriter.println((int) (e.getX() * xScale));
        printWriter.println((int) (e.getY() * yScale));
        printWriter.flush();
    }

    private void onMousePressed(MouseEvent e) {
        printWriter.println(KeyEvents.PRESS_MOUSE.getEventID());
        int button = e.getButton().ordinal();
        int xButton = 16;
        if(button==3){
            xButton = 4;
        }
        printWriter.println(xButton);
        printWriter.flush();
    }

    private void onMouseReleased(MouseEvent e) {
        printWriter.println(KeyEvents.RELEASE_MOUSE.getEventID());
        int button = e.getButton().ordinal();
        int xButton = 16;
        if(button==3){
            xButton = 4;
        }
        printWriter.println(xButton);
        printWriter.flush();
    }

    private void onKeyPressed(KeyEvent e) {
        printWriter.println(KeyEvents.PRESS_KEY.getEventID());
        printWriter.println(e.getCode());
        printWriter.flush();
    }

    private void onKeyReleased(KeyEvent e) {
        printWriter.println(KeyEvents.RELEASE_KEY.getEventID());
        printWriter.println(e.getCode());
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
    }

}
