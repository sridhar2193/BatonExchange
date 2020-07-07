package com.baton.Exchange.mapper;

import com.baton.Exchange.model.Trades;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TradesMapper implements RowMapper<Trades> {

    @Override
    public Trades mapRow(ResultSet rs, int rowNum) throws SQLException {
        Trades trades = new Trades();
        trades.setId(rs.getInt("id"));
        trades.setSellerName(rs.getString("seller_name"));
        trades.setBuyerName(rs.getString("buyer_name"));
        trades.setStock(rs.getString("stock"));
        trades.setPrice(rs.getDouble("price"));
        trades.setDate(rs.getDate("date").toLocalDate());
        return trades;
    }
}
