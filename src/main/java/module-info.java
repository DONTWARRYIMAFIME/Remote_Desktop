module org.infinityConnection {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.jfoenix;
    requires org.trayNotification;

    opens org.infinityConnection.scenes.load to javafx.fxml;
    opens org.infinityConnection.scenes.client to javafx.fxml;
    opens org.infinityConnection.scenes.server to javafx.fxml;
    opens org.infinityConnection.scenes.members to javafx.fxml;
    opens org.infinityConnection.scenes.drawer to javafx.fxml;
    opens org.infinityConnection.scenes.container to javafx.fxml;
    opens org.infinityConnection.scenes.connect to javafx.fxml;
    opens org.infinityConnection.scenes.remoteScreen to javafx.fxml;

    exports org.infinityConnection;

}