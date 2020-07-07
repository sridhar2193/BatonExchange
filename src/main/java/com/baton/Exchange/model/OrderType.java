package com.baton.Exchange.model;

public enum OrderType {
    BUY,SELL;

    public static OrderType getOrderType(String orderType){
        return OrderType.valueOf(orderType);
    }
}
