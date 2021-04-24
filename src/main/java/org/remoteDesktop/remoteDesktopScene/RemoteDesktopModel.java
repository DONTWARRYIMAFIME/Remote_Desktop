package org.remoteDesktop.remoteDesktopScene;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.remoteDesktop.Verification;
import org.remoteDesktop.client.Authentication;
import org.remoteDesktop.client.ReceiveScreen;
import org.remoteDesktop.client.SendEvents;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RemoteDesktopModel {

    private Stage stage;

    private final ImageView iw;
    private final Label status;
    private final List<Closeable> threads = new ArrayList<>();

    private void setCloseAllThreadsEvent(Stage stage) {
        stage.setOnCloseRequest((event) -> {
            for (Closeable thread : threads) {
                try {
                    thread.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onResize() {
        iw.setFitWidth(stage.getScene().getWidth());
        iw.setFitHeight(stage.getScene().getHeight());
    }

    public RemoteDesktopModel(ImageView iw, Label status) {
        this.iw = iw;
        this.status = status;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.widthProperty().addListener((obs, oldVal, newVal) -> onResize());
        stage.heightProperty().addListener((obs, oldVal, newVal) -> onResize());
        setCloseAllThreadsEvent(stage);
    }

    public void establishConnection(Socket socket, String password) {

        try {

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String statusMsg = "Connecting to the server...";
            System.out.println(statusMsg);
            status.setText(statusMsg);

            Authentication authentication = new Authentication(dis, dos, password);

            if (authentication.tryToConnect() != Verification.CORRECT) {
                statusMsg = "Authentication problem. Incorrect password";
                System.out.println(statusMsg);
                status.setText(statusMsg);

                stage.close();
                return;
            }

            double ssWidth = dis.readDouble();
            double ssHeight = dis.readDouble();

            statusMsg = "Connected successfully!";
            System.out.println(statusMsg);
            status.setText(statusMsg);

            status.setVisible(false);

            stage.setWidth(1280);
            stage.setHeight(720);

            threads.add(new ReceiveScreen(dis, iw));
            new SendEvents(dos, iw, ssWidth, ssHeight);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
