package org.infinityConnection.scenes.connect;

import javafx.animation.StrokeTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import org.infinityConnection.*;
import org.infinityConnection.client.Authentication;
import org.infinityConnection.scenes.remoteScreen.RemoteScreen;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ConnectSceneModel extends Thread implements Closeable {

    private final String ip;
    private final int port;
    private final String password;

    private Label status;
    private final List<Circle> circles = new ArrayList<>();
    private final List<Paint> normalColors = new ArrayList<>();

    private final List<Closeable> threads = new ArrayList<>();

    private ConnectionStatus connectionStatus = ConnectionStatus.UNKNOWN;

    private void initNormalColors() {
        for (Circle circle : circles) {
            normalColors.add(circle.getStroke());
        }
    }

    private void setErrorColors() {
        for (Circle circle : circles) {
            StrokeTransition strokeTransition = new StrokeTransition(Duration.seconds(1), circle);
            strokeTransition.setToValue(Color.RED);
            strokeTransition.play();
        }
    }

    private void setNormalColors() {
        for (int i = 0; i < circles.size(); i++) {
            circles.get(i).setStroke(normalColors.get(i));
        }
    }


    public ConnectSceneModel(String ip, int port, String password) {
        this.ip = ip;
        this.port = port;
        this.password = password;

        setDaemon(true);
    }

    public void setGUIElements(Circle circle1, Circle circle2, Circle circle3, Label status) {
        circles.add(circle1);
        circles.add(circle2);
        circles.add(circle3);

        this.status = status;

        initNormalColors();
    }

    @Override
    public void run() {

        try {

            Socket socket = new Socket(ip, port);

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            threads.add(socket);
            threads.add(dis);
            threads.add(dos);

            Authentication authentication = new Authentication(dis, dos, password);

            if (authentication.tryToConnect() != Verification.CORRECT) {
                connectionStatus = ConnectionStatus.WRONG_PASSWORD;
            } else {
                connectionStatus = ConnectionStatus.CONNECTED;
                Platform.runLater(() -> new RemoteScreen(dis, dos));
            }

        } catch (UnknownHostException e) {
            connectionStatus = ConnectionStatus.UNKNOWN_HOST;
        } catch (IOException e) {
            connectionStatus = ConnectionStatus.TIME_OUT;
        }

        if (connectionStatus != ConnectionStatus.CONNECTED) {
            Platform.runLater(() -> {
                setErrorColors();
                status.setText("Ops...an error occurred :\n" + connectionStatus.getStatusName());
            });

            try {
                Thread.sleep(5000);
                close();

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void close() throws IOException {
        Platform.runLater(() -> SceneController.setRoot("mainScene", EffectType.EASE_OUT));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setNormalColors();

        for (Closeable thread : threads) {
            thread.close();
        }

        interrupt();
    }

}
