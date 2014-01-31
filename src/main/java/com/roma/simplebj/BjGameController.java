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
public class BjGameController {
    private int MAX_PLACES = 7;
    private Deck deck = new Deck();
    private BetPlace[] places = new BetPlace[MAX_PLACES];
    private NetworkActions networkActions = new NetworkActions();
    public boolean addClient(Socket socket) {
        for (int i = 0; i < MAX_PLACES; i++)
            if (places[i] == null) {
                places[i] = new BetPlace(socket);
                return true;
            }
        return false;
    }
    
    public void roundStart() {
        for (BetPlace place: places) 
            if (place != null) 
                place.pushAction(networkActions.roundStart());
        sleep(15000);
    }

    void dealCards() {
    }

    void roundFinish() {
        for (BetPlace place: places) 
            if (place != null) 
                place.pushAction(networkActions.roundFinish());
        
    }
    
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(BjGameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
