package org.infinityConnection.scenes.remoteScreen;

import javafx.fxml.FXMLLoader;
import org.infinityConnection.utils.SceneController;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class RemoteScreen {

    private final RemoteScreenController controller;

    public RemoteScreen() {
        FXMLLoader loader = SceneController.getFXMLLoader("remoteScreen");
        controller = loader.getController();
    }

    public boolean isStopped() {
        return controller.isStopped();
    }

    public void shutDown() {
        controller.onDisconnect();
    }

    public void exchangeData(DataInputStream dis, DataOutputStream dos) {
        controller.exchangeData(dis, dos);
    }

}
