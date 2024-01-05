-- Harshini Gaddam
-- updated Drop tables


use PIZZERIA;

-- Drop tables with no foreign key dependencies
DROP TABLE IF EXISTS pizzadiscount;
DROP TABLE IF EXISTS pizzatoppings;
DROP TABLE IF EXISTS dinein;
DROP TABLE IF EXISTS pickup;
DROP TABLE IF EXISTS delivery;
DROP TABLE IF EXISTS orderdiscount;

-- Drop tables with foreign key dependencies
DROP TABLE IF EXISTS pizza;
DROP TABLE IF EXISTS topping;
DROP TABLE IF EXISTS baseprice;
DROP TABLE IF EXISTS discount;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customer;
