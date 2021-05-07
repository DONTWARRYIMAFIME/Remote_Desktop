package org.infinityConnection.scenes.connect;

import javafx.application.Platform;
import org.infinityConnection.utils.ConnectionStatus;
import org.infinityConnection.scenes.client.Verification;
import org.infinityConnection.scenes.client.Authentication;
import org.infinityConnection.utils.EventsChangeListener;
import org.infinityConnection.scenes.remoteScreen.RemoteScreen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectSceneModel {

    private ConnectionStatus connectionStatus = ConnectionStatus.UNKNOWN;
    private RemoteScreen remoteScreen;

    private boolean stopWasRequested = false;
    private final ExecutorService service = Executors.newCachedThreadPool();
    private final ExecutorService listener = Executors.newCachedThreadPool();
    private final List<EventsChangeListener> listeners = new ArrayList<>();

    private void fireGUIChangeEvent() {
        for (EventsChangeListener listener : listeners) {
            listener.onReadingChange();
            if (listener.isAutoCloasable()) {
                listeners.remove(listener);
            }
        }
    }

    public ConnectSceneModel(String ip, int port, String password) {

        service.submit(() -> {
            try {
                connectionStatus = ConnectionStatus.CONNECTING;

                Socket socket = new Socket(ip, port);

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                Authentication authentication = new Authentication(dis, dos, password);

                if (authentication.tryToConnect() != Verification.CORRECT) {
                    connectionStatus = ConnectionStatus.WRONG_PASSWORD;
                } else {
                    connectionStatus = ConnectionStatus.CONNECTED;
                    Thread.sleep(2000);
                    Platform.runLater(() -> {
                        remoteScreen = new RemoteScreen();
                        remoteScreen.exchangeData(dis, dos);
                    });
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fireGUIChangeEvent();
                if (remoteScreen != null) {
                    connectionStatus = remoteScreen.getConnectionStatus();
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
        listener.shutdown();
        service.shutdown();
    }

}
