package game.model;

import sun.security.provider.NativePRNG;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private static Deck instance = new Deck();

    public static Deck getInstance(){
        return instance;
    }

    private ArrayList<Card> deck;

    private Deck(){
        this.deck = new ArrayList<>();
    }


}
