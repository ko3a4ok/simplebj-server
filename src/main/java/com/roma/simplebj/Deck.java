/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roma.simplebj;

import com.roma.simplebj.Card.Rank;
import com.roma.simplebj.Card.Suit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ko3a4ok
 */
public class Deck {
    private List<Card> cards = new ArrayList<Card>();
    private int randLimit;
    private int cardPointer;
    public Deck() {
        createDeck();
        shuffle();
    }

    private void createDeck() {
        for (int i = 0; i < 8; i++)
        for (Suit suit : Card.Suit.values())
            for (Rank rank : Card.Rank.values())
                cards.add(new Card(suit, rank));
    }

    private void shuffle() {
        Collections.shuffle(cards);
        randLimit = cards.size()/2 + new Random().nextInt(10);
        cardPointer = 0;
    }
    
    public void onRoundFinish() {
        if (randLimit <= cardPointer) shuffle();
    }
    
    public Card getCard() {
        return cards.get(cardPointer++);
    }
}
