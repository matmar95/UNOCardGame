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

  @Override public void hello() throws RemoteException {
    LOG.info("Hello World");
  }

  @Override public HashMap<String, PlayerNode> addNode(PlayerNode node) throws RemoteException {
    //NetworkManager.getInstance().addNode(node);
    //return NetworkManager.getInstance().getNodes();
    return new HashMap<String, PlayerNode>();
  }

  @Override public int sendBeat(PlayerNode node, int blockchainLength) throws RemoteException {
    return 0;
  }
}
