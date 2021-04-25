package org.remoteDesktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.remoteDesktop.mainScene.MainScene;
import org.remoteDesktop.mainScene.MainSceneController;
import org.remoteDesktop.remoteDesktopScene.RemoteDesktopSceneController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    private static Scene scene;
    private static Map<String, Parent> parents = new HashMap<>();
    private static Map<String, FXMLLoader> loaders = new HashMap<>();

    @Override
    public void start(Stage stage) throws IOException {
        String title = "Free Remote Control | version 1.0";
        stage.setTitle(title);
        stage.getIcons().add(Loader.loadImage("icon.png"));

        addScene("mainScene", Loader.loadFXML("mainScene"));
        addScene("remoteDesktopScene", Loader.loadFXML("remoteDesktopScene"));

        scene = new Scene(getParent("mainScene"));
        stage.setScene(scene);

        stage.show();

        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }

    public static void addScene(String name, FXMLLoader loader) throws IOException {
        loaders.put(name, loader);
        parents.put(name, loader.load());
    }

    public static void setRoot(String name) {
        scene.setRoot(parents.get(name));
    }

    public static Parent getParent(String name) {
        return parents.get(name);
    }
    public static FXMLLoader getFXMLLoader(String name) {
        return loaders.get(name);
    }

    public static void main(String[] args) {
        launch();
    }

}