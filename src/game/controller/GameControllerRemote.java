package game.controller;

import game.network.PlayerNode;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameControllerRemote extends Remote {

    void startNewGame(PlayerNode player, long seed) throws RemoteException;

}
