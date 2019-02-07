package game.model;

import java.util.ArrayList;

public class Deck {
    private static Deck instance = new Deck();

    public static Deck getInstance(){
        return instance;
    }


    private ArrayList<Card> deck = new ArrayList<>();

    private void generateDeck(){

    }
}
