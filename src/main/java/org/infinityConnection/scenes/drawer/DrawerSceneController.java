package org.infinityConnection.scenes.drawer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import org.infinityConnection.utils.EffectType;
import org.infinityConnection.utils.SceneController;

public class DrawerSceneController {

    @FXML
    private JFXToggleNode btnInformation;

    @FXML
    private JFXToggleNode btnClient;

    @FXML
    private JFXToggleNode btnServer;

    @FXML
    private JFXButton btnExit;

    @FXML
    private ToggleGroup toolbarButton;

    public void initialize() {
        toolbarButton.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
    }

    public void onClient() {
        SceneController.setChild("clientScene", EffectType.POP_UP);
    }

    public void onServer() {
        SceneController.setChild("serverScene", EffectType.POP_UP);
    }

    public void onExit() {
        Platform.exit();
    }

}
