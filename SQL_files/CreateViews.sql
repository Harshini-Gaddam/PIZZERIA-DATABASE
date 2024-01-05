-- Harshini Gaddam
-- updated Create Views


USE PIZZERIA;

-- Creating View ToppingPopularity
CREATE OR REPLACE VIEW ToppingPopularity AS
SELECT
    topping.TopName AS Topping,
    COALESCE(COUNT(*) + SUM(pizzatoppings.Isextra), 0) AS ToppingCount
FROM topping
LEFT JOIN pizzatoppings ON topping.TopID = pizzatoppings.TopID
GROUP BY topping.TopName
ORDER BY ToppingCount DESC;


-- Creating view ProfitByPizza
CREATE OR REPLACE VIEW ProfitByPizza AS
SELECT D.Size, D.Crust, SUM(D.Profit) AS "Profit", D.OrderMonth AS 'Order Month'
FROM (
    SELECT
        pizza.Size AS Size,
        pizza.CrustType AS Crust,
        SUM(pizza.CustPrice - pizza.BusPrice) AS Profit,
        CONCAT(MONTH(O.OrderTimeStamp), "/", YEAR(O.OrderTimeStamp)) AS 'OrderMonth'
    FROM pizza
    JOIN orders O ON O.OrderID = pizza.OrderID
    GROUP BY pizza.Size, pizza.CrustType
) AS D
GROUP BY Size, Crust, 'Order Month'
ORDER BY Profit DESC;


-- Creating View ProfitByOrderType
CREATE OR REPLACE VIEW ProfitByOrderType AS 
SELECT 
    OrderType AS 'customerType',
    CONCAT(MONTH(OrderTimeStamp), "/", YEAR(OrderTimeStamp)) AS 'OrderMonth', 
    SUM(CustPrice) AS 'TotalOrderPrice',
    SUM(BusPrice) AS 'TotalOrderCost', 
    SUM(CustPrice - BusPrice) AS 'Profit'
FROM orders
GROUP BY OrderMonth, OrderType

UNION

SELECT 
    '', 'GrandTotal', 
    SUM(CustPrice), 
    SUM(BusPrice), 
    SUM(CustPrice - BusPrice) AS 'Profit'
FROM orders;


-- SELECT * FROM ToppingPopularity;
-- SELECT * FROM ProfitByPizza;
-- SELECT * FROM ProfitByOrderType;
