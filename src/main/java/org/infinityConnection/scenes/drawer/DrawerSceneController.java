package org.infinityConnection.scenes.drawer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import org.infinityConnection.utils.EffectType;
import org.infinityConnection.utils.SceneController;

import java.util.ArrayList;
import java.util.List;

public class DrawerSceneController {

    @FXML
    private JFXToggleNode btnClient;

    @FXML
    private JFXToggleNode btnServer;

    @FXML
    private JFXToggleNode btnMembers;

    @FXML
    private ToggleGroup toolbarButton;

    private int lastId = 0;
    private final List<JFXToggleNode> buttons = new ArrayList<>();

    public void initialize() {
        toolbarButton.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });

        buttons.add(btnClient);
        buttons.add(btnServer);
        buttons.add(btnMembers);

        btnClient.setOnAction((e) -> onClient(calculateEffect()));
        btnServer.setOnAction((e) -> onServer(calculateEffect()));
        btnMembers.setOnAction((e) -> onMembers(calculateEffect()));
    }

    private EffectType calculateEffect() {
        JFXToggleNode currentButton = (JFXToggleNode) toolbarButton.getSelectedToggle();

        int newId = buttons.indexOf(currentButton);

        EffectType effectType = lastId < newId ? EffectType.POP_UP : EffectType.POP_DOWN;
        lastId = newId;
        return effectType;
    }

    public void onClient(EffectType effectType) {
        SceneController.setChild("clientScene", effectType);
    }

    public void onServer(EffectType effectType) {
        SceneController.setChild("serverScene", effectType);
    }

    public void onMembers(EffectType effectType) {
        SceneController.setChild("membersScene", effectType);
    }

    public void onExit() {
        Platform.exit();
    }

}
