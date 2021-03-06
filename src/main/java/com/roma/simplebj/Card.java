/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roma.simplebj;

import net.sf.json.JSONObject;

/**
 *
 * @author ko3a4ok
 */
public class Card {
    enum Suit { H, D, C, S}
    enum Rank {A(11), K(10), Q(10), J(10), _10(10), _9(9), _8(8),
               _7(7), _6(6), _5(5), _4(4), _3(3), _2(2);
               int value;
               Rank(int value) {
                   this.value = value;
               }
    }
    
    private Suit suit;
    private Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public JSONObject getJson() {
        JSONObject o = new JSONObject();
        o.put("suit", suit);
        o.put("rank", rank.toString().replaceAll("_", ""));
        return o;
    }
    
}
