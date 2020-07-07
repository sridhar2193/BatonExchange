package com.baton.Exchange.dao;

import com.baton.Exchange.mapper.OrdersMapper;
import com.baton.Exchange.mapper.TradesMapper;
import com.baton.Exchange.model.Orders;
import com.baton.Exchange.model.Trades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class ExchangeRepository {

    @Autowired
    NamedParameterJdbcTemplate parameterJdbcTemplate;

    private final Logger logger = LoggerFactory.getLogger(ExchangeRepository.class);

    public List<Orders> findByPrice(double price) {
        logger.info("findByPrice Method execute {}", price);
        String query = "SELECT * FROM orders WHERE price=:price";
        HashMap<String,Object> parameterMap = new HashMap();
        parameterMap.put("price",price);
        return parameterJdbcTemplate.query(query, parameterMap, new OrdersMapper());
    }

    public List<Orders> findByStock(String stock) {
        logger.info("findByStock Method execute {}",stock);
        String query = "SELECT * FROM orders WHERE stock=:stock";
        HashMap<String,Object> parameterMap = new HashMap();
        parameterMap.put("stock",stock);
        return parameterJdbcTemplate.query(query, parameterMap, new OrdersMapper());
    }

    public List<Trades> findByTradesStock(String stock) {
        logger.info("findByTradesStock Method execute {}",stock);
        String query = "SELECT * FROM trades WHERE stock=:stock";
        HashMap<String,Object> parameterMap = new HashMap();
        parameterMap.put("stock",stock);
        return parameterJdbcTemplate.query(query, parameterMap, new TradesMapper());
    }

    public List<Trades> findByTradesDate(LocalDate date) {
        logger.info("findByTradesDate Method execute {}", date);
        String query = "SELECT * FROM trades WHERE date=:date";
        HashMap<String,Object> parameterMap = new HashMap();
        parameterMap.put("date",date);
        return parameterJdbcTemplate.query(query, parameterMap, new TradesMapper());
    }

    public List<Trades> findByTradesBuyerName(String buyerName) {
        logger.info("findByTradesBuyerName Method execute {}", buyerName);
        String query = "SELECT * FROM trades WHERE buyer_name=:buyerName";
        HashMap<String,Object> parameterMap = new HashMap();
        parameterMap.put("buyerName",buyerName);
        return parameterJdbcTemplate.query(query, parameterMap, new TradesMapper());
    }

    public List<Trades> findByTradesSellerName(String sellerName) {
        logger.info("findByTradesSellerName Method execute {}", sellerName);
        String query = "SELECT * FROM trades WHERE seller_name=:sellerName";
        HashMap<String,Object> parameterMap = new HashMap();
        parameterMap.put("sellerName",sellerName);
        return parameterJdbcTemplate.query(query, parameterMap, new TradesMapper());
    }

    public List<Trades> findByTradesParties(String buyerName, String sellerName) {
        logger.info("findByTradesParties Method execute {} and {}", buyerName, sellerName);
        String query = "SELECT * FROM trades WHERE buyer_name=:buyerName AND seller_name=:sellerName";
        HashMap<String,Object> parameterMap = new HashMap();
        parameterMap.put("sellerName",sellerName);
        parameterMap.put("buyerName",buyerName);
        return parameterJdbcTemplate.query(query, parameterMap, new TradesMapper());
    }

    public List<Trades> insertBulkTradesRecords(List<Trades> trades){
        logger.info("insertBulkTradesRecords Method execute size {}", trades.size());
        String query = "INSERT INTO trades (seller_name, buyer_name, stock, price, date)" +
                " VALUES (:sellerName, :buyerName, :stock, :price, :date)";
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(trades.toArray());
        int[] updateCounts = parameterJdbcTemplate.batchUpdate(query, batch);
        if(updateCounts.length==trades.size()){
            logger.info("successful update");
        }else{
            logger.error("Some data were not updated");
        }
        return trades;
    }

    public CompletableFuture<List<Orders>> insertBulkOrders(List<Orders> orders){
        logger.info("insertBulkOrders Method execute size {}", orders.size());
        String query = "INSERT INTO orders(party_name,order_type,stock,price,status) VALUES (?,?,?,?,?)";
        int[][] updateCounts = parameterJdbcTemplate.getJdbcOperations().batchUpdate(query,
                orders,
                orders.size(),
                (ps,order)->{
                    ps.setString(1, order.getPartyName());
                    ps.setString(2, order.getOrderType().name());
                    ps.setString(3, order.getStock());
                    ps.setDouble(4, order.getPrice());
                    ps.setString(5, order.getStatus().name());
                });
        if(updateCounts[0].length == orders.size()){
            logger.info("success insert");
        }else{
            logger.error("Some data were not inserted");
        }
        return CompletableFuture.completedFuture(orders);
    }

    public CompletableFuture<List<Orders>> updateBulkOrders(List<Orders> orders){
        logger.info("updateBulkOrders Method execute size {}", orders.size());
        String query = "UPDATE orders set status = ? where id = ?";
        int[][] updateCounts = parameterJdbcTemplate.getJdbcOperations().batchUpdate(query,
                orders,
                orders.size(),
                (ps,order)->{
                    ps.setString(1, "SOLD");
                    ps.setInt(2, order.getId());
                });
        if(updateCounts.length == orders.size()){
            logger.info("success insert");
        }else{
            logger.error("Some data were not inserted");
        }
        return CompletableFuture.completedFuture(orders);
    }

    public List<Orders> getUnSoldOrders(){
        logger.info("getUnSoldOrders Method execute");
        String query = "SELECT * FROM orders WHERE status=:status";
        HashMap<String,Object> parameterMap = new HashMap();
        parameterMap.put("status","UNSOLD");
        return parameterJdbcTemplate.query(query, parameterMap, new OrdersMapper());
    }

}
