package org.remoteDesktop.remoteDesktopScene;

import javafx.fxml.FXMLLoader;
import org.remoteDesktop.EffectType;
import org.remoteDesktop.SceneController;

import java.io.IOException;

public class RemoteDesktop {

    public RemoteDesktop(String ip, int port, String password) throws IOException {

        FXMLLoader loader = SceneController.getFXMLLoader("remoteDesktopScene");
        RemoteDesktopSceneController controller = loader.getController();

        SceneController.setRoot("remoteDesktopScene", EffectType.EASE_IN);

        controller.establishConnection(ip, port, password);

    }

}
