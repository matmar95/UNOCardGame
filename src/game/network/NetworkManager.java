package game.network;

import game.controller.GameController;
import game.controller.GameControllerRemote;
import utils.Logger;
import utils.NetworkUtils;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class NetworkManager {

    Logger LOG = new Logger(NetworkManager.class);

    private static NetworkManager ourInstance = new NetworkManager();

    public static NetworkManager getInstance() {
        return ourInstance;
    }

    private final HashMap<String, PlayerNode> nodes;

    private NetworkManager() {
        this.nodes = new HashMap<>();
    }

    private String myNetworkAddress;

    public ClusterServicesRemote getClusterServices(final PlayerNode player) {
        String url = "rmi://" + player.getNetworkAddress() + "/ClusterServicesRemote";

        ClusterServicesRemote clusterServicesRemote = null;
        try {
            clusterServicesRemote = (ClusterServicesRemote) Naming.lookup(url);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return clusterServicesRemote;
    }

    public PlayerNode getMyNode() {
        return nodes.get(myNetworkAddress);
    }

    public HashMap<String, PlayerNode> getAllNodes() {
        return nodes;
    }

    public void addNode(PlayerNode node) {
        if (!this.nodes.containsKey(node.getNetworkAddress())) {
            this.nodes.put(node.getNetworkAddress(), node);
            LOG.info("Added new node: " + node.toString());
        }
    }

    public HashMap<String, PlayerNode> getOtherNodes() {
        HashMap<String, PlayerNode> otherNodes = new HashMap<>();
        for (Map.Entry<String, PlayerNode> entry : this.nodes.entrySet()) {
            if (!entry.getKey().equals(myNetworkAddress) && entry.getValue().isAlive()) {
                otherNodes.put(entry.getKey(), entry.getValue());
            }
        }
        return otherNodes;
    }

    public HashMap<String, PlayerNode> addAllNodes(HashMap<String, PlayerNode> nodes) {
        HashMap<String, PlayerNode> newNodesMap = new HashMap<>();
        for (Map.Entry<String, PlayerNode> entry : nodes.entrySet()) {
            if (!this.nodes.containsKey(entry.getKey())) {
                this.nodes.put(entry.getKey(), entry.getValue());
                newNodesMap.put(entry.getKey(), entry.getValue());
                LOG.info("Add Joined nodes: " + entry.getValue().toString());
            }
        }
        return newNodesMap;
    }

    public void initialize(String username) {
        final String ipAddress = NetworkUtils.getIpAddress();
        final int port = NetworkUtils.getFreePort();
        this.myNetworkAddress = ipAddress + ":" + port;

        PlayerNode myNode = new PlayerNode(username,ipAddress, port);
        LOG.info("This node: " + myNode.toString());
        nodes.put(myNetworkAddress,myNode);
        try {
            java.rmi.registry.LocateRegistry.createRegistry(port);
            LOG.info("RMI registry ready");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            Naming.rebind("//" + myNetworkAddress + "/ClusterServicesRemote", new ClusterServices());
            Naming.rebind("//" + myNetworkAddress + "/GameControllerRemote", new GameController());
            LOG.info("RMI rebind ready");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public GameControllerRemote getGameController(final PlayerNode nodePlayer) {
        String url = "rmi://"+nodePlayer.getNetworkAddress()+"/GameControllerRemote";

        GameControllerRemote gcr = null;
        try{
            gcr = (GameControllerRemote) Naming.lookup(url);
        } catch(NotBoundException nbe){
            nbe.printStackTrace();
        } catch (RemoteException re){
            re.printStackTrace();
        } catch (MalformedURLException mue){
            mue.printStackTrace();
        }
        return gcr;
    }
}
