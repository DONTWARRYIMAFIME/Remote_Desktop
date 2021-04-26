package org.remoteDesktop;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        String title = "Free Remote Control | version 1.0";
        stage.setTitle(title);
        stage.getIcons().add(Loader.loadImage("icon.png"));

        SceneController.addScene("loadScene", Loader.loadFXML("loadScene"));
        SceneController.addScene("mainScene", Loader.loadFXML("mainScene"));
        SceneController.addScene("remoteDesktopScene", Loader.loadFXML("remoteDesktopScene"));

        StackPane stackPane = new StackPane();
        stackPane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        SceneController.scene = scene;
        SceneController.rootContainer = (StackPane) scene.getRoot();

        SceneController.setRoot("loadScene", EffectType.NULL);

        stage.show();

        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());


    }

    public static void main(String[] args) {
        launch();
    }

}