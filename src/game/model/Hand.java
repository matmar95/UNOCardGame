package game.model;

import java.util.ArrayList;

public class Hand {
    private static Hand instance = new Hand();

    public static Hand getInstance() {
        return instance;
    }

    private ArrayList<Card> hand = new ArrayList<>();

    public ArrayList<Card> getHand(){
        return this.hand;
    }

    public void addCard(Card card){
        this.hand.add(card);
    }

    public void removeCard(Card card){
        this.hand.remove(card);
    }
}
