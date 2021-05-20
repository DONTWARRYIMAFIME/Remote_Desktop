package org.infinityConnection.scenes.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Authentication {

    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final String password;

    public Authentication(DataInputStream dis, DataOutputStream dos, String password) {
        this.dis = dis;
        this.dos = dos;
        this.password = password;
    }

    public Verification tryToConnect() throws IOException {

        try{
            dos.writeUTF(password);
            return Verification.valueOf(dis.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
            return Verification.UNKNOWN;
        }
    }

}
