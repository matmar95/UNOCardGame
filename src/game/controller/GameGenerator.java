package game.controller;

import game.model.Card;
import game.model.Color;
import game.model.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameGenerator {

    private ArrayList<Card> generateDeck(long seed){
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
                for(int num=0; num<12; num++){
                    switch (num){
                        case 0:
                            Card zero = new Card(num, color, Type.NONE);
                            deck.add(zero);
                            break;
                        case 10:
                            Card skip = new Card(-1, color, Type.SKIP);
                            deck.add(skip);
                            break;
                        case 11:
                            Card invert = new Card(-1, color, Type.INVERT);
                            deck.add(invert);
                            break;
                        case 12:
                            Card draw2 = new Card(-1, color, Type.DRAW2);
                            deck.add(draw2);
                            break;
                        default:
                            Card cardNum = new Card(num, color, Type.NONE);
                            deck.add(cardNum);
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

    public ArrayList<Card> generateHand(){
        return new ArrayList<>();
    }
}
