/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roma.simplebj;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ko3a4ok
 */
public class BetPlace {
    private Socket socket;
    private int betAmount;
    private List<Card> cards = new ArrayList<Card>();
    private PrintWriter pw;
    private Scanner sc;

    public BetPlace(Socket socket) {
        
        this.socket = socket;
        try {
            pw = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(BetPlace.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            sc = new Scanner(socket.getInputStream());
            new Thread(new Runnable() {

                public void run() {
                    while (BetPlace.this.socket.isConnected()) {
                        if (sc.hasNext()){
                            String str = sc.nextLine();
                            performClientAction(str);
                        } else {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(BetPlace.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }

            }).start();
        } catch (IOException ex) {
            Logger.getLogger(BetPlace.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Socket getSocket() {
        return socket;
    }
    
    
    public int getPoint() {
        int aceCount = 0;
        int point = 0;
        for (Card card: cards) {
            point += card.getRank().value;
            if (card.getRank() == Card.Rank.A) aceCount++;
        }
        while (aceCount > 0 && point > 21) {
            aceCount--;
            point -= 10;
        }
        return point;
    }
    
    public void addCard(Card card) {
        cards.add(card);
    }
    
    public void onRoundFinish() {
        betAmount = 0;
        cards.clear();
    }

    void onRoundStart() {
        
    }

    void pushAction(String roundStart) {
        pw.println(roundStart);
    }
                      
    private void performClientAction(String str) {
        System.err.println("client sent: " + str);
    }

}

