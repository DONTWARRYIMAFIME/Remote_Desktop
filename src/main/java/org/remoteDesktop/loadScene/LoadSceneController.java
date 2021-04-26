package org.remoteDesktop.loadScene;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.remoteDesktop.EffectType;
import org.remoteDesktop.SceneController;

public class LoadSceneController {

    @FXML
    private Pane pane;

    public void initialize() {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), pane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), pane);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);

        fadeIn.play();
        fadeIn.setOnFinished((e) -> fadeOut.play());

        fadeOut.setOnFinished((e) -> SceneController.setRoot("mainScene", EffectType.NULL));
    }

}
