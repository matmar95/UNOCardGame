package game.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface ClusterServicesRemote extends Remote {

  void hello() throws RemoteException;

  HashMap<String, PlayerNode> addNode(PlayerNode node) throws RemoteException;

  int sendBeat(PlayerNode node, int blockchainLength) throws RemoteException;
}
