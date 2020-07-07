package com.baton.Exchange.mapper;

import com.baton.Exchange.model.OrderType;
import com.baton.Exchange.model.Orders;
import com.baton.Exchange.model.OrdersStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdersMapper implements RowMapper<Orders> {

    @Override
    public Orders mapRow(ResultSet rs, int rowNum) throws SQLException {
        Orders orders = new Orders();
        orders.setId(rs.getInt("id"));
        orders.setPartyName(rs.getString("party_name"));
        orders.setOrderType(OrderType.valueOf(rs.getString("order_type")));
        orders.setStock(rs.getString("stock"));
        orders.setPrice(rs.getDouble("price"));
        orders.setStatus(OrdersStatus.valueOf(rs.getString("status")));
        return orders;
    }

}
