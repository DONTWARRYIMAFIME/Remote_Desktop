package org.infinityConnection;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.infinityConnection.utils.EffectType;
import org.infinityConnection.utils.SceneController;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        String title = "Infinity connection | version 1.0";
        stage.setTitle(title);
        stage.getIcons().add(Loader.loadImage("images/icon.png"));

        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        SceneController.scene = scene;
        SceneController.rootContainer = (StackPane) scene.getRoot();

        SceneController.addScene("remoteScreen", Loader.loadFXML("scenes/remoteScreen"));
        SceneController.addScene("connectScene", Loader.loadFXML("scenes/connect"));
        SceneController.addScene("clientScene", Loader.loadFXML("scenes/client"));
        SceneController.addScene("drawerScene", Loader.loadFXML("scenes/drawer"));
        SceneController.addScene("containerScene", Loader.loadFXML("scenes/container"));

        SceneController.childContainer = (StackPane) SceneController.getParent("containerScene").getChildren().get(3);

        SceneController.addScene("serverScene", Loader.loadFXML("scenes/server"));
        SceneController.addScene("membersScene", Loader.loadFXML("scenes/members"));
        SceneController.addScene("loadScene", Loader.loadFXML("scenes/load"));

        SceneController.setRoot("loadScene", EffectType.NULL);

        stage.show();

        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());


    }

    public static void main(String[] args) {
        launch();
    }

}