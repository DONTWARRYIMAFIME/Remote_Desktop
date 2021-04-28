package org.infinityConnection.scenes.connect;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.infinityConnection.ConnectionStatus;

public class ConnectSceneController {

    @FXML
    private Label status;

    @FXML
    private Circle circle1;

    @FXML
    private Circle circle2;

    @FXML
    private Circle circle3;

    private void setRotation(Circle c, boolean reverse, int angle, int duration) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(duration), c);

        rotateTransition.setAutoReverse(reverse);

        rotateTransition.setByAngle(angle);
        rotateTransition.setDelay(Duration.seconds(0));
        rotateTransition.setRate(3);
        rotateTransition.setCycleCount(18);

        rotateTransition.setOnFinished((e) -> setRotation(c, reverse, angle, duration));
        rotateTransition.play();

    }

    public void initialize() {
        setRotation(circle1, true, 360, 10);
        setRotation(circle2, true, 180, 18);
        setRotation(circle3, true, 145, 24);
    }

    public void establishConnection(String ip, int port, String password) {
        status.setText(ConnectionStatus.CONNECTING.getStatusName());

        ConnectSceneModel model = new ConnectSceneModel(ip, port, password);
        model.setGUIElements(circle1, circle2, circle3, status);
        model.start();
    }

}
