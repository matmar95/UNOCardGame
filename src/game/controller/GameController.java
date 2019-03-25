package game.controller;

import game.model.Card;
import game.model.Color;
import game.model.StatusRegistry;
import game.model.Type;
import game.network.NetworkManager;
import game.network.PlayerNode;
import javafx.application.Platform;
import utils.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameController extends UnicastRemoteObject implements GameControllerRemote {

    private Logger LOG = new Logger(GameController.class);
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
        StatusRegistry.getInstance().setSeed(seed);
        ArrayList<String> avatars = new  Generator().generateAvatars(seed);
        StatusRegistry.getInstance().setAvatars(avatars);
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
        NetworkManager.getInstance().nodesHealthCheck(true);

        if(StatusRegistry.getInstance().getMyPlayerIndex() == StatusRegistry.getInstance().getCurrentPlayerIndex()){
            timer();
        }
    }

    @Override public void playCard(PlayerNode player, Card card) {
        NetworkManager.getInstance().nodesHealthCheck(true);
        LOG.info("New card from " + player.getUsername() + ": " + card.toString());
        final StatusRegistry gs = StatusRegistry.getInstance();
        GameUIController.getInstance().gridColorChoose.setVisible(false);
        if (gs.getPlayerHand(player).size() == 1) {
            StatusRegistry.getInstance().setCalledOnePlayer(null);
        }
        if (!isMyTurn(player)) {
            GameUIController.getInstance().showDialog("It's not your turn yet!");
            return;
        }
        if (isMePlayer(player) && !isValidCard(player, card)) {
            GameUIController.getInstance().showDialog("You can't play this card!");
            GameUIController.getInstance().gridColorChoose.setVisible(false);
            return;
        }
        stopTimer();
        switch (card.getType()) {
            case NONE:
                gs.setCurrentPlayerIndex(gs.getNextPlayerIndex(false));
                gs.getGraveyard().add(card);
                break;
            case SKIP:
                gs.setCurrentPlayerIndex(gs.getNextPlayerIndex(true));
                gs.getGraveyard().add(card);
                break;
            case DRAW2:
                PlayerNode target = gs.getAPlayer(gs.getNextPlayerIndex(false));
                Card card1 = gs.drawTopDeckCard();
                Card card2 = gs.drawTopDeckCard();
                gs.getPlayerHand(target).add(card1);
                gs.getPlayerHand(target).add(card2);
                gs.setCurrentPlayerIndex(gs.getNextPlayerIndex(true));
                gs.getGraveyard().add(card);
                break;
            case INVERT:
                gs.setDirection(gs.getDirection() * -1);
                gs.setCurrentPlayerIndex(gs.getNextPlayerIndex(false));
                gs.getGraveyard().add(card);
                break;
            case COLORCHANGE:
                gs.setCurrentPlayerIndex(gs.getNextPlayerIndex(false));
                gs.getGraveyard().add(card);
                break;
            case DRAW4COLORCHANGE:
                PlayerNode targetW = gs.getAPlayer(gs.getNextPlayerIndex(false));
                ArrayList<Card> extraCards = new ArrayList<>();
                for (int k = 0; k < 4; k++) {
                    extraCards.add(gs.drawTopDeckCard());
                }
                gs.getPlayerHand(targetW).addAll(extraCards);
                gs.setCurrentPlayerIndex(gs.getNextPlayerIndex(true));
                gs.getGraveyard().add(card);
                break;
        }

        gs.getPlayerHand(player).remove(card);

        if (gs.getPlayerHand(player).size() == 0) {
            callVictoryScreen(player);
        }
        boolean drawTwoCard = false;
        if (isMePlayer(player) && gs.getPlayerHand(player).size() == 1 && gs.getCalledOnePlayer() == null) {
            drawTwoCard(player);
            drawTwoCard = true;
        }
        GameUIController.getInstance().updateGUI();
        if (isMePlayer(player)) {
            for (Map.Entry<String, PlayerNode> entry : NetworkManager.getInstance().getOtherNodes().entrySet()) {
                try {
                    NetworkManager.getInstance().getGameController(entry.getValue()).playCard(player, card);
                    if (drawTwoCard) {
                        NetworkManager.getInstance().getGameController(entry.getValue()).drawTwoCard(player);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        if (StatusRegistry.getInstance().getMyPlayerIndex() == StatusRegistry.getInstance().getCurrentPlayerIndex()) {
            timer();
        }
    }

    public boolean isValidCard(PlayerNode player, Card card) {
        Card topGraveyardCard = StatusRegistry.getInstance().getTopGraveyard();
        Color topGraveyardColor = topGraveyardCard.getColor();
        int topGraveyardCardNumber = topGraveyardCard.getNumber();
        Type topGraveyardType = topGraveyardCard.getType();
        if (card.getType() == Type.COLORCHANGE) {
            return true;
        } else if (card.getType() == Type.DRAW4COLORCHANGE) {
            ArrayList<Card> hand = StatusRegistry.getInstance().getPlayerHand(player);
            for (Card handCard : hand) {
                if (handCard.getType() != Type.DRAW4COLORCHANGE) {
                    if (isValidCard(player, handCard)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return topGraveyardColor == card.getColor() || (topGraveyardCardNumber == card.getNumber()
                    && card.getNumber() != -1) || (topGraveyardType == card.getType()
                    && card.getType() != Type.NONE);
        }
    }

    public void drawCard(PlayerNode player) {
        NetworkManager.getInstance().nodesHealthCheck(true);
        if(StatusRegistry.getInstance().getPlayerHand(player).size()==1){
            StatusRegistry.getInstance().setCalledOnePlayer(null);
        }
        if (!isMyTurn(player)) {
            GameUIController.getInstance().showDialog("It's not your turn yet!");
            return;
        }
        stopTimer();
        LOG.info(player.getUsername() + " drawed a card");
        StatusRegistry.getInstance().getPlayerHand(player).add(StatusRegistry.getInstance().drawTopDeckCard());
        StatusRegistry.getInstance().setCurrentPlayerIndex(StatusRegistry.getInstance().getNextPlayerIndex(false));
        GameUIController.getInstance().updateGUI();
        if (isMePlayer(player)) {
            for (Map.Entry<String, PlayerNode> entry : NetworkManager.getInstance().getOtherNodes().entrySet()) {
                Platform.runLater(()->{
                    try {
                        NetworkManager.getInstance().getGameController(entry.getValue()).drawCard(player);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        if (StatusRegistry.getInstance().getMyPlayerIndex() == StatusRegistry.getInstance().getCurrentPlayerIndex()) {
            timer();
        }
        LOG.info(StatusRegistry.getInstance().getPlayerHand(NetworkManager.getInstance().getMyNode()).toString());
    }

    @Override public void callOne(PlayerNode player) {
        NetworkManager.getInstance().nodesHealthCheck(true);
        if (!isMePlayer(player)) {
            GameUIController.getInstance().showDialog("Player " + player.getUsername() + " called UNO!");
        }
        StatusRegistry.getInstance().setCalledOnePlayer(player);
        GameUIController.getInstance().updateGUI();
        if (isMePlayer(player)) {
            for (Map.Entry<String, PlayerNode> entry : NetworkManager.getInstance().getOtherNodes().entrySet()) {
                try {
                    NetworkManager.getInstance().getGameController(entry.getValue()).callOne(player);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void callVictoryScreen(PlayerNode player){
        NetworkManager.getInstance().nodesHealthCheck(true);
        GameUIController.getInstance().showVictoryScreen(player);
        GameUIController.getInstance().updateGUI();
        stopTimer();
        if (isMePlayer(player)) {
            for (Map.Entry<String, PlayerNode> entry : NetworkManager.getInstance().getOtherNodes().entrySet()) {
                try {
                    NetworkManager.getInstance().getGameController(entry.getValue()).callVictoryScreen(player);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void drawTwoCard(PlayerNode player) {
        final StatusRegistry gs = StatusRegistry.getInstance();
        Card card1 = gs.drawTopDeckCard();
        Card card2 = gs.drawTopDeckCard();
        gs.getPlayerHand(player).add(card1);
        gs.getPlayerHand(player).add(card2);
        gs.setCalledOnePlayer(null);
    }

    public boolean isMyTurn(PlayerNode player) {
        return !isMePlayer(player) || StatusRegistry.getInstance().getMyPlayerIndex() == StatusRegistry.getInstance().getCurrentPlayerIndex();
    }


    private boolean isMePlayer(PlayerNode player) {
        return player.getNetworkAddress().equals(NetworkManager.getInstance().getMyNode().getNetworkAddress());
    }

    private void timer(){
        LOG.info("Timer turn started");
        Platform.runLater(()-> {
            StatusRegistry.getInstance().getTimer().schedule(new TimerTask() {
                @Override
                public void run() {
                    drawCard(NetworkManager.getInstance().getMyNode());
                }
            }, TIME);
        });
    }

    private void stopTimer(){
        if(StatusRegistry.getInstance().getTimer() != null){
            StatusRegistry.getInstance().getTimer().cancel();
            StatusRegistry.getInstance().getTimer().purge();
            StatusRegistry.getInstance().setTimer(null);
            LOG.info("Countdown stopped");
        }
    }


}
