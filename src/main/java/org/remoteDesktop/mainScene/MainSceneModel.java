package org.remoteDesktop.mainScene;

import javafx.stage.Stage;
import org.remoteDesktop.Utils;
import org.remoteDesktop.client.Client;
import org.remoteDesktop.remoteDesktopScene.RemoteDesktop;
import org.remoteDesktop.server.Server;

import java.io.IOException;

public class MainSceneModel {

    private final String serverPassword;
    private Server server;

    MainSceneModel() {
        serverPassword = Utils.generatePassword(4);
        server = new Server(8001, serverPassword);
        //server.listen();
    }

    public String getServerIP() {
        return server.getIP();
    }

    public String getServerPassword() {
        return serverPassword;
    }

//    public void becomeClient() {
//        client = new Client();
//    }

    public void connectToServer(String ip, int port, String password) {
        try {
            new RemoteDesktop(ip, port, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
