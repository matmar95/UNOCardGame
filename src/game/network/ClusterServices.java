package game.network;

import utils.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ClusterServices extends UnicastRemoteObject implements
    ClusterServicesRemote {

  private final Logger LOG = new Logger(ClusterServices.class);

  protected ClusterServices() throws RemoteException {
    super();
  }

  @Override
  public HashMap<String, PlayerNode> addNode(PlayerNode node) throws RemoteException {
    NetworkManager.getInstance().addNode(node);
    return NetworkManager.getInstance().getAllNodes();
  }

  public boolean canPlay() throws RemoteException {
    return NetworkManager.getInstance().checkCanPlay();
  }
}
