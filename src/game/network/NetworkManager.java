package game.network;

import game.controller.GameController;
import game.controller.GameControllerRemote;
import game.controller.GameUIController;
import game.controller.HomeUIController;
import game.model.StatusRegistry;
import utils.Logger;
import utils.NetworkUtils;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class NetworkManager {

    private Logger LOG = new Logger(NetworkManager.class);

    private static NetworkManager ourInstance = new NetworkManager();
    private int playerNumber;
    private int gcrCheck;

    public static NetworkManager getInstance() {
        return ourInstance;
    }

    private final HashMap<String, PlayerNode> nodes;

    private NetworkManager() {
        this.nodes = new HashMap<>();
        this.playerNumber = 0;
    }

    private String myNetworkAddress;

    ClusterServicesRemote getClusterServices(final PlayerNode player) {
        String url = "rmi://" + player.getNetworkAddress() + "/ClusterServicesRemote";

        ClusterServicesRemote clusterServicesRemote = null;
        try {
            clusterServicesRemote = (ClusterServicesRemote) Naming.lookup(url);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            HomeUIController.getInstance().showDialogError("Can't connect to " + player.getIpAddress() + ":" + player.getPort());
            e.printStackTrace();
        }

        return clusterServicesRemote;
    }

    public PlayerNode getMyNode() {
        return nodes.get(myNetworkAddress);
    }

    public HashMap<String, PlayerNode> getAllNodes() {
        HashMap<String, PlayerNode> allAliveNodes = new HashMap<>();
        for (Map.Entry<String, PlayerNode> entry : this.nodes.entrySet()) {
            if (entry.getValue().isAlive()) {
                allAliveNodes.put(entry.getKey(), entry.getValue());
            }
        }
        return allAliveNodes;
    }

    void addNode(PlayerNode node) {
        if (!this.nodes.containsKey(node.getNetworkAddress())) {
            this.nodes.put(node.getNetworkAddress(), node);
            LOG.info("Added new node: " + node.toString());
            HomeUIController.getInstance().addPlayerToLabel(node);
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

    HashMap<String, PlayerNode> addAllNodes(HashMap<String, PlayerNode> nodes) {
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
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public GameControllerRemote getGameController(final PlayerNode nodePlayer) {
        String url = "rmi://"+nodePlayer.getNetworkAddress()+"/GameControllerRemote";

        GameControllerRemote gcr = null;
        try{
            gcr = (GameControllerRemote) Naming.lookup(url);
            gcrCheck = 0;
        } catch(NotBoundException | MalformedURLException nbe){
            nbe.printStackTrace();
        } catch (RemoteException re){
            if(++gcrCheck>3) {
                nodes.get(nodePlayer.getNetworkAddress()).setAlive(false);
                if (StatusRegistry.getInstance().getDeck().size() != 0) {
                    int deadIndexPlayer = StatusRegistry.getInstance().getPlayers().indexOf(nodePlayer);
                    if (deadIndexPlayer == -1)
                        LOG.error("Node not found on StatusRegistry list: " + nodePlayer.toString());
                    StatusRegistry.getInstance().getPlayers().get(deadIndexPlayer).setAlive(false);
                    StatusRegistry.getInstance().addPlayerHandToDeck(nodePlayer);
                    nodes.remove(nodePlayer.getNetworkAddress());
                    StatusRegistry.getInstance().getPlayers().remove(deadIndexPlayer);
                    StatusRegistry.getInstance().getHands().remove(nodePlayer.getNetworkAddress());
                    int currentPlayerIndex = StatusRegistry.getInstance().getCurrentPlayerIndex();
                    LOG.info("currentPlayerIndex: " + currentPlayerIndex);
                    if (currentPlayerIndex == 0) {
                        if (StatusRegistry.getInstance().getDirection() == -1) {
                            int newCurrentIndex = StatusRegistry.getInstance().getNextPlayerIndex(false);
                            StatusRegistry.getInstance().setCurrentPlayerIndex(newCurrentIndex);
                        }
                        return gcr;
                    } else if (deadIndexPlayer < currentPlayerIndex) {
                        currentPlayerIndex--;
                        StatusRegistry.getInstance().setCurrentPlayerIndex(currentPlayerIndex);
                    } else if (currentPlayerIndex == deadIndexPlayer) {
                        if (StatusRegistry.getInstance().getDirection() == 1) {
                            currentPlayerIndex--;
                            StatusRegistry.getInstance().setCurrentPlayerIndex(currentPlayerIndex);
                        }
                        int newCurrentIndex = StatusRegistry.getInstance().getNextPlayerIndex(false);
                        StatusRegistry.getInstance().setCurrentPlayerIndex(newCurrentIndex);
                    }
                }
            } else {
                LOG.info("Node Unreachable. Reachability Check: "+ gcrCheck);
                try {
                    Thread.sleep(150);
                } catch (Exception e){
                    e.printStackTrace();
                }
                gcr = getGameController(nodePlayer);
            }
        }
        return gcr;
    }

    public void nodesHealthCheck(boolean print) {
        final HashMap<String, PlayerNode> otherNodes = NetworkManager.getInstance().getOtherNodes();
        if(otherNodes.size()==0){
            if(print)LOG.warn("Node not in the cluster");
        } else {
            for (Map.Entry<String, PlayerNode> entry : otherNodes.entrySet()) {
                GameControllerRemote gcr = NetworkManager.getInstance().getGameController(entry.getValue());
                if(gcr == null) {
                    if(print)LOG.warn("This node is offline (dead): " + entry.getValue().toString());
                    if(StatusRegistry.getInstance().getDeck().size() != 0)
                        GameUIController.getInstance().updateGUI();
                }
            }
        }
    }

    boolean checkCanPlay() {
        if(++this.playerNumber<=10){
            LOG.info("checked num player: OK");
            return true;
        } else {
            LOG.info("checked num player: NO");
            return false;
        }
    }
}
