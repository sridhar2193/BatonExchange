package com.baton.Exchange.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Orders {
    private int id;
    private String partyName;
    private OrderType orderType;
    private String stock;
    private double price;
    private OrdersStatus status;

    public Orders() {
        super();
    }
    public Orders(String partyName, String orderType, String stock, double price, String status) {
        super();
        this.partyName = partyName;
        this.orderType = OrderType.getOrderType(orderType);
        this.stock = stock;
        this.price = price;
        this.status = OrdersStatus.getOrdersStatus(status);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrdersStatus getStatus() {
        return status;
    }

    public void setStatus(OrdersStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", partyName='" + partyName + '\'' +
                ", orderType=" + orderType +
                ", stock='" + stock + '\'' +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}
