package game.network;

import utils.Logger;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class NetworkClusterServices {

  private final Logger LOG = new Logger(NetworkClusterServices.class);

  public void joinTheCluster(final PlayerNode node) {
    /*ClusterServicesRemote clusterServicesRemote = NetworkManager.getInstance().getClusterServices(node);

    try {
      HashMap<String, PlayerNode> remoteNodesMap =
          clusterServicesRemote.addNode(NetworkManager.getInstance().getMyNode());
      HashMap<String, PlayerNode> newNodesMap =
          NetworkManager.getInstance().addNodes(remoteNodesMap);

      if (!newNodesMap.isEmpty()) {
        for (Map.Entry<String, PlayerNode> entry : newNodesMap.entrySet()) {
          joinTheCluster(entry.getValue());
        }
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }*/
  }
}