package org.infinityConnection.scenes.main;

import org.infinityConnection.scenes.connect.ConnectScene;
import org.infinityConnection.server.Server;

public class MainSceneModel {

    private Server server;

    public void startServer() {
        server = new Server(8001);

    }

    public String getServerIP() {
        return server.getIP();
    }

    public String getServerPassword() {
        return server.getServerPassword();
    }

    public void connectToServer(String ip, int port, String password) {
        new ConnectScene(ip, port, password);
    }

}
