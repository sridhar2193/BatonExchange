package com.baton.Exchange.model;

public enum OrdersStatus {
    SOLD,UNSOLD;

    public static OrdersStatus getOrdersStatus(String status){
        return OrdersStatus.valueOf(status);
    }
}
