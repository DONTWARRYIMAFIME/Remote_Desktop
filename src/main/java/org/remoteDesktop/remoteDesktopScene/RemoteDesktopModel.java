package org.remoteDesktop.remoteDesktopScene;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import org.remoteDesktop.ConnectionStatus;
import org.remoteDesktop.Loader;
import org.remoteDesktop.client.Client;

public class RemoteDesktopModel {

    private final ImageView iw;
    private final Label status;

    public RemoteDesktopModel(ImageView iw, Label status) {
        this.iw = iw;
        this.status = status;
    }

    public void establishConnection(String ip, int port, String password) {

        iw.setImage(Loader.loadImage("loading.gif"));
        status.setText(ConnectionStatus.CONNECTING.getStatusName());

        new Client(ip, port , password, status, iw);
    }

}
