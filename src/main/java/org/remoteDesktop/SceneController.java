package org.remoteDesktop;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static javafx.animation.Interpolator.EASE_IN;

public class SceneController {
    public static Scene scene;
    public static StackPane rootContainer;
    private static final Map<String, Pane> parents = new HashMap<>();
    private static final Map<String, FXMLLoader> loaders = new HashMap<>();

    public static void addScene(String name, FXMLLoader loader) {
        try {
            loaders.put(name, loader);
            parents.put(name, loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setRoot(String name, EffectType effect) {
        Pane root = getParent(name);
        root.setMaxWidth(rootContainer.getMaxWidth());
        root.setMaxHeight(rootContainer.getMaxHeight());

        switch (effect) {
            case EASE_IN -> {
                root.translateXProperty().set(scene.getWidth());
                horizontal(root);
            }
            case EASE_OUT -> {
                root.translateXProperty().set(-scene.getWidth());
                horizontal(root);
            }
            case NULL -> {
                rootContainer.getChildren().add(root);
                removeParent();
            }
        }


    }

    private static void horizontal(Parent root) {
        rootContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished((e) -> removeParent());
        timeline.play();
    }

    private static void removeParent() {
        if (rootContainer.getChildren().size() > 1) {
            rootContainer.getChildren().remove(0);
        }
    }

    public static Pane getParent(String name) {
        return parents.get(name);
    }
    public static FXMLLoader getFXMLLoader(String name) {
        return loaders.get(name);
    }
}