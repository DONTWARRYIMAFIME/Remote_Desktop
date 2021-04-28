package org.infinityConnection.scenes.remoteScreen;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.infinityConnection.EffectType;
import org.infinityConnection.SceneController;
import org.infinityConnection.client.ReceiveScreen;
import org.infinityConnection.client.SendEvents;

import javax.swing.event.HyperlinkEvent;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RemoteScreenModel extends Thread implements Closeable {

    private final DataInputStream dis;
    private final DataOutputStream dos;

    private Label host;
    private Label lbTimer;
    private ImageView iw;

    private final ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> onResize();
    private Closeable receiveScreen;

    private final Timer timer = new Timer();
    private int secondsFromConnection = 0;

    private TimerTask getTask() {
        return new TimerTask() {
            @Override
            public void run() {
                secondsFromConnection++;

                int seconds = secondsFromConnection % 60;
                int minutes = secondsFromConnection / 60;
                int hours = minutes / 60;

                Platform.runLater(() -> lbTimer.setText(hours + " : " + minutes + " : " + seconds));
            }
        };
    }

    private void onResize() {
        iw.setFitWidth(iw.getScene().getWidth());
        iw.setFitHeight(iw.getScene().getHeight() - 40);
    }

    public RemoteScreenModel(DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;

        setDaemon(true);
    }

    public void setGUIElements(Label host, Label lbTimer, ImageView iw) {
        this.host = host;
        this.lbTimer = lbTimer;
        this.iw = iw;
    }

    @Override
    public void run() {
        try {
            double ssWidth = dis.readDouble();
            double ssHeight = dis.readDouble();

            String hostName = dis.readUTF();

            receiveScreen = new ReceiveScreen(dis, iw);
            new SendEvents(dos, iw, ssWidth, ssHeight);

            iw.setImage(null);

            iw.setFitWidth(iw.getScene().getWidth());
            iw.setFitHeight(iw.getScene().getHeight() - 40);

            timer.scheduleAtFixedRate(getTask(), 1000, 1000);

            Platform.runLater(() -> {
                host.setText("C O N N E C T E D  T O : " + hostName);

                Stage stage = (Stage) iw.getScene().getWindow();

                stage.widthProperty().addListener(stageSizeListener);
                stage.heightProperty().addListener(stageSizeListener);

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        Stage stage = (Stage) SceneController.scene.getWindow();
        stage.widthProperty().removeListener(stageSizeListener);
        stage.heightProperty().removeListener(stageSizeListener);

        timer.cancel();

        receiveScreen.close();
        dis.close();
        dos.close();

        interrupt();
    }
}
