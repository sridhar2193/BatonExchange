package com.baton.Exchange;

import com.baton.Exchange.controller.ExchangeResource;
import com.baton.Exchange.model.Orders;
import com.baton.Exchange.model.Trades;
import com.baton.Exchange.service.ExchangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ExchangeResource.class)
public class ExchangeResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeService exchangeService;

    @Before
    public void setUp(){
        Mockito.when(exchangeService.buyOrders(Mockito.anyList())).thenReturn(CompletableFuture.completedFuture(createExpectedTrade()));
        Mockito.when(exchangeService.getOrdersByPrice(Mockito.anyDouble())).thenReturn(CompletableFuture.completedFuture(createExpectedOrders()));
    }

    @Test
    public void createProduct() throws Exception {
        List<Orders> orders = new ArrayList<>();
        orders.add(new Orders("Party A", "SELL", "IBM", 110d, "UNSOLD"));
        orders.add(new Orders("Party A", "SELL", "INFY", 600d, "UNSOLD"));
        orders.add(new Orders("Party A", "SELL", "GOOG", 500d, "UNSOLD"));
        orders.add(new Orders("Party B", "BUY", "IBM", 110d, "UNSOLD"));
        orders.add(new Orders("Party C", "BUY", "IBM", 110d, "UNSOLD"));
        orders.add(new Orders("Party C", "BUY", "INFY", 600d, "UNSOLD"));
        ObjectMapper mapper = new ObjectMapper();
        String inputJson = mapper.writeValueAsString(orders);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8085/Exchange/feedOrders")
                .accept(MediaType.APPLICATION_JSON).content(inputJson)
                .characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String expected = "[Trades{id=0, sellerName='Party A', buyerName='party B', stock='IBM', price=110.0, date=2020-07-07}]";
        assertEquals(expected, result.getAsyncResult().toString());
    }

    @Test
    public void getOrderByPriceList() throws Exception {

        String uri = "/Exchange/getOrdersByPrice/110";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                uri).characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[Orders{id=0, partyName='Party A', orderType=SELL, stock='IBM', price=110.0, status=UNSOLD}]";
        assertEquals(expected, result.getAsyncResult().toString());
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }


    public static List<Orders> createExpectedOrders(){
        return Arrays.asList(new Orders("Party A", "SELL", "IBM", 110d, "UNSOLD"));
    }

    public static List<Trades> createExpectedTrade(){
        return Arrays.asList(new Trades("Party A", "party B", "IBM", 110d, LocalDate.now()));
    }
}
