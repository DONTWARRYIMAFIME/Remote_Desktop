package org.infinityConnection.scenes.remoteScreen;

import javafx.fxml.FXMLLoader;
import org.infinityConnection.EffectType;
import org.infinityConnection.SceneController;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class RemoteScreen {

    public RemoteScreen(DataInputStream dis, DataOutputStream dos) {
        FXMLLoader loader = SceneController.getFXMLLoader("remoteScreen");
        RemoteScreenController controller = loader.getController();

        SceneController.setRoot("remoteScreen", EffectType.EASE_IN);

        controller.exchangeData(dis, dos);
    }

}