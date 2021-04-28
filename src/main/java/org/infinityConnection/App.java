package org.infinityConnection;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        String title = "Infinity connection | version 1.0";
        stage.setTitle(title);
        stage.getIcons().add(Loader.loadImage("images/icon.png"));

        SceneController.addScene("loadScene", Loader.loadFXML("scenes/load"));
        SceneController.addScene("mainScene", Loader.loadFXML("scenes/main"));
        SceneController.addScene("connectScene", Loader.loadFXML("scenes/connect"));
        SceneController.addScene("remoteScreen", Loader.loadFXML("scenes/remoteScreen"));

        StackPane stackPane = new StackPane();
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