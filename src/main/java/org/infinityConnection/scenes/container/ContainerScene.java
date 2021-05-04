package org.infinityConnection.scenes.container;

import javafx.fxml.FXMLLoader;
import org.infinityConnection.utils.EffectType;
import org.infinityConnection.utils.SceneController;

public class ContainerScene {

    public ContainerScene() {
        String fxml = "containerScene";

        FXMLLoader loader = SceneController.getFXMLLoader(fxml);
        ContainerSceneController controller = loader.getController();

        SceneController.setRoot(fxml, EffectType.NULL);
    }

}
