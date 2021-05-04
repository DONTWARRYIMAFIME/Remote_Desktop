package org.infinityConnection.scenes.drawer;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class DrawerSceneController {

    @FXML
    private JFXButton btnInformation;

    @FXML
    private JFXButton btnClient;

    @FXML
    private JFXButton btnServer;

    @FXML
    private JFXButton btnExit;

    public void onExit() {
        Platform.exit();
    }

}
