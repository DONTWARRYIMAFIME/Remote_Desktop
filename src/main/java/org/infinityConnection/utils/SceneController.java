package org.infinityConnection.utils;

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
import static javafx.animation.Interpolator.EASE_OUT;

public class SceneController {
    public static Scene scene;
    public static StackPane rootContainer;
    public static StackPane childContainer;
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

    public static void setChild(String name, EffectType effect) {
        changeScene(name, effect, childContainer);
    }

    public static void setRoot(String name, EffectType effect) {
        changeScene(name, effect, rootContainer);
    }

    private static void changeScene(String name, EffectType effect, StackPane container) {
        Pane root = getParent(name);

        if (container.getChildren().contains(root)) {
            return;
        }

        root.setMaxWidth(container.getMaxWidth());
        root.setMaxHeight(container.getMaxHeight());

        switch (effect) {
            case EASE_IN -> {
                root.translateXProperty().set(scene.getWidth());
                horizontal(root, container);
            }
            case EASE_OUT -> {
                root.translateXProperty().set(-scene.getWidth());
                horizontal(root, container);
            }
            case POP_UP -> {
                root.translateYProperty().set(scene.getHeight());
                vertical(root, container);
            }
            case POP_DOWN -> {
                root.translateYProperty().set(-scene.getHeight());
                vertical(root, container);
            }
            case NULL -> {
                container.getChildren().add(root);
                removeParent(container);
            }
        }
    }

    private static void horizontal(Parent root, StackPane container) {
        container.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished((e) -> removeParent(container));
        timeline.play();
    }

    private static void vertical(Parent root, StackPane container) {
        container.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0, EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished((e) -> removeParent(container));
        timeline.play();
    }

    private static void removeParent(StackPane container) {
        if (container.getChildren().size() > 1) {
            container.getChildren().remove(0);
        }
    }

    public static Pane getParent(String name) {
        return parents.get(name);
    }
    public static FXMLLoader getFXMLLoader(String name) {
        return loaders.get(name);
    }
}