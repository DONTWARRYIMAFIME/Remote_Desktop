package org.infinityConnection.scenes.remoteScreen;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import org.infinityConnection.client.SendEvents;
import org.infinityConnection.utils.ConnectionStatus;
import org.infinityConnection.utils.GUIChangeListener;
import org.infinityConnection.client.ReceiveScreen;

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

    private final DataInputStream dis;
    private final DataOutputStream dos;

    private String hostName = "Undefined";

    private final Timer timer = new Timer();
    private int seconds = 0;

    private boolean stopWasRequested = false;
    private final ExecutorService service = Executors.newCachedThreadPool();
    private final List<GUIChangeListener> listeners = new ArrayList<>();

    private ReceiveScreen receiveScreen;
    private SendEvents sendEvents;

    private TimerTask getTask() {
        return new TimerTask() {
            @Override
            public void run() {
                seconds++;
            }
        };
    }

    private void fireGUIChangeEvent() {
        for (GUIChangeListener listener : listeners) {
            listener.onReadingChange();
        }
    }

    public RemoteScreenModel(DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;

        service.submit(() -> {
            try {
                double ssWidth = dis.readDouble();
                double ssHeight = dis.readDouble();

                hostName = dis.readUTF();

                receiveScreen = new ReceiveScreen(dis);
                sendEvents = new SendEvents(dos, ssWidth, ssHeight);

                timer.scheduleAtFixedRate(getTask(), 1000, 1000);

                while (!stopWasRequested) {
                    Thread.sleep(40);
                    fireGUIChangeEvent();
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void addListener(GUIChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GUIChangeListener listener) {
        listeners.remove(listener);
    }

    public ConnectionStatus getConnectionStatus() {
        return receiveScreen.getConnectionStatus();
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

    public EventHandler getMouseMovedEH(double iwWidth, double iwHeight, double iwFitWidth, double iwFitHeight) {
        return sendEvents.getMouseMovedEH(iwWidth, iwHeight, iwFitWidth, iwFitHeight);
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

    public void shutDown() {
        stopWasRequested = true;

        timer.cancel();

        try {
            dis.close();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        receiveScreen.shutDown();
        service.shutdown();
    }

}