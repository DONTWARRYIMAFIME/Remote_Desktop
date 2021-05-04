package org.infinityConnection.scenes.remoteScreen;

import com.jfoenix.controls.JFXToolbar;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.infinityConnection.scenes.connect.ConnectSceneModel;
import org.infinityConnection.utils.ConnectionStatus;
import org.infinityConnection.utils.EffectType;
import org.infinityConnection.utils.SceneController;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class RemoteScreenController {

    @FXML
    private Label host;

    @FXML
    private Label timer;

    @FXML
    private ImageView iw;

    @FXML
    private JFXToolbar toolbar;

    private double iwWidth;
    private double iwHeight;

    private double iwFitWidth;
    private double iwFitHeight;

    private ConnectionStatus connectionStatus = ConnectionStatus.CONNECTED;

    private Stage stage;
    private RemoteScreenModel model;
    private final ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> onResize();

    private void updateConnectionStatus() {
        connectionStatus = model.getConnectionStatus();
        if (connectionStatus != ConnectionStatus.CONNECTED) {
            Platform.runLater(() -> SceneController.setRoot("connectScene", EffectType.EASE_OUT));
        }
    }

    private void updateHostName() {
        Platform.runLater(() -> host.setText(model.getHostName()));
        model.removeListener(this::updateHostName);
    }

    private void updateTimer() {
        Platform.runLater(() -> timer.setText(model.getSessionTime()));
    }

    private void updateScreen() {
        Image image = iw.getImage();
        Platform.runLater(() -> {
            iw.setImage(model.getReceivedImage());
            if (image == null) {
                onResize();
            }
        });

    }

    private void onResize() {
        try {
            iwFitWidth = iw.getScene().getWidth();
            iwFitHeight = iw.getScene().getHeight() - toolbar.getHeight();

            iw.setFitWidth(iwFitWidth);
            iw.setFitHeight(iwFitHeight);

            Image image = iw.getImage();
            iwWidth = image.getWidth();
            iwHeight = image.getHeight();

            model.addListener(this::onMouseMoved);
        } catch (NullPointerException e) {}
    }

    //Mouse events
    private void onMouseMoved() {
        iw.setOnMouseClicked(model.getMouseMovedEH(iwWidth, iwHeight, iwFitWidth, iwFitHeight));
        model.removeListener(this::onMousePressed);
    }

    private void onMousePressed() {
        iw.setOnMousePressed(model.getMousePressedEH());
        model.removeListener(this::onMousePressed);
    }

    private void onMouseReleased() {
        iw.setOnMouseReleased(model.getMouseReleasedEH());
        model.removeListener(this::onMouseReleased);
    }

    //Keyboard events
    private void onKeyPressed() {
        iw.setOnKeyTyped(model.getKeyPressedEH());
        model.removeListener(this::onKeyPressed);
    }

    private void onKeyReleased() {
        iw.setOnKeyReleased(model.getKeyReleasedEH());
        model.removeListener(this::onKeyReleased);
    }

    public void exchangeData(DataInputStream dis, DataOutputStream dos) {

        Platform.runLater(() -> iw.requestFocus() );

        model = new RemoteScreenModel(dis, dos);

        stage = (Stage) SceneController.scene.getWindow();
        stage.setOnCloseRequest((e) -> model.shutDown());

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);

        model.addListener(this::updateHostName);
        model.addListener(this::updateTimer);
        model.addListener(this::updateScreen);

        //Events
        model.addListener(this::onMousePressed);
        model.addListener(this::onMouseReleased);

        model.addListener(this::onKeyPressed);
        model.addListener(this::onKeyReleased);

        model.addListener(this::updateConnectionStatus);
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void onDisconnect() {
        iw.setImage(null);

        stage.widthProperty().removeListener(stageSizeListener);
        stage.heightProperty().removeListener(stageSizeListener);

        model.shutDown();
    }
}
