package org.infinityConnection.scenes.connect;

import javafx.application.Platform;
import org.infinityConnection.scenes.server.Server;
import org.infinityConnection.utils.*;
import org.infinityConnection.scenes.client.Verification;
import org.infinityConnection.scenes.client.Authentication;
import org.infinityConnection.scenes.remoteScreen.RemoteScreen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectSceneModel {

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private ConnectionStatus connectionStatus;
    private RemoteScreen remoteScreen = new RemoteScreen();

    private boolean stopWasRequested = false;
    private ExecutorService service = Executors.newCachedThreadPool();
    private ExecutorService listener = Executors.newCachedThreadPool();
    private final List<EventsChangeListener> listeners = new ArrayList<>();

    private void fireGUIChangeEvent() {
        for (EventsChangeListener listener : listeners) {
            listener.onReadingChange();
            if (listener.isAutoCloasable()) {
                Platform.runLater(() -> listeners.remove(listener));
            }
        }
    }

    public void removeListeners() {
        listeners.clear();
    }

    public void start(String ip, int port, String password) {
        service = Executors.newCachedThreadPool();
        listener = Executors.newCachedThreadPool();

        stopWasRequested = false;
        connectionStatus = ConnectionStatus.CONNECTING;

        service.submit(() -> {
            try {
                socket = new Socket(ip, port);

                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());

                dos.writeUTF(InetAddress.getLocalHost().getHostName());
                dos.writeUTF(ServerUtils.getIP());

                Authentication authentication = new Authentication(dis, dos, password);

                if (authentication.tryToConnect() != Verification.CORRECT) {
                    connectionStatus = ConnectionStatus.WRONG_PASSWORD;
                } else {
                    connectionStatus = ConnectionStatus.CONNECTED;
                    remoteScreen.exchangeData(dis, dos);
                    Thread.sleep(2000);
                    Platform.runLater(() -> SceneController.setRoot("remoteScreen", EffectType.EASE_IN));
                }

            } catch (UnknownHostException e) {
                connectionStatus = ConnectionStatus.UNKNOWN_HOST;
            } catch (IOException | InterruptedException e) {
                connectionStatus = ConnectionStatus.TIME_OUT;
            }
        });

        listener.submit(() -> {
            while (!stopWasRequested) {
                try {
                    Thread.sleep(1000);

                    if (connectionStatus == ConnectionStatus.CONNECTED && remoteScreen.isStopped()) {
                        Platform.runLater(() -> SceneController.setRoot("connectScene", EffectType.EASE_OUT));
                        connectionStatus = ConnectionStatus.DROPPED_CONNECTION;
                        remoteScreen.shutDown();
                    }

                    fireGUIChangeEvent();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addListener(EventsChangeListener listener) {
        listeners.add(listener);
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void shutDown() {
        stopWasRequested = true;
        removeListeners();

//        try {
//            if (socket != null) {
//                socket.close();
//            }
//
//            if (dis != null) {
//                dis.close();
//            }
//
//            if (dos != null) {
//                dos.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        listener.shutdown();
        service.shutdown();
    }

}
