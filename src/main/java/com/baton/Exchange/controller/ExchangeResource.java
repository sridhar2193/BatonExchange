package com.baton.Exchange.controller;

import com.baton.Exchange.model.Orders;
import com.baton.Exchange.model.Trades;
import com.baton.Exchange.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/Exchange")
public class ExchangeResource {

    @Autowired
    ExchangeService exchangeService;

    @GetMapping("/getOrdersByPrice/{price}")
    public CompletableFuture<List<Orders>> getOrdersByPrice(@PathVariable String price){
        return exchangeService.getOrdersByPrice(Double.parseDouble(price));
    }

    @GetMapping("/getOrdersByStock/{stock}")
    public CompletableFuture<List<Orders>> getOrdersByStock(@PathVariable String stock){
        return exchangeService.getOrdersByStock(stock);
    }

    @GetMapping("/getTradesByStock/{stock}")
    public CompletableFuture<List<Trades>> getTradesByStock(@PathVariable String stock){
        return exchangeService.getTradesByStock(stock);
    }

    @GetMapping("/getTradesByDate/{date}")
    public CompletableFuture<List<Trades>> getTradesByDate(@PathVariable String date){
        return exchangeService.getTradesByDate(LocalDate.parse(date));
    }

    @GetMapping("/getTradesByBuyerName/{buyerName}")
    public CompletableFuture<List<Trades>> getTradesByBuyerName(@PathVariable String buyerName){
        return exchangeService.getTradesByBuyerName(buyerName);
    }

    @GetMapping("/getTradesBySellerName/{sellerName}")
    public CompletableFuture<List<Trades>> getTradesBySellerName(@PathVariable String sellerName){
        return exchangeService.getTradesBySellerName(sellerName);
    }

    @GetMapping("/getTradesByParty/{buyerName}/{sellerName}")
    public CompletableFuture<List<Trades>> getTradesByParty(@PathVariable String buyerName, @PathVariable String sellerName){
        return exchangeService.getTradesByParty(buyerName,sellerName);
    }

    @Async
    @PostMapping(value = "/feedOrders",consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<List<Trades>> feedOrders(@RequestBody List<Orders> orders){
        return exchangeService.buyOrders(orders);
    }
}
