package org.infinityConnection.scenes.remoteScreen;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.infinityConnection.EffectType;
import org.infinityConnection.SceneController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RemoteScreenController {

    @FXML
    private Label host;

    @FXML
    private Label timer;

    @FXML
    private ImageView iw;

    private RemoteScreenModel model;

    public void exchangeData(DataInputStream dis, DataOutputStream dos) {
        model = new RemoteScreenModel(dis, dos);
        model.setGUIElements(host, timer, iw);
        model.start();

        Stage stage = (Stage) SceneController.scene.getWindow();
        stage.setOnCloseRequest((e) -> {
            try {
                model.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public void onDisconnect() throws IOException {
        model.close();
        Platform.runLater(() -> SceneController.setRoot("mainScene", EffectType.EASE_OUT));
    }
}
