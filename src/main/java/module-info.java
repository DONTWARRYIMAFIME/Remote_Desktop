module org.remoteDesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.remoteDesktop.remoteDesktopScene to javafx.fxml;
    opens org.remoteDesktop.mainScene to javafx.fxml;

    exports org.remoteDesktop.server;
    exports org.remoteDesktop.mainScene;
    exports org.remoteDesktop.remoteDesktopScene;
    exports org.remoteDesktop;

}