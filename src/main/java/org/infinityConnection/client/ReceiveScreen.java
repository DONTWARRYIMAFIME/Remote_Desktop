package org.infinityConnection.client;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReceiveScreen {

    private DataInputStream dis;

    private boolean stopWasRequested = false;
    private final ExecutorService service = Executors.newCachedThreadPool();

    private Image image;

    private void updateImage() {
        try {
            byte[] bytes = new byte[1024 * 1024];
            int count = 0;
            do {
                count += dis.read(bytes, count, bytes.length - count);
            } while (!(count > 4 && bytes[count - 2] == (byte) - 1 && bytes[count - 1] == (byte) - 39));

            image = new Image(new ByteArrayInputStream(bytes));

        } catch (SocketException e) {
            System.out.println("Client: disconnected from server. Threads closed");
            shutDown();
        } catch (Exception e) {
            e.printStackTrace();
            shutDown();
        }
    }

    public ReceiveScreen(DataInputStream dis) {
        this. dis = dis;

        service.submit(() -> {
            while(!stopWasRequested) {
                updateImage();
            }
        });
    }

    public Image getReceivedImage() {
        return image;
    }

    public void shutDown() {
        stopWasRequested = true;
        service.shutdown();
    }

}
