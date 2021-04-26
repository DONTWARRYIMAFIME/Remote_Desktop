package org.remoteDesktop.remoteDesktopScene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;

public class RemoteDesktopSceneController {

    @FXML
    private ImageView iw;

    @FXML
    private Label status;

    @FXML
    private ToolBar toolBar;

    @FXML
    private Label serverIP;

    private RemoteDesktopModel model;

    public void initialize() {
        model = new RemoteDesktopModel(iw, status, toolBar, serverIP);
    }

    public void establishConnection(String ip, int port, String password) {
        model.establishConnection(ip, port, password);
    }

    public void onDisconnect() {
        model.onDisconnect();
    }

}
