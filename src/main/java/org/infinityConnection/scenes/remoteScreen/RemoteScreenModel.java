package org.infinityConnection.scenes.remoteScreen;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import org.infinityConnection.scenes.client.ReceiveScreen;
import org.infinityConnection.scenes.client.SendEvents;
import org.infinityConnection.utils.EventsChangeListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoteScreenModel {

    private DataInputStream dis;
    private DataOutputStream dos;

    private String hostName = "Undefined";

    private Timer timer;
    private int seconds = 0;

    private boolean stopWasRequested = false;
    private ExecutorService service = Executors.newCachedThreadPool();
    private final List<EventsChangeListener> listeners = new ArrayList<>();

    private final ReceiveScreen receiveScreen = new ReceiveScreen();
    private final SendEvents sendEvents = new SendEvents();

    private TimerTask getTask() {
        return new TimerTask() {
            @Override
            public void run() {
                seconds++;
            }
        };
    }

    private void fireGUIChangeEvent() {
        for (EventsChangeListener listener : listeners) {
            listener.onReadingChange();
        }
    }

    private void removeAutoClosableEvents() {
        for (EventsChangeListener listener : listeners) {
            if (listener.isAutoCloasable()) {
                Platform.runLater(() -> listeners.remove(listener));
            }
        }
    }

    public void removeListeners() {
        listeners.clear();
    }

    public void start(DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;

        stopWasRequested = false;
        service = Executors.newCachedThreadPool();

        hostName = "Undefined";
        timer = new Timer();
        seconds = 0;

        service.submit(() -> {
            try {
                double ssWidth = dis.readDouble();
                double ssHeight = dis.readDouble();

                hostName = dis.readUTF();

                receiveScreen.start(dis);
                sendEvents.setComponents(dos, ssWidth, ssHeight);

                timer.scheduleAtFixedRate(getTask(), 1000, 1000);

                while (!stopWasRequested) {
                    Thread.sleep(40);
                    fireGUIChangeEvent();
                    removeAutoClosableEvents();
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void addListener(EventsChangeListener listener) {
        listeners.add(listener);
    }

    public String getHostName() {
        return "C O N N E C T E D  T O : " + hostName;
    }

    public String getSessionTime() {
        int s = seconds % 60;
        int m = seconds / 60;
        int h = m / 60;

        return h + " : " + m + " : " + s;
    }

    public EventHandler getMouseMovedEH(double iwFitWidth, double iwFitHeight) {
        return sendEvents.getMouseMovedEH(iwFitWidth, iwFitHeight);
    }

    public EventHandler getMousePressedEH() {
        return sendEvents.getMousePressedEH();
    }

    public EventHandler getMouseReleasedEH() {
        return sendEvents.getMouseReleasedEH();
    }

    public EventHandler getKeyPressedEH() {
        return sendEvents.getKeyPressedEH();
    }

    public EventHandler getKeyReleasedEH() {
        return sendEvents.getKeyReleasedEH();
    }

    public Image getReceivedImage() {
        return receiveScreen.getReceivedImage();
    }

    public boolean isStopped() {
        return receiveScreen.isStopped();
    }

    public void shutDown() {
        stopWasRequested = true;
        removeListeners();

        if (timer != null) {
            timer.cancel();
        }

        try {
            if (dis != null) {
                dis.close();
            }

            if (dos != null) {
                dos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        receiveScreen.shutDown();
        service.shutdown();
    }

}