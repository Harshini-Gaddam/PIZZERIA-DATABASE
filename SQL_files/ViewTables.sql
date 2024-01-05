-- Harshini Gaddam
-- updated View Tables

USE PIZZERIA;

SELECT * FROM topping;
SELECT * FROM pizza;
SELECT * FROM pizzatoppings;
SELECT * FROM baseprice;
SELECT * FROM discount;
-- discount table as required 
SELECT DiscountName, CASE WHEN isPercent=1 THEN Amount ELSE NULL
END AS isPercent,
CASE WHEN isPercent=0 THEN Amount ELSE NULL
END AS Amount
FROM discount;

SELECT * FROM pizzadiscount;
SELECT * FROM customer;
SELECT * FROM orders;
SELECT * FROM orderdiscount;
SELECT * FROM dinein;
SELECT * FROM pickup;
SELECT * FROM delivery;