package org.remoteDesktop.remoteDesktopScene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.remoteDesktop.Loader;

import java.io.IOException;
import java.net.Socket;

public class RemoteDesktop {

    public RemoteDesktop(Socket socket, String password) throws IOException {
        Stage stage = new Stage();

        String title = "Free Remote Control | version 1.0";

        FXMLLoader loader = Loader.loadFXML("remoteDesktopScene");

        Scene scene = new Scene(loader.load());
        RemoteDesktopSceneController controller = loader.getController();

        stage.setTitle(title);
        stage.getIcons().add(Loader.loadImage("icon.png"));
        stage.setScene(scene);

        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());

        stage.setScene(scene);
        stage.show();

        controller.setStage(stage);
        controller.establishConnection(socket, password);
    }

}
