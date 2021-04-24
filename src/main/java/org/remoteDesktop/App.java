package org.remoteDesktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        String title = "Free Remote Control | version 1.0";
        Scene scene = new Scene(Loader.loadFXML("mainScene").load());

        stage.setTitle(title);
        stage.getIcons().add(Loader.loadImage("icon.png"));
        stage.setScene(scene);

        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());

        stage.show();
    }

}