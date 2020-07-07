package com.baton.Exchange;

import com.baton.Exchange.dao.ExchangeRepository;
import com.baton.Exchange.model.Trades;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class ExchangeRepositoryTest {

    @InjectMocks
    private ExchangeRepository repository;

    @Mock
    NamedParameterJdbcTemplate parameterJdbcTemplate;

    private int[] updateCounts = {1};

    @Before
    public void setUp(){
        Mockito.when(parameterJdbcTemplate.query(Mockito.anyString(),Mockito.anyMap(),Mockito.any(RowMapper.class)))
                .thenReturn(ExchangeResourceTest.createExpectedOrders());
        Mockito.when(parameterJdbcTemplate.batchUpdate(Mockito.anyString(),Mockito.any(SqlParameterSource[].class))).thenReturn(updateCounts);
    }

    @Test
    public void testUnSoldOrders() {
        String expected = "[Orders{id=0, partyName='Party A', orderType=SELL, stock='IBM', price=110.0, status=UNSOLD}]";
        assertEquals(expected, repository.getUnSoldOrders().toString());
    }

    @Test
    public void testInsertBulkRecords() {
        List<Trades> orders = Arrays.asList(new Trades("Party A", "Party B", "IBM", 110d, LocalDate.now()));
        String expected = "[Trades{id=0, sellerName='Party A', buyerName='Party B', stock='IBM', price=110.0, date=2020-07-07}]";
        assertEquals(expected, repository.insertBulkTradesRecords(orders).toString());
    }
}
