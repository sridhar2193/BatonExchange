package com.baton.Exchange.model;

import java.util.Objects;

public final class StockPriceGroup {

    private String stock;
    private double price;

    public StockPriceGroup(StockPriceBuilder stockPriceBuilder) {
        this.stock = stockPriceBuilder.stock;
        this.price = stockPriceBuilder.price;
    }

    public String getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockPriceGroup that = (StockPriceGroup) o;
        return Double.compare(that.getPrice(), getPrice()) == 0 &&
                Objects.equals(getStock(), that.getStock());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getStock(), getPrice());
    }

    @Override
    public String toString() {
        return "StockPriceGroup{" +
                "stock='" + stock + '\'' +
                ", price=" + price +
                '}';
    }

    public static final class StockPriceBuilder {
        private String stock;
        private double price;
        private StockPriceBuilder() {
        }
        public static StockPriceBuilder stockPriceBuilder() {
            return new StockPriceBuilder();
        }
        public static StockPriceBuilder stockPriceBuilder(StockPriceGroup stockPriceGroup) {
            return stockPriceBuilder().addStock(stockPriceGroup.getStock()).addPrice(stockPriceGroup.getPrice());
        }
        public StockPriceBuilder addStock(String stock) {
            this.stock = stock;
            return this;
        }
        public StockPriceBuilder addPrice(double price) {
            this.price = price;
            return this;
        }

        public StockPriceGroup build() {
            return new StockPriceGroup(this);
        }
    }

}

