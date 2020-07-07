package com.baton.Exchange;

import com.baton.Exchange.dao.ExchangeRepository;
import com.baton.Exchange.service.ExchangeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class ExchangeServiceTest {

    @InjectMocks
    private ExchangeService exchangeService;

    @Mock
    private ExchangeRepository repository;

    @Before
    public void setUp(){
        Mockito.when(repository.findByStock(Mockito.anyString())).thenReturn(ExchangeResourceTest.createExpectedOrders());
        Mockito.when(repository.findByTradesSellerName(Mockito.anyString())).thenReturn(ExchangeResourceTest.createExpectedTrade());
    }

    @Test
    public void getOrdersByStock() throws ExecutionException, InterruptedException {
        String expected = "[Orders{id=0, partyName='Party A', orderType=SELL, stock='IBM', price=110.0, status=UNSOLD}]";
        assertEquals(expected, exchangeService.getOrdersByStock("IBM").get().toString());
    }

    @Test
    public void getTradesBySellerName() throws ExecutionException, InterruptedException {
        String expected = "[Trades{id=0, sellerName='Party A', buyerName='party B', stock='IBM', price=110.0, date=2020-07-07}]";
        assertEquals(expected, exchangeService.getTradesBySellerName("Party A").get().toString());
    }
}
