package org.remoteDesktop;

import javafx.util.Duration;
import org.trayNotification.animations.Animations;
import org.trayNotification.notification.Notification;
import org.trayNotification.notification.Notifications;
import org.trayNotification.notification.TrayNotification;

public class NotificationsController {
    public static void showNewUserNotification() {
        String title = "Free Remote Desktop";
        String message = "New user was connected";
        Notification notification = Notifications.SUCCESS;

        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotification(notification);
        tray.setAnimation(Animations.POPUP);
        tray.showAndDismiss(Duration.seconds(2));
    }

    public static void showUserDisconnected() {
        String title = "Free Remote Desktop";
        String message = "User disconnected";
        Notification notification = Notifications.NOTICE;

        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotification(notification);
        tray.setAnimation(Animations.POPUP);
        tray.showAndDismiss(Duration.seconds(2));
    }
}
