package org.remoteDesktop.remoteDesktopScene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.remoteDesktop.ConnectionStatus;
import org.remoteDesktop.Loader;

public class RemoteDesktopSceneController {

    @FXML
    private ImageView iw;

    @FXML
    private Label status;

    private RemoteDesktopModel model;

    public void initialize() {
        model = new RemoteDesktopModel(iw, status);
    }

    public void establishConnection(String ip, int port, String password) {
        model.establishConnection(ip, port, password);
    }

//    public void setClient(Client client) {
//        model.setClient(client);
//    }
}
