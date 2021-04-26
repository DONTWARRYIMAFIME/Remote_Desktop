package org.remoteDesktop.remoteDesktopScene;

import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;

import org.remoteDesktop.ConnectionStatus;
import org.remoteDesktop.Loader;
import org.remoteDesktop.client.Client;

import java.io.IOException;

public class RemoteDesktopModel {

    private final ImageView iw;
    private final Label status;
    private final ToolBar toolBar;
    private final Label serverIP;
    private Client client;

    public RemoteDesktopModel(ImageView iw, Label status, ToolBar toolBar, Label serverIP) {
        this.iw = iw;
        this.status = status;
        this.toolBar = toolBar;
        this.serverIP = serverIP;
    }

    public void establishConnection(String ip, int port, String password) {

        iw.setImage(Loader.loadImage("loading.gif"));
        status.setText(ConnectionStatus.CONNECTING.getStatusName());

        client = new Client(ip, port , password);
        client.setGUIElements(status, iw, toolBar, serverIP);
        client.start();
    }

    public void onDisconnect() {
        try {
            client.onDisconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
