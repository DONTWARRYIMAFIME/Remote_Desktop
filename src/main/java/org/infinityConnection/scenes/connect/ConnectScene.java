package org.infinityConnection.scenes.connect;

import javafx.fxml.FXMLLoader;
import org.infinityConnection.utils.EffectType;
import org.infinityConnection.utils.SceneController;

public class ConnectScene {

    public ConnectScene(String ip, int port, String password) {

        FXMLLoader loader = SceneController.getFXMLLoader("connectScene");
        ConnectSceneController controller = loader.getController();

        SceneController.setRoot("connectScene", EffectType.EASE_IN);

        controller.establishConnection(ip, port, password);

    }

}
