package org.remoteDesktop.client;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.remoteDesktop.App;
import org.remoteDesktop.ConnectionStatus;
import org.remoteDesktop.Loader;
import org.remoteDesktop.Verification;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {

    private final String ip;
    private final int port;
    private final String password;

    private final Label status;
    private final ImageView iw;

    private ConnectionStatus connectionStatus = ConnectionStatus.UNKNOWN;

    private void onResize() {
        iw.setFitWidth(iw.getScene().getWidth());
        iw.setFitHeight(iw.getScene().getHeight());
    }

    public Client(String ip, int port, String password, Label status, ImageView iw) {
        this.ip = ip;
        this.port = port;
        this.password = password;

        this.status = status;
        this.iw = iw;

        setDaemon(true);
        start();
    }

    @Override
    public void run() {

        try {

            Socket socket = new Socket(ip, port);

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            Authentication authentication = new Authentication(dis, dos, password);

            if (authentication.tryToConnect() != Verification.CORRECT) {
                connectionStatus = ConnectionStatus.WRONG_PASSWORD;
            } else {
                connectionStatus = ConnectionStatus.CONNECTED;

                status.setVisible(false);

                double ssWidth = dis.readDouble();
                double ssHeight = dis.readDouble();

                Closeable thread  = new ReceiveScreen(dis, iw);
                new SendEvents(dos, iw, ssWidth, ssHeight);

                iw.setImage(null);

                iw.setFitWidth(iw.getScene().getWidth());
                iw.setFitHeight(iw.getScene().getHeight());

                Platform.runLater(() -> {
                    Stage stage = (Stage) iw.getScene().getWindow();
                    stage.widthProperty().addListener((obs, oldVal, newVal) -> onResize());
                    stage.heightProperty().addListener((obs, oldVal, newVal) -> onResize());

                    stage.setOnCloseRequest((event) -> {
                        try {
                            thread.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });

            }

        } catch (UnknownHostException e) {
            connectionStatus = ConnectionStatus.UNKNOWN_HOST;
        } catch (IOException e) {
            connectionStatus = ConnectionStatus.TIME_OUT;
        }

        if (connectionStatus != ConnectionStatus.CONNECTED) {
            iw.setImage(Loader.loadImage("sad_smile.png"));

            Platform.runLater(() -> status.setText("Ooops...an error occured :\n" + connectionStatus.getStatusName()));

            try {
                Thread.sleep(5000);
                App.setRoot("mainScene");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
