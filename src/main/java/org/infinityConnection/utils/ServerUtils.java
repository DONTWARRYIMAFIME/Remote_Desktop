package org.infinityConnection.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ServerUtils {

    public static String getIP() {

        String ip;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    System.out.println(iface.getDisplayName() + " " + ip);
                    if (ip.matches("([0-9]{1,3}[\\.]){3}[0-9]{1,3}")) {
                        return ip;
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return "127.0.0.1";
    }

    public static String generatePassword() {
        return "1111";
    }



}
