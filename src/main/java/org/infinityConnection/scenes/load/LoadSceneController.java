package org.infinityConnection.scenes.load;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.infinityConnection.scenes.container.ContainerScene;

public class LoadSceneController {

    @FXML
    private Pane pane;

    public void initialize() {

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), pane);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);

        fadeOut.setDelay(Duration.seconds(1));
        fadeOut.play();

        fadeOut.setOnFinished((e) -> new ContainerScene());

    }

}
