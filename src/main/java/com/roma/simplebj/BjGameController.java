/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roma.simplebj;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ko3a4ok
 */
public class BjGameController implements PlayerActions{
    private int MAX_PLACES = 7;
    private Deck deck = new Deck();
    private BetPlace[] places = new BetPlace[MAX_PLACES];
    private BetPlace dealer = new BetPlace(null);
    private NetworkActions networkActions = new NetworkActions();
    public boolean addClient(Socket socket) {
        for (int i = 0; i < MAX_PLACES; i++)
            if (places[i] == null) {
                places[i] = new BetPlace(socket);
                places[i].setPosition(i);
                places[i].setPlayerActions(this);
                return true;
            }
        return false;
    }
    
    public void roundStart() {
        notifyAll(networkActions.roundStart(Consts.BETTING_TIME));
        sleep(Consts.BETTING_TIME);
    }

    void dealCards() {
        deck.getCard();
        for (int i = 0; i < 2; i++) {
            for (int position = 0; position < places.length; position++)
                if (places[position] != null && places[position].getBetAmount() > 0)
                    dealerCardToPlayer(position);
            if (i == 0) dealCardToDealer();
        }
        for (BetPlace place: places) {
            if (place != null && place.getBetAmount() > 0) {
                waitForActions(place);
            }
        }
        while (dealer.getPoints() < 17)
            dealCardToDealer();
    }

    private void waitForActions(BetPlace place) {
        synchronized (place) {
            while (place.getPoints() < 21) {
                place.pushAction(networkActions.actions(Consts.ACTION_TIME));
                try {
                    place.wait(Consts.ACTION_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BjAction action = place.popAction();
                if (action == BjAction.Hit) {
                    dealerCardToPlayer(place.getPosition());
                    continue;
                }
                if (action == BjAction.Double) {
                    dealerCardToPlayer(place.getPosition());
                    place.actionDouble();
                }
                return;
            }
        }
    }

    private void dealerCardToPlayer(int position) {
        Card card = deck.getCard();
        places[position].addCard(card);
        notifyAll(networkActions.dealCard(card, position+1, places[position].getPoints()));
    }

    private void dealCardToDealer() {
        Card card = deck.getCard();
        dealer.addCard(card);
        notifyAll(networkActions.dealCard(card, 0, dealer.getPoints()));
    }

    void roundFinish() {
        checkConnections();
        int dealerPoints = dealer.getPoints();
        if (dealerPoints > 21) dealerPoints = -1;
        deck.onRoundFinish();
        for (BetPlace place : places)
            if (place != null && place.getBetAmount() > 0) {
                int points = place.getPoints();
                int winAmount = points > 21 || dealerPoints > points ? 0 : dealerPoints == points ? place.getBetAmount() : 2*place.getBetAmount();
                place.pushAction(networkActions.roundFinish(winAmount));
                place.onRoundFinish();
            }
        dealer.onRoundFinish();
        sleep(5000);
    }

    private void checkConnections() {
        for (int i = 0; i < places.length; i++)
            if (places[i] != null && places[i].getSocket().isClosed())
                places[i] = null;
    }

    void notifyAll(String action) {
        for (BetPlace place: places)
            if (place != null)
                place.pushAction(action);
    }
    
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(BjGameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onPlayerAction(int position, BjAction action) {
        places[position].setAction(action);
        synchronized (places[position]) {
            places[position].notify();
        }
    }

    @Override
    public void onPlayerBet(int position, int amount) {

    }
}
