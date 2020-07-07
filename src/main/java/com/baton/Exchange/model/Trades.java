package com.baton.Exchange.model;

import java.time.LocalDate;

public class Trades {
    private int id;
    private String sellerName;
    private String buyerName;
    private String stock;
    private double price;
    private LocalDate date;

    public Trades() {
        super();
    }
    public Trades(String sellerName, String buyerName, String stock, double price, LocalDate date) {
        super();
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.stock = stock;
        this.price = price;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Trades{" +
                "id=" + id +
                ", sellerName='" + sellerName + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", stock='" + stock + '\'' +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
