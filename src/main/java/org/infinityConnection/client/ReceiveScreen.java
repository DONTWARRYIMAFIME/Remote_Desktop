package org.infinityConnection.client;

import javafx.scene.image.ImageView;

import javafx.scene.image.Image;
import java.io.*;
import java.net.SocketException;

public class ReceiveScreen extends Thread implements Closeable {

    private final DataInputStream dis;
    private final ImageView iw;

    public ReceiveScreen(DataInputStream dis, ImageView iw) {
        this.dis = dis;
        this.iw = iw;

        setDaemon(true);
        start();
    }

    @Override
    public void run() {

        while(!interrupted()) {

            try {
                byte[] bytes = new byte[1024 * 1024];
                int count = 0;
                do {
                    count += dis.read(bytes, count, bytes.length - count);
                } while (!(count > 4 && bytes[count - 2] == (byte) - 1 && bytes[count - 1] == (byte) - 39));

                Image img = new Image(new ByteArrayInputStream(bytes));
                iw.setImage(img);

            } catch (SocketException e) {
                System.out.println("Client: disconnected from server. Threads closed");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                try {
                    close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

        }

    }

    @Override
    public void close() throws IOException {
        dis.close();
        interrupt();
    }
}
