package game.model;

import game.network.NetworkManager;
import game.network.PlayerNode;
import utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class StatusRegistry {

    private static StatusRegistry instance = new StatusRegistry();

    public static StatusRegistry getInstance(){
        return instance;
    }

    private Logger LOG = new Logger(StatusRegistry.class);

    private long seed = 1010110101L;
    private boolean first;
    private HashMap<String,ArrayList<Card>> hands;
    private ArrayList<Card> deck;
    private ArrayList<Card> graveyard;
    private ArrayList<PlayerNode> players;
    private PlayerNode currentPlayer;
    private int currentPlayerIndex;
    private int direction;
    private Timer timer;

    private StatusRegistry(){
        this.first = false;
        this.direction = 1;
        this.hands = new HashMap<>();
        this.currentPlayerIndex = 0;
        this.deck = new ArrayList<>();
        this.graveyard = new ArrayList<>();
    }

    public boolean getFirst() {
        return first;
    }

    public void setFirst(boolean first){
        this.first = first;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int dir){
        this.direction = dir;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public int getMyPlayerIndex(){
        return players.indexOf(NetworkManager.getInstance().getMyNode());
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex){
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public ArrayList<Card> getDeck(){
        return deck;
    }

    public void setDeck(ArrayList<Card> deck){
        this.deck = deck;
    }

    public ArrayList<Card> getGraveyard() {
        return graveyard;
    }

    public HashMap<String, ArrayList<Card>> getHands() {
        return hands;
    }

    public ArrayList<Card> getPlayerHand(PlayerNode player) {
        return hands.get(player.getUsername());
    }

    public void setHands(HashMap<String, ArrayList<Card>> hands) {
        this.hands = hands;
    }

    public ArrayList<PlayerNode> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerNode> players) {
        this.players = players;
    }

    public PlayerNode getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerNode currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Card getTopGraveyard() {
        return graveyard.get(graveyard.size() - 1);
    }



    @Override
    public String toString() {
        String ret =
            "\nStaturRegistry["
            + "\n\tdirection="
            + direction
            + "\n\tcurrentPlayerIndex="
            + currentPlayerIndex
            + "\n\tdeck size="
            + deck.size()
            + "\n\tgraveyard size="
            + graveyard.size()
            + "\n\thands="
            + hands.toString();
        if(players!=null)
            ret +=
                "\n\tplayers="
                + players.toString()
                + ']';
        return ret;
    }

    public Timer getTimer() {
        if(timer == null){
            timer = new Timer();
            return timer;
        }
        return timer;
    }
}

