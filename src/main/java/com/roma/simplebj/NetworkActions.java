/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roma.simplebj;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Arrays;

/**
 *
 * @author ko3a4ok
 */
public class NetworkActions {
    private JSONObject createStakeholder(String action) {
        JSONObject obj = new JSONObject();
        obj.put("action", action);
        return obj;
    }
    
    public String roundStart(long timer) {
        JSONObject o = createStakeholder("roundStart");
        JSONObject data = new JSONObject();
        data.put("timer", timer);
        o.put("data", data);
        return o.toString();
    }

    String roundFinish(int winAmount) {
        JSONObject o = createStakeholder("roundFinish");
        JSONObject data = new JSONObject();
        data.put("winAmount", winAmount);
        o.put("data", data);
        return o.toString();
    }

    public String actions(long timer) {
        JSONObject o = createStakeholder("actions");
        JSONArray bjAction = new JSONArray();
        bjAction.addAll(Arrays.asList(BjAction.values()));
        JSONObject data = new JSONObject();
        data.put("actions", bjAction);
        data.put("timer", timer);
        o.put("data", data);
        return o.toString();
    }

    public String dealCard(Card card, int position, int points) {
        JSONObject o = createStakeholder("dealCard");
        JSONObject data = new JSONObject();
        data.put("position", position);
        data.put("card", card.getJson());
        data.put("points", points);
        o.put("data", data);
        return o.toString();
    }
}
