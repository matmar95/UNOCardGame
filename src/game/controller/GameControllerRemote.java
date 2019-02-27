package game.controller;

import game.model.Card;
import game.network.PlayerNode;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameControllerRemote extends Remote {

    void startNewGame(PlayerNode player, long seed) throws RemoteException;

    void playCard(PlayerNode player, Card card) throws RemoteException;

    void drawCard(PlayerNode player) throws RemoteException;

    void callVictoryScreen(PlayerNode player) throws RemoteException;

    void drawTwoCard(PlayerNode player) throws RemoteException;

    void callOne(PlayerNode player) throws RemoteException;
}
