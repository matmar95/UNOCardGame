package game.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface ClusterServicesRemote extends Remote {

  HashMap<String, PlayerNode> addNode(PlayerNode node) throws RemoteException;

  boolean canPlay() throws RemoteException;
}
