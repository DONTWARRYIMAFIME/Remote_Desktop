package org.infinityConnection.scenes.remoteScreen;

import javafx.fxml.FXMLLoader;
import org.infinityConnection.utils.ConnectionStatus;
import org.infinityConnection.utils.EffectType;
import org.infinityConnection.utils.SceneController;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class RemoteScreen {

    private final RemoteScreenController controller;

    public RemoteScreen() {
        FXMLLoader loader = SceneController.getFXMLLoader("remoteScreen");
        controller = loader.getController();

        SceneController.setRoot("remoteScreen", EffectType.EASE_IN);
    }

    public ConnectionStatus getConnectionStatus() {
        return controller.getConnectionStatus();
    }

    public void exchangeData(DataInputStream dis, DataOutputStream dos) {
        controller.exchangeData(dis, dos);
    }

}
