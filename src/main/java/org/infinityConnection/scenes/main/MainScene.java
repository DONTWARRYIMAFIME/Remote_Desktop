package org.infinityConnection.scenes.main;

import javafx.fxml.FXMLLoader;
import org.infinityConnection.EffectType;
import org.infinityConnection.SceneController;

public class MainScene {
    public MainScene() {
        FXMLLoader loader = SceneController.getFXMLLoader("mainScene");
        MainSceneController controller = loader.getController();

        SceneController.setRoot("mainScene", EffectType.NULL);

        controller.startServer();
        controller.init();
    }
}
