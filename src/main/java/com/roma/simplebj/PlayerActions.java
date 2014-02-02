package com.roma.simplebj;

/**
 * Created with IntelliJ IDEA.
 * User: ko3a4ok
 * Date: 2/2/14
 * Time: 8:28 PM
 */
public interface PlayerActions {
    public void onPlayerAction(int position, BjAction action);
    public void onPlayerBet(int position, int amount);
}
