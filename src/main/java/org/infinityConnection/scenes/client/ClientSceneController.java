package org.infinityConnection.scenes.client;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.infinityConnection.scenes.connect.ConnectScene;
import org.infinityConnection.utils.EffectType;
import org.infinityConnection.utils.SceneController;

public class ClientSceneController {

    @FXML
    private JFXTextField tfIP;

    @FXML
    private JFXTextField tfPassword;

    private final ConnectScene connectScene = new ConnectScene();

    public void onConnect() {
        int port = 8001;
        String ip = tfIP.getText();
        String password = tfPassword.getText();

        connectScene.establishConnection(ip, port, password);
        SceneController.setRoot("connectScene", EffectType.EASE_IN);
    }


}
