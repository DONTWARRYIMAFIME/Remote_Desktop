package org.infinityConnection.scenes.main;

import javafx.fxml.FXMLLoader;
import org.infinityConnection.utils.EffectType;
import org.infinityConnection.utils.SceneController;

public class MainScene {
    public MainScene() {
        FXMLLoader loader = SceneController.getFXMLLoader("mainScene");
        MainSceneController controller = loader.getController();

        SceneController.setRoot("mainScene", EffectType.NULL);

        controller.startServer();
        controller.init();
    }
}
