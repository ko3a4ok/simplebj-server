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
public class NetworkActions {
    private JSONObject createStakeholder(String action) {
        JSONObject obj = new JSONObject();
        obj.put("action", action);
        return obj;
    }
    
    public String roundStart() {
        return createStakeholder("roundStart").toString();
    }

    String roundFinish() {
        return createStakeholder("roundFinish").toString();
    }
}
