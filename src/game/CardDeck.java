package game;

import game.model.Card;
import java.util.ArrayList;

public class CardDeck {

    private static CardDeck deckInstance = new CardDeck();
    private ArrayList<Card> deck;

    public static CardDeck getInstance() {
        return deckInstance;
    }

    private CardDeck() {
        this.deck = new ArrayList<>();
    }
}
