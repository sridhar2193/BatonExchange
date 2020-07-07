# BatonExchange
Exchange project to match buyer and seller

Exchange project is based on the trading where buy orders and sell orders will be feeded into the channel.

Have to compare the stock or SYMBOL, price and the sequence in which the orders are coming in.

Project is developed using SpringBoot and exposed number of API's to query for Orders and Trades.

I am using inmemory H2 database where created two tables.
1. ORDERS
2. TRADES

TABLE DETAILS:
Table --- ORDERS
COLUMN DETAILS:
id    party_name   order_type   stock    price   status

Table - TRADES
COLUMN DETAILS:
id    buyer_name   seller_name  stock   price   date

To interact with DB, I am using JDBC_Template.

Main application of the project is:
ExchangeApplication.java

Test files added to src/test/java

Testdata included in src/test/resources/testData.json

Used Async CompletedFuture processing to work in multithreaded way and for efficient processing.

