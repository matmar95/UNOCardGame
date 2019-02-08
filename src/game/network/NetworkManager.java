package game.network;

import utils.NetworkUtils;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;

public class NetworkManager {
    private NetworkManager() {
        this.nodes = new HashMap<>();
    }
    private static NetworkManager ourInstance = new NetworkManager();

    public static NetworkManager getInstance() {
        return ourInstance;
    }
    private String myNetworkAddress;
    private final HashMap<String, PlayerNode> nodes;

    public void initialize(String username) {
        final String ipAddress = NetworkUtils.getIpAddress();
        final int port = NetworkUtils.getFreePort();
        this.myNetworkAddress = ipAddress + ":" + port;

        PlayerNode myNode = new PlayerNode(username,ipAddress, port);
        nodes.put(myNetworkAddress,myNode);


    }
}
