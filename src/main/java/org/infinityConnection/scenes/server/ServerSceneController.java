package org.infinityConnection.scenes.server;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.infinityConnection.utils.EventsChangeListener;
import org.infinityConnection.utils.NotificationsController;
import org.infinityConnection.utils.SceneController;

public class ServerSceneController {

    @FXML
    private JFXTextField tfIP;

    @FXML
    private JFXTextField tfPassword;

    @FXML
    private JFXToggleButton toggleButton;

    private int oldClientsCount = 0;
    private final Server server = new Server(8001);
    private Stage stage;

    private EventsChangeListener updateClientsNotifications() {
        return new EventsChangeListener() {
            @Override
            public void onReadingChange() {

                int newClientsCount = server.getClientsCount();

                if (oldClientsCount < newClientsCount) {
                    Platform.runLater(NotificationsController::showNewUserNotification);
                    oldClientsCount = newClientsCount;

                    System.out.format("Clients count :  %d\n", oldClientsCount);
                } else if (oldClientsCount > newClientsCount) {
                    Platform.runLater(NotificationsController::showUserDisconnected);
                    oldClientsCount = newClientsCount;

                    System.out.format("Clients count :  %d\n", oldClientsCount);
                }
            }

            @Override
            public boolean isAutoCloasable() {
                return false;
            }
        };
    }

    private void closeWindowEvent(WindowEvent event) {
        server.shutDown();
    }

    private void turnOnServer() {
        stage = (Stage) SceneController.scene.getWindow();
        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

        server.addListener(updateClientsNotifications());
        server.start();
    }

    public void initialize() {
        tfIP.setText(server.getIP());
        tfPassword.setText(server.getServerPassword());
    }

    public void onToggle() {
        if (toggleButton.isSelected()) {
            turnOnServer();
        } else {
            server.shutDown();
        }
    }


}
