package org.infinityConnection.scenes.connect;

import javafx.fxml.FXMLLoader;
import org.infinityConnection.utils.SceneController;

public class ConnectScene {

    private final ConnectSceneController controller;

    public ConnectScene() {
        FXMLLoader loader = SceneController.getFXMLLoader("connectScene");
        controller = loader.getController();
    }

    public void establishConnection(String ip, int port, String password) {
        controller.establishConnection(ip, port, password);
    }

}
