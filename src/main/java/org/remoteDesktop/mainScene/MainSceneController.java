package org.remoteDesktop.mainScene;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.remoteDesktop.mainScene.MainSceneModel;

public class MainSceneController {

    private final MainSceneModel mainSceneModel = new MainSceneModel();

    @FXML
    private TextField tfMyIP;

    @FXML
    private TextField tfMyPassword;

    @FXML
    private TextField tfOtherIP;

    @FXML
    private TextField tfOtherPassword;

    public void initialize() {
        tfMyIP.setText(mainSceneModel.getServerIP());
        tfMyPassword.setText(mainSceneModel.getServerPassword());
    }

    public void connectToServer() {
        //model.becomeClient();
        mainSceneModel.connectToServer(tfOtherIP.getText(), 8001, tfOtherPassword.getText());
    }



}
