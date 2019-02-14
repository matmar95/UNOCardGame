package game.controller;

import game.model.Card;
import game.model.Color;
import game.model.StatusRegistry;
import game.model.Type;
import game.network.PlayerNode;
import utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Generator {

    Logger LOG = new Logger(Generator.class);
    public ArrayList<Card> generateDeck(long seed){
        ArrayList<Card> deck = new ArrayList<>();
        for (Color color: Color.values()) {
            if (color == Color.NONE){
                for(int i=0; i<4; i++){
                    Card colorChange = new Card(-1,Color.NONE, Type.COLORCHANGE);
                    deck.add(colorChange);
                    Card draw4ColorChange = new Card(-1,Color.NONE,Type.DRAW4COLORCHANGE);
                    deck.add(draw4ColorChange);
                }
            } else {
                for(int num=0; num<=12; num++){
                    switch (num){
                        case 0:
                            Card zero = new Card(num, color, Type.NONE);
                            deck.add(zero);
                            break;
                        case 10:
                            Card skip = new Card(-1, color, Type.SKIP);
                            deck.add(skip);
                            skip = new Card(-1, color, Type.SKIP);
                            deck.add(skip);
                            break;
                        case 11:
                            Card invert = new Card(-1, color, Type.INVERT);
                            deck.add(invert);
                            invert = new Card(-1, color, Type.INVERT);
                            deck.add(invert);
                            break;
                        case 12:
                            Card draw2 = new Card(-1, color, Type.DRAW2);
                            deck.add(draw2);
                            draw2 = new Card(-1, color, Type.DRAW2);
                            deck.add(draw2);
                            break;
                        default:
                            Card cardNum = new Card(num, color, Type.NONE);
                            deck.add(cardNum);
                            cardNum = new Card(num, color, Type.NONE);
                            deck.add(cardNum);
                            break;
                    }

                }
            }
        }

        for (int i = 0; i < deck.size(); i++){
            deck.get(i).setIdCard(i+1);
        }
        Collections.shuffle(deck,new Random(seed));

        return deck;
    }

    public void generateHand(ArrayList<PlayerNode> players, ArrayList<Card> deck){
        HashMap<String, ArrayList<Card>> hands = new HashMap<String, ArrayList<Card>>();
        for (PlayerNode player: players){
            hands.put(player.getUsername(), new ArrayList<Card>());
        }
        for (int i=0; i<7; i++){
            for (ArrayList<Card> hand: hands.values()) {
                hand.add(deck.get(0));
                deck.remove(0);
            }
        }
        StatusRegistry.getInstance().setDeck(deck);
        StatusRegistry.getInstance().setHands(hands);
    }

    public void topCardToGraveyard(ArrayList<Card> deck) {
        //prendo la carta in cima al deck
        Card topDeckCard = deck.get(deck.size() - 1);
        LOG.info("The first top graveyard card is: " + topDeckCard.getType());
        //se è diverso dal +4 aggiungo agli scarti e riaggiorno il deck
        if (topDeckCard.getType() != Type.DRAW4COLORCHANGE && topDeckCard.getType() != Type.COLORCHANGE) {
            StatusRegistry.getInstance().getGraveyard().add(topDeckCard);
            //System.out.println(topDeckCard.toString());
            deck.remove(topDeckCard);
            StatusRegistry.getInstance().setDeck(deck);
            //altrimenti sposto il +4 alla fine del mazzo e ricontrollo la carta in cima
        } else {
            deck.add(0, topDeckCard);
            deck.remove(deck.size() - 1);
            topCardToGraveyard(deck);
        }
    }
}
