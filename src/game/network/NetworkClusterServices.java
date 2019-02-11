package game.network;

import utils.Logger;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class NetworkClusterServices {

  private final Logger LOG = new Logger(NetworkClusterServices.class);

  public void joinTheCluster(final PlayerNode node) {
    ClusterServicesRemote clusterServicesRemote = NetworkManager.getInstance().getClusterServices(node);

    try {
      HashMap<String, PlayerNode> remoteNodesMap =
          clusterServicesRemote.addNode(NetworkManager.getInstance().getMyNode());
      HashMap<String, PlayerNode> newNodesMap =
          NetworkManager.getInstance().addAllNodes(remoteNodesMap);


      if (!newNodesMap.isEmpty()) {
        for (Map.Entry<String, PlayerNode> entry : newNodesMap.entrySet()) {
          joinTheCluster(entry.getValue());
          LOG.info("Join The Cluster: " + entry.getValue().getUsername());
        }
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
}