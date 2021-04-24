module org.remoteDesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.remoteDesktop.mainScene to javafx.fxml;
    opens org.remoteDesktop.remoteDesktopScene to javafx.fxml;
    opens org.remoteDesktop to javafx.fxml;

    exports org.remoteDesktop.mainScene;
    exports org.remoteDesktop.remoteDesktopScene;
    exports org.remoteDesktop;
    exports org.remoteDesktop.server;
    opens org.remoteDesktop.server to javafx.fxml;
}