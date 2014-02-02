/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roma.simplebj;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
    private int position;
    private PlayerActions playerActions;
    private BjAction action;

    public BetPlace(Socket socket) {
        if (socket == null) return;
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

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPlayerActions(PlayerActions playerActions) {
        this.playerActions = playerActions;
    }

    public Socket getSocket() {
        return socket;
    }
    
    
    public int getPoints() {
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
        action = null;
    }


    public void actionDouble() {
        betAmount *= 2;
    }

    public int getBetAmount() {
        return betAmount;
    }


    void pushAction(String roundStart) {
        try {
            pw.println(roundStart);
        } catch (Exception ex) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
                      
    private void performClientAction(String str) {
        System.err.println("client sent: " + str);
        JSONObject o = JSONObject.fromObject(str);
        String action = o.getString("action");
        if ("bets".equals(action)) {
            betAmount += o.getInt("amount");
            playerActions.onPlayerBet(position, betAmount);
        } else if ("action".equals(action))  {
            BjAction bjAction = BjAction.valueOf(o.getString("value"));
            playerActions.onPlayerAction(position, bjAction);
        }
    }

    public void setAction(BjAction action) {
        this.action = action;
    }

    public BjAction popAction() {
        BjAction bj = action;
        action = null;
        return bj;
    }
}

