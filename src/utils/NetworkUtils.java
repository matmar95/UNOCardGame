package utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class NetworkUtils {

    private static final String IPV4_LOCALHOST = "127.0.0.1";

    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");

    public static String getIpAddress() {
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (IPV4_PATTERN.matcher(i.getHostAddress()).matches() && !i.getHostAddress()
                            .equals(IPV4_LOCALHOST)) {
                        return i.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return IPV4_LOCALHOST;
    }

    public static int getFreePort() {
        for (int port = 9120; port < 10120; port++) {
            try {
                new ServerSocket(port).close();
                return port;
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
        throw new RuntimeException("No ports available.");
    }
}

