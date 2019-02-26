package game.model;

import game.network.NetworkManager;
import game.network.PlayerNode;
import utils.Logger;

import java.util.*;
import java.lang.reflect.Array;

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
    private PlayerNode pointedPlayer;
    private ArrayList<String> avatars;
    private int currentPlayerIndex;
    private int direction;
    private Timer timer;
    private boolean colorChange;

    private StatusRegistry(){
        this.first = false;
        this.direction = 1;
        this.hands = new HashMap<>();
        this.currentPlayerIndex = 0;
        this.deck = new ArrayList<>();
        this.graveyard = new ArrayList<>();
        this.colorChange=true;
    }

    public void setAvatars(ArrayList<String> avatars) {
        this.avatars = avatars;
    }
    public ArrayList<String> getAvatars(){
        return avatars;
    }
    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
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

    public PlayerNode getPointedPlayer() {
        return pointedPlayer;
    }

    public void setPointedPlayer(PlayerNode pointedPlayer) {
        this.pointedPlayer = pointedPlayer;
    }

    public Card getTopGraveyard() {
        return graveyard.get(graveyard.size() - 1);
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public PlayerNode getAPlayer(int i) {
        return players.get(i);
    }

    public boolean getColorChange() {
        return colorChange;
    }

    public void setColorChange(boolean colorChange) {
        this.colorChange = colorChange;
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

    public int getNextPlayerIndex(boolean b) {
        int skip = b ? 2 : 1;
        int i = currentPlayerIndex + (skip * direction);
        i = i % players.size();
        if (i < 0) {
            i += players.size();
        }
        return i;
    }

    public Card drawTopDeckCard() {
        if(deck.size() == 1){
            LOG.info("Deck ended");
            LOG.info("Shuffling Graveyard to Deck");
            ArrayList<Card> newDeck = new ArrayList<>(getGraveyard().subList(0,graveyard.size()-1));
            getGraveyard().subList(0,graveyard.size()-1).clear();
            Collections.shuffle(newDeck, new Random(seed));
            setDeck(newDeck);
            LOG.info("Deck size; " + deck.size());
            LOG.info("Graveyard size; " + graveyard.size());
            return deck.remove(deck.size()-1);
        } else {
            LOG.info("Deck size; " + deck.size());
            LOG.info("Graveyard size; " + graveyard.size());
            return deck.remove(deck.size()-1);
        }
    }

    public void addPlayerHandToDeck(PlayerNode player) {
        ArrayList<Card> hand = getPlayerHand(player);
        Collections.shuffle(hand, new Random(seed));
        deck.addAll(0, hand);
        getPlayerHand(player).clear();
    }



}

