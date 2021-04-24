package org.remoteDesktop.remoteDesktopScene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.Socket;

public class RemoteDesktopSceneController {

    @FXML
    private ImageView iw;

    @FXML
    private Label status;

    private RemoteDesktopModel model;

    public void initialize() {
        model = new RemoteDesktopModel(iw, status);
    }

    public void setStage(Stage stage) {
        model.setStage(stage);
    }

    public void establishConnection(Socket socket, String password) {
        model.establishConnection(socket, password);
    }

}
