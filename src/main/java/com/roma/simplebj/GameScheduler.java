/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roma.simplebj;

/**
 *
 * @author ko3a4ok
 */
public class GameScheduler implements Runnable {
    private BjGameController controller;

    public GameScheduler(BjGameController controller) {
        this.controller = controller;
    }
    
    public void run() {
        while (true) {
            controller.roundStart();
            controller.dealCards();
            controller.roundFinish();
            
        }
    }
    
}
