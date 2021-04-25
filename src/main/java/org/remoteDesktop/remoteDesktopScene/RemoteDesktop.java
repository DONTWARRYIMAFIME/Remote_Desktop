package org.remoteDesktop.remoteDesktopScene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.remoteDesktop.App;
import org.remoteDesktop.Loader;

import java.io.IOException;

public class RemoteDesktop {

    public RemoteDesktop(String ip, int port, String password) throws IOException {

        FXMLLoader loader = App.getFXMLLoader("remoteDesktopScene");
        RemoteDesktopSceneController controller = loader.getController();

        App.setRoot("remoteDesktopScene");

        //controller.setStage(stage);
        controller.establishConnection(ip, port, password);



    }

}
