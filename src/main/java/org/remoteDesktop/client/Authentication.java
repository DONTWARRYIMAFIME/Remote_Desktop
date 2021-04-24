package org.remoteDesktop.client;

import org.remoteDesktop.Verification;

import java.io.*;
import java.net.Socket;

public class Authentication {

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
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
