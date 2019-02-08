package game.network;

import game.controller.GameController;
import game.controller.UIController;
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
        try {
            java.rmi.registry.LocateRegistry.createRegistry(port);
            //LOG.info("RMI registry ready");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
           // Naming.rebind("//" + myNetworkAddress + "/ClusterServicesRemote", new ClusterServices());
            Naming.rebind("//" + myNetworkAddress + "/GameControllerRemote", new GameController());
            //LOG.info("RMI Bind ready");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
