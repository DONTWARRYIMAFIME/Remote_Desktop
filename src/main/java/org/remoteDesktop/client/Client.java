package org.remoteDesktop.client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.remoteDesktop.*;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread implements Closeable {

    private ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> onResize();

    private final String ip;
    private final int port;
    private final String password;

    private Label status;
    private ImageView iw;
    private ToolBar toolBar;
    private HBox hbox;
    private Label serverIP;

    private final List<Closeable> threads = new ArrayList<>();

    private ConnectionStatus connectionStatus = ConnectionStatus.UNKNOWN;

    private void resetGUIElements() {
        iw.setFitWidth(200);
        iw.setFitHeight(150);
        toolBar.setVisible(false);
        status.setVisible(true);
    }

    private void onResize() {
        hbox.setPrefWidth(iw.getScene().getWidth() - 20);
        iw.setFitWidth(iw.getScene().getWidth());
        iw.setFitHeight(iw.getScene().getHeight() - 64);

    }

    public Client(String ip, int port, String password) {
        this.ip = ip;
        this.port = port;
        this.password = password;

        setDaemon(true);
    }

    public void setGUIElements(Label status, ImageView iw, ToolBar toolBar, Label serverIP) {
        this.status = status;
        this.iw = iw;
        this.toolBar = toolBar;
        this.serverIP = serverIP;

        hbox = (HBox) toolBar.getItems().get(0);
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

                toolBar.setVisible(true);
                status.setVisible(false);

                double ssWidth = dis.readDouble();
                double ssHeight = dis.readDouble();

                threads.add(new ReceiveScreen(dis, iw));
                new SendEvents(dos, iw, ssWidth, ssHeight);

                iw.setImage(null);

                hbox.setPrefWidth(iw.getScene().getWidth() - 20);
                iw.setFitWidth(iw.getScene().getWidth());
                iw.setFitHeight(iw.getScene().getHeight() - 64);

                Platform.runLater(() -> {
                    serverIP.setText("Connected to : " + socket.getLocalAddress().getHostAddress());

                    Stage stage = (Stage) iw.getScene().getWindow();

                    stage.widthProperty().addListener(stageSizeListener);
                    stage.heightProperty().addListener(stageSizeListener);

                    stage.setOnCloseRequest((event) -> {
                        try {
                            close();
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

            Platform.runLater(() -> status.setText("Ops...an error occurred :\n" + connectionStatus.getStatusName()));

            try {
                Thread.sleep(5000);
                resetGUIElements();
                close();
                Platform.runLater(() -> SceneController.setRoot("mainScene", EffectType.EASE_OUT));
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void close() throws IOException {
        Stage stage = (Stage) iw.getScene().getWindow();
        stage.widthProperty().removeListener(stageSizeListener);
        stage.heightProperty().removeListener(stageSizeListener);

        for (Closeable thread : threads) {
            thread.close();
        }

        interrupt();
    }

    public void onDisconnect() throws IOException {
        resetGUIElements();
        close();
        Platform.runLater(() -> SceneController.setRoot("mainScene", EffectType.EASE_OUT));
    }
}
