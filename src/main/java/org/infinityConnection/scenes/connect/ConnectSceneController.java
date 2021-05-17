package org.infinityConnection.scenes.connect;

import javafx.animation.RotateTransition;
import javafx.animation.StrokeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.infinityConnection.utils.ConnectionStatus;
import org.infinityConnection.utils.EffectType;
import org.infinityConnection.utils.EventsChangeListener;
import org.infinityConnection.utils.SceneController;

import java.util.ArrayList;
import java.util.List;

public class ConnectSceneController {

    @FXML
    private Label status;

    @FXML
    private Circle circle1;

    @FXML
    private Circle circle2;

    @FXML
    private Circle circle3;

    private ConnectionStatus connectionStatus;
    private final ConnectSceneModel model = new ConnectSceneModel();
    private final List<Circle> circles = new ArrayList<>();
    private final List<Paint> normalColors = new ArrayList<>();

    private void setRotation(Circle c, boolean reverse, int angle, int duration) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(duration), c);

        rotateTransition.setAutoReverse(reverse);

        rotateTransition.setByAngle(angle);
        rotateTransition.setDelay(Duration.seconds(0));
        rotateTransition.setRate(3);
        rotateTransition.setCycleCount(18);

        rotateTransition.play();
    }

    private void setSuccessColors() {
        for (Circle circle : circles) {
            StrokeTransition strokeTransition = new StrokeTransition(Duration.seconds(1), circle);
            strokeTransition.setToValue(Color.GREEN);
            strokeTransition.play();
        }
    }

    private void setErrorColors() {
        for (Circle circle : circles) {
            StrokeTransition strokeTransition = new StrokeTransition(Duration.seconds(1), circle);
            strokeTransition.setToValue(Color.RED);
            strokeTransition.play();
        }
    }

    private void setNormalColors() {
        for (int i = 0; i < circles.size(); i++) {
            circles.get(i).setStroke(normalColors.get(i));
        }
    }

    private EventsChangeListener updateConnectionStatus() {
        return new EventsChangeListener() {
            @Override
            public void onReadingChange() {
                connectionStatus = model.getConnectionStatus();

                if (connectionStatus != ConnectionStatus.CONNECTING) {
                    updateLabel();
                    if (connectionStatus == ConnectionStatus.CONNECTED) {
                        setSuccessColors();
                    } else {
                        setErrorColors();
                        model.shutDown();
                        changeScene();
                    }
                }
            }

            @Override
            public boolean isAutoCloasable() {
                return false;
            }
        };
    }

    private void updateLabel() {
        Platform.runLater(() -> status.setText(connectionStatus.getStatusName()));
    }

    private void changeScene() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> SceneController.setRoot("containerScene", EffectType.EASE_OUT));
    }

    private void initColorsArray() {
        for (Circle circle : circles) {
            normalColors.add(circle.getStroke());
        }
    }

    private void closeWindowEvent(WindowEvent event) {
        model.shutDown();
    }

    public void initialize() {
        circles.add(circle1);
        circles.add(circle2);
        circles.add(circle3);

        initColorsArray();

        Stage stage = (Stage) SceneController.scene.getWindow();
        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    public void establishConnection(String ip, int port, String password) {
        setRotation(circle1, true, 360, 10);
        setRotation(circle2, true, 180, 18);
        setRotation(circle3, true, 145, 24);

        connectionStatus = ConnectionStatus.CONNECTING;

        setNormalColors();
        status.setText(ConnectionStatus.CONNECTING.getStatusName());

        model.addListener(updateConnectionStatus());
        model.start(ip, port, password);
    }

}
