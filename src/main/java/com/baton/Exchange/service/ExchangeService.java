package com.baton.Exchange.service;

import com.baton.Exchange.dao.ExchangeRepository;
import com.baton.Exchange.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ExchangeService {

    @Autowired
    ExchangeRepository repository;

    private List<Orders> insertOrders = new ArrayList<>();
    private List<Orders> updateOrders = new ArrayList<>();

    @Async
    public CompletableFuture<List<Trades>> buyOrders(List<Orders> ordersData) {
        final HashMap<Orders, Orders> ordersMap = new HashMap<>();
        insertOrders = new ArrayList<>();
        updateOrders = new ArrayList<>();
        Map<StockPriceGroup, List<Orders>> buyOrders = ordersData.parallelStream()
                .filter(order -> order.getOrderType() == OrderType.BUY)
                .collect(Collectors.groupingBy(orders -> StockPriceGroup.StockPriceBuilder.stockPriceBuilder().
                        addStock(orders.getStock()).addPrice(orders.getPrice()).build()));
        Map<StockPriceGroup, List<Orders>> sellOrders = ordersData.parallelStream()
                .filter(order -> order.getOrderType() == OrderType.SELL)
                .collect(Collectors.groupingBy(orders -> StockPriceGroup.StockPriceBuilder.stockPriceBuilder().
                        addStock(orders.getStock()).addPrice(orders.getPrice()).build()));
        List<Orders> ordersList = repository.getUnSoldOrders();
        Map<StockPriceGroup, List<Orders>> unSoldBuyOrders = ordersList.parallelStream()
                .filter(order -> order.getOrderType() == OrderType.BUY)
                .collect(Collectors.groupingBy(orders -> StockPriceGroup.StockPriceBuilder.stockPriceBuilder().
                        addStock(orders.getStock()).addPrice(orders.getPrice()).build()));
        Map<StockPriceGroup, List<Orders>> unSoldSellOrders = ordersList.parallelStream()
                .filter(order -> order.getOrderType() == OrderType.SELL)
                .collect(Collectors.groupingBy(orders -> StockPriceGroup.StockPriceBuilder.stockPriceBuilder().
                        addStock(orders.getStock()).addPrice(orders.getPrice()).build()));
        ordersMap.putAll(getMatchedOrders(buyOrders, unSoldSellOrders));
        ordersMap.putAll(getMatchedOrders(unSoldBuyOrders, sellOrders));
        ordersMap.putAll(getMatchedOrders(buyOrders, sellOrders));
        CompletableFuture<List<Trades>> completableFutureTradeList = createTradeRecordsAndInsert(ordersMap);
        createMatchedAndUnMatchedOrders(buyOrders, sellOrders);
        return completableFutureTradeList;
    }

    @Async
    private void createMatchedAndUnMatchedOrders(Map<StockPriceGroup, List<Orders>> buyOrders,
                                                                    Map<StockPriceGroup, List<Orders>> sellOrders) {
        buyOrders.forEach((key, value) -> insertOrders.addAll(value));
        sellOrders.forEach((key, value) -> insertOrders.addAll(value));
        CompletableFuture<List<Orders>> insertOrdersTask = repository.insertBulkOrders(insertOrders);
        CompletableFuture<List<Orders>> updateOrdersTask = repository.updateBulkOrders(updateOrders);
        CompletableFuture.allOf(insertOrdersTask,updateOrdersTask);
    }

    private Map<Orders, Orders> getMatchedOrders(Map<StockPriceGroup, List<Orders>> buyOrders,
                                                 Map<StockPriceGroup, List<Orders>> sellOrders) {
        HashMap<Orders, Orders> ordersMap = new HashMap<>();
        final List<Orders> deleteBuyOrders = new ArrayList<>();
        buyOrders.forEach((key, value) -> {
            deleteBuyOrders.clear();
            if (sellOrders.containsKey(key)) {
                value.stream().forEach(buyerOrder -> {
                    if (sellOrders.containsKey(key)) {
                        Orders sellerOrder = sellOrders.get(key).parallelStream().sorted(new OrdersIdComparator())
                                .findFirst().orElse(null);
                        sellerOrder.setStatus(OrdersStatus.SOLD);
                        buyerOrder.setStatus(OrdersStatus.SOLD);
                        ordersMap.put(buyerOrder, sellerOrder);
                        sellOrders.get(key).remove(sellerOrder);
                        if (sellOrders.get(key).size() == 0) {
                            sellOrders.remove(key);
                        }
                        deleteBuyOrders.add(buyerOrder);
                        if (sellerOrder.getId() != 0) {
                            updateOrders.add(sellerOrder);
                        } else {
                            insertOrders.add(sellerOrder);
                        }
                        if (buyerOrder.getId() != 0) {
                            updateOrders.add(buyerOrder);
                        } else {
                            insertOrders.add(buyerOrder);
                        }
                    }
                });
            }
            value.removeAll(deleteBuyOrders);
        });
        return ordersMap;
    }

    @Async
    private CompletableFuture<List<Trades>> createTradeRecordsAndInsert(HashMap<Orders, Orders> ordersMap) {
        Trades trades;
        Orders buyer, seller;
        ArrayList<Trades> tradeList = new ArrayList<>();
        for (Map.Entry<Orders, Orders> entry : ordersMap.entrySet()) {
            trades = new Trades();
            buyer = entry.getKey();
            seller = entry.getValue();
            trades.setBuyerName(buyer.getPartyName());
            trades.setSellerName(seller.getPartyName());
            trades.setPrice(seller.getPrice());
            trades.setStock(seller.getStock());
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            trades.setDate(LocalDate.parse(date.format(formatter), formatter));
            tradeList.add(trades);
        }
        return CompletableFuture.completedFuture(repository.insertBulkTradesRecords(tradeList));
    }

    @Async
    public CompletableFuture<List<Orders>> getOrdersByPrice(double price) {
        return CompletableFuture.completedFuture(repository.findByPrice(price));
    }

    @Async
    public CompletableFuture<List<Orders>> getOrdersByStock(String stock) {
        return CompletableFuture.completedFuture(repository.findByStock(stock));
    }

    @Async
    public CompletableFuture<List<Trades>> getTradesByStock(String stock) {
        return CompletableFuture.completedFuture(repository.findByTradesStock(stock));
    }

    @Async
    public CompletableFuture<List<Trades>> getTradesByDate(LocalDate date) {
        return CompletableFuture.completedFuture(repository.findByTradesDate(date));
    }

    @Async
    public CompletableFuture<List<Trades>> getTradesByBuyerName(String buyerName) {
        return CompletableFuture.completedFuture(repository.findByTradesBuyerName(buyerName));
    }

    @Async
    public CompletableFuture<List<Trades>> getTradesBySellerName(String sellerName) {
        return CompletableFuture.completedFuture(repository.findByTradesSellerName(sellerName));
    }

    @Async
    public CompletableFuture<List<Trades>> getTradesByParty(String buyerName, String sellerName) {
        return CompletableFuture.completedFuture(repository.findByTradesParties(buyerName, sellerName));
    }

    static class OrdersIdComparator implements Comparator<Orders> {
        @Override
        public int compare(Orders orders1, Orders orders2) {
            if (orders1.getId() > orders2.getId()) {
                return 1;
            } else if (orders1.getId() < orders2.getId()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
