package org.infinityConnection.scenes.main;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class MainSceneController {

    private final MainSceneModel model = new MainSceneModel();

    @FXML
    private TextField tfMyIP;

    @FXML
    private TextField tfMyPassword;

    @FXML
    private TextField tfOtherIP;

    @FXML
    private TextField tfOtherPassword;

    public void startServer() {
        model.startServer();
    }

    public void init() {
        tfMyIP.setText(model.getServerIP());
        tfMyPassword.setText(model.getServerPassword());
    }

    public void connectToServer() {
        model.connectToServer(tfOtherIP.getText(), 8001, tfOtherPassword.getText());
    }


}
