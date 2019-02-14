package game.controller;

import game.model.Card;
import game.model.StatusRegistry;
import game.model.Type;
import game.network.NetworkManager;
import game.network.PlayerNode;
import sun.nio.ch.Net;
import utils.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameController extends UnicastRemoteObject implements GameControllerRemote {

    private Logger LOG = new Logger(GameController.class);

    /*private static GameController instance;

    static {
        try {
            instance = new GameController();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static GameController getInstance() {
        return instance;
    }*/

    public GameController() throws RemoteException {
    }

    private static int TIME = 20000;

    @Override
    public void startNewGame(PlayerNode player, long seed){
        ArrayList<PlayerNode> players = new ArrayList<>(NetworkManager.getInstance().getAllNodes().values());
        Collections.shuffle(players, new Random(seed));
        StatusRegistry.getInstance().setPlayers(players);
        ArrayList<Card> deck = new Generator().generateDeck(seed);
        StatusRegistry.getInstance().setDeck(deck);
        new Generator().generateHand(players,deck);
        new Generator().topCardToGraveyard(deck);
        if(StatusRegistry.getInstance().getFirst()){
            for (Map.Entry<String,PlayerNode> entry: NetworkManager.getInstance().getOtherNodes().entrySet()) {
                try {
                    NetworkManager.getInstance().getGameController(entry.getValue()).startNewGame(player,seed);
                } catch (RemoteException re){
                    re.printStackTrace();
                }
            }
        }
        LOG.info(StatusRegistry.getInstance().toString());
        HomeUIController.getInstance().launchGameUI();

        if(StatusRegistry.getInstance().getMyPlayerIndex() == StatusRegistry.getInstance().getCurrentPlayerIndex()){
            //timer();
        }
    }



    private void timer(){
        LOG.info("Timer turn started");
        StatusRegistry.getInstance().getTimer().schedule(new TimerTask() {
            @Override
            public void run() {

            }
        },TIME);
    }
}
