package com.decagon.OakLandv1be.paystack.utils;

import java.util.HashMap;

public class ApiQuery {

    private HashMap<String, Object> queryMap;

    public ApiQuery(){
        this.queryMap = new HashMap<String, Object>();
    }

    public void putParams(String key, Object value){
        this.queryMap.put(key, value);
    }

    public HashMap<String, Object> getParams(){
        return this.queryMap;
    }
}
