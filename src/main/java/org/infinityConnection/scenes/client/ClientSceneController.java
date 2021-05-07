package org.infinityConnection.scenes.client;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.infinityConnection.scenes.connect.ConnectScene;

public class ClientSceneController {

    @FXML
    private JFXTextField tfIP;

    @FXML
    private JFXTextField tfPassword;

    public void onConnect() {
        int port = 8001;
        String ip = tfIP.getText();
        String password = tfPassword.getText();

        new ConnectScene(ip, port, password);
    }


}
