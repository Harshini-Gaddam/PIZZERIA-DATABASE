-- Harshini Gaddam
-- Updated populated tables


use PIZZERIA;
INSERT INTO topping( TopName, CustPrice, BusPrice, CurINVT, MinINVT, 
PerAMT, MedAMT, LgAMT, XLAMT)
VALUES 
('Pepperoni', 1.25, 0.2, 100, 50, 2, 2.75, 3.5, 4.5),
('Sausage', 1.25, 0.15, 100, 50, 2.5, 3, 3.5, 4.25),
('Ham', 1.5, 0.15, 78, 25, 2, 2.5, 3.25, 4),
('Chicken', 1.75, 0.25, 56, 25, 1.5, 2, 2.25, 3),
('Green Pepper', 0.5, 0.02, 79, 25, 1, 1.5, 2, 2.5),
('Onion', 0.5, 0.02, 85, 25, 1, 1.5, 2, 2.75),
('Roma Tomato', 0.75, 0.03, 86, 10, 2, 3, 3.5, 4.5),
('Mushrooms', 0.75, 0.1, 52 ,50, 1.5, 2, 2.5, 3),
('Black Olives', 0.6, 0.1, 39, 25, 0.75, 1, 1.5, 2),
('Pineapple', 1, 0.25, 15, 0, 1, 1.25, 1.75, 2),
('Jalapenos', 0.5, 0.05, 64, 0, 0.5, 0.75, 1.25, 1.75),
('Banana Peppers', 0.5, 0.05 ,36, 0, 0.6, 1, 1.3, 1.75),
('Regular Cheese', 0.5, 0.12, 250, 50, 2, 3.5, 5, 7),
('Four Cheese Blend', 1, 0.15, 150, 25, 2 ,3.5 ,5 ,7),
('Feta Cheese', 1.5, 0.18, 75, 0, 1.75, 3, 4, 5.5),
('Goat Cheese', 1.5, 0.2, 54, 0, 1.6, 2.75, 4, 5.5),
('Bacon', 1.5, 0.25, 89, 0, 1, 1.5, 2, 3); 

-- SELECT * FROM topping;

INSERT INTO discount(DiscountName, isPercent, Amount)
VALUES
('Employee', TRUE, 15),
('Lunch Special Medium', FALSE, 1.00),
('Lunch Special Large' ,FALSE, 2.00),
('Specialty Pizza', FALSE, 1.50),
('Happy Hour', TRUE, 10),
('Gameday Special', TRUE, 20);

-- SELECT * FROM discount;

INSERT INTO baseprice(BaseSize, CrustType, CustPrice, BusPrice)
VALUES
('small', 'Thin', 3, 0.5),
('small', 'Original', 3, 0.75),
('small', 'Pan', 3.5, 1),
('small', 'Gluten-Free', 4, 2),
('medium', 'Thin', 5, 1),
('medium', 'Original', 5, 1.5),
('medium', 'Pan', 6, 2.25),
('medium', 'Gluten-Free', 6.25, 3),
('large', 'Thin', 8, 1.25),
('large', 'Original', 8, 2),
('large', 'Pan', 9, 3),
('large', 'Gluten-Free', 9.5, 4),
('x-large', 'Thin', 10, 2),
('x-large', 'Original', 10, 3),
('x-large', 'Pan', 11.5, 4.5),
('x-large', 'Gluten-Free', 12.5, 6);

-- SELECT * FROM topping
-- SELECT * FROM discount
-- SELECT * FROM baseprice;

-- Values into orderpizza table
-- --ORDER 1--
/* On March 5th at 12:03 pm there was a dine-in order (at table 21) for a large thin crust pizza with Regular
Cheese (extra), Pepperoni, and Sausage (Price: $20.75, Cost: $3.68). They used the “Lunch Special Large”
discount for the pizza */
use PIZZERIA;
INSERT INTO customer(CustID)
VALUES(1);

INSERT INTO orders(CustID, OrderTimeStamp, isComplete, CustPrice , BusPrice, OrderType)
VALUES
(1, '2023-03-05 12:03:00', 1, 20.75, 3.68, 'dinein'); 

INSERT INTO dinein(OrderID, TableNum)
VALUES
(1, 21);

INSERT INTO pizza(Size, CrustType, OrderID, PizzaState, PizzaDate, BusPrice, CustPrice)
VALUES
("large", "Thin", 1, "Completed", '2023-03-05 12:03:00', 3.68, 20.75);

INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 1, TRUE 
from topping WHERE topping.TopName = 'Regular Cheese';

INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 1, FALSE 
from topping WHERE topping.TopName = 'Pepperoni';

INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 1, FALSE 
from topping WHERE topping.TopName = 'Sausage';

INSERT INTO pizzadiscount
(PizzaID, DiscountID)
SELECT 1, discount.DiscountID
FROM discount
WHERE DiscountName="Lunch Special Large";

-- SELECT * FROM customer

-- -- ORDER 2--
/*On April 3rd at 12:05 pm there was a dine-in order (at table 4). They ordered a medium pan pizza with
Feta Cheese, Black Olives, Roma Tomatoes, Mushrooms and Banana Peppers (Price: $12.85, Cost: $3.23).
They used the “Lunch Special Medium” and the “Specialty Pizza” discounts for the pizza. They also ordered
a small original crust pizza with Regular Cheese, Chicken and Banana Peppers (Price: $6.93, Cost: $1.40)*/
use PIZZERIA;
INSERT INTO customer(CustID)
VALUES(2);

INSERT INTO orders(CustID, OrderTimeStamp, isComplete, CustPrice , BusPrice, OrderType)
VALUES
(2, '2023-04-03 12:05:00', 1, 19.78, 4.63, 'dinein'); 

INSERT INTO dinein(OrderID, TableNum)
VALUES
(2, 4);

INSERT INTO pizza(Size, CrustType, OrderID, PizzaState, PizzaDate, BusPrice, CustPrice)
VALUES
("medium", "Pan", 2, "Completed", '2023-04-03 12:05:00', 3.23, 12.85);

INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 2, FALSE 
FROM topping WHERE topping.TopName IN ('Feta Cheese','Black Olives',
'Roma Tomato','Mushrooms','Banana Peppers');

INSERT INTO pizzadiscount
(PizzaID, DiscountID)
SELECT 2, discount.DiscountID
FROM discount
WHERE DiscountName="Lunch Special Medium";

INSERT INTO pizzadiscount
(PizzaID, DiscountID)
SELECT 2, discount.DiscountID
FROM discount
WHERE DiscountName="Specialty Pizza";

-- pizza 2 in the order 3002 
INSERT INTO pizza(Size, CrustType, OrderID, PizzaState, PizzaDate, BusPrice, CustPrice)
VALUES
("small", "Original", 2, "Completed", '2023-04-03 12:05:00', 1.40, 6.93);

INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 3, FALSE 
FROM topping WHERE topping.TopName IN ('Regular Cheese',
'Chicken','Banana Peppers');



-- --ORDER 3--
/* On March 3rd at 9:30 pm Andrew Wilkes-Krier placed an order for pickup of 6 large original crust pizzas
with Regular Cheese and Pepperoni (Price: $14.88, Cost: $3.30 each). Andrew’s phone number is 864-254-
5861 */
use PIZZERIA;
INSERT INTO customer(CustID, FName, LName, Phone)
VALUES(3, "Andrew", "Wilkes-Krier", 8642545861);

INSERT INTO orders(CustID, OrderTimeStamp, isComplete, CustPrice , BusPrice, OrderType)
VALUES
(3, '2023-03-03 21:30:00', 1, 89.28, 19.8, 'pickup'); 

INSERT INTO pizza(Size, CrustType, OrderID, PizzaState, PizzaDate, BusPrice, CustPrice)
VALUES
("large", "Original", 3, "Completed", '2023-03-03 21:30:00', 3.30, 14.88),
("large", "Original", 3, "Completed", '2023-03-03 21:30:00', 3.30, 14.88),
("large", "Original", 3, "Completed", '2023-03-03 21:30:00', 3.30, 14.88),
("large", "Original", 3, "Completed", '2023-03-03 21:30:00', 3.30, 14.88),
("large", "Original", 3, "Completed", '2023-03-03 21:30:00', 3.30, 14.88),
("large", "Original", 3, "Completed", '2023-03-03 21:30:00', 3.30, 14.88);

INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 4, FALSE 
FROM topping WHERE topping.TopName IN ('Regular Cheese','Pepperoni');
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 5, FALSE 
FROM topping WHERE topping.TopName IN ('Regular Cheese','Pepperoni');
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 6, FALSE 
FROM topping WHERE topping.TopName IN ('Regular Cheese','Pepperoni');
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 7, FALSE 
FROM topping WHERE topping.TopName IN ('Regular Cheese','Pepperoni');
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 8, FALSE 
FROM topping WHERE topping.TopName IN ('Regular Cheese','Pepperoni');
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 9, FALSE 
FROM topping WHERE topping.TopName IN ('Regular Cheese','Pepperoni');

INSERT INTO pickup(OrderID, CustID)
VALUES
(3, 3);

/* SELECT * FROM customer;
SELECT * FROM orders;
SELECT * FROM pizza;
SELECT * FROM pizzatoppings;

-- -- ORDER 4 --
/* On April 20th at 7:11 pm there was a delivery order made by Andrew Wilkes-Krier for 1 xlarge pepperoni
and Sausage pizza (Price: $27.94, Cost: $9.19), one xlarge pizza with Ham (extra) and Pineapple (extra)
pizza (Price: $31.50, Cost: $6.25), and one xlarge Chicken and Bacon pizza (Price: $26.75, Cost: $8.18). All
the pizzas have the Four Cheese Blend on it and are original crust. The order has the “Gameday Special”
discount applied to it, and the ham and pineapple pizza has the “Specialty Pizza” discount applied to it. The
pizzas were delivered to 115 Party Blvd, Anderson SC 29621. His phone number is the same as before */

use PIZZERIA;
UPDATE customer
SET Address = "115 Party Blvd, Anderson SC, 29621" 
WHERE CustID=3;

INSERT INTO orders(CustID, OrderTimeStamp, isComplete, CustPrice , BusPrice, OrderType)
VALUES
(3, '2023-04-20 19:11:00', 1, 86.19, 23.62, 'delivery'); 

INSERT INTO pizza(Size, CrustType, OrderID, PizzaState, PizzaDate, BusPrice, CustPrice)
VALUES
("x-large", "Original", 4, "Completed", '2023-04-20 19:11:00', 9.19, 27.94),
("x-large", "Original", 4, "Completed", '2023-04-20 19:11:00', 6.25, 31.50),
("x-large", "Original", 4, "Completed", '2023-04-20 19:11:00', 8.18, 26.75);

INSERT INTO delivery(OrderID, CustID, CustAddress)
VALUES
(4, 3, "115 Party Blvd, Anderson SC, 29621");

INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 10, FALSE 
FROM topping WHERE topping.TopName IN ('Pepperoni', 'Sausage');
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 11, TRUE 
FROM topping WHERE topping.TopName IN ('Ham', 'Pineapple');
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 12, FALSE 
FROM topping WHERE topping.TopName IN ('Chicken', 'Bacon');
-- inserting Four Cheese Blend into 3 pizzas
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 10, FALSE 
FROM topping WHERE topping.TopName="Four Cheese Blend";
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 11, FALSE 
FROM topping WHERE topping.TopName="Four Cheese Blend";
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 12, FALSE 
FROM topping WHERE topping.TopName="Four Cheese Blend";

INSERT INTO orderdiscount(OrderID, DiscountID)
SELECT 4, discount.DiscountID
FROM discount
WHERE DiscountName="Gameday Special";

INSERT INTO pizzadiscount
(PizzaID, DiscountID)
SELECT 11, discount.DiscountID
FROM discount
WHERE DiscountName="Specialty Pizza";

-- -- ORDER 5--
/* On March 2nd at 5:30 pm Matt Engers placed an order for pickup for an xlarge pizza with Green Pepper,
Onion, Roma Tomatoes, Mushrooms, and Black Olives on it. He wants the Goat Cheese on it, and a Gluten
Free Crust (Price: $27.45, Cost: $7.88). The “Specialty Pizza” discount is applied to the pizza. Matt’s phone
number is 864-474-9953 */

use PIZZERIA;
INSERT INTO customer(CustID, FName, LName, Phone)
VALUES(4, "Matt", "Engers", 8644749953);

INSERT INTO orders(CustID, OrderTimeStamp, isComplete, CustPrice , BusPrice, OrderType)
VALUES
(4, '2023-03-02 17:30:00', 1, 27.45, 7.88, 'pickup');

INSERT INTO pickup(OrderID, CustID)
VALUES(5, 4); 

INSERT INTO pizza(Size, CrustType, OrderID, PizzaState, PizzaDate, BusPrice, CustPrice)
VALUES
("x-large", "Gluten-Free", 5, "Completed", '2023-03-02 17:30:00', 7.88, 27.45);

INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 13, FALSE 
FROM topping WHERE topping.TopName IN ('Green Pepper', 'Onion', 'Roma Tomato', 
'Mushrooms', 'Black Olives', 'Goat Cheese');

INSERT INTO pizzadiscount(PizzaID, DiscountID)
SELECT 13, discount.DiscountID
FROM discount
WHERE DiscountName="Specialty Pizza";

-- -- ORDER 6--
/* On March 2nd at 6:17 pm Frank Turner places an order for delivery of one large pizza with Chicken, Green
Peppers, Onions, and Mushrooms. He wants the Four Cheese Blend (extra) and thin crust (Price: $25.81,
Cost: $4.24). The pizza was delivered to 6745 Wessex St Anderson SC 29621. Frank’s phone number is 864-
232-8944 */

USE PIZZERIA;
INSERT INTO customer(CustID, FName, LName, Phone, Address)
VALUES(5, 'Frank', 'Turner', 8642328944, "6745 Wessex St, Anderson SC 29621");

INSERT INTO orders(CustID, OrderTimeStamp, isComplete, CustPrice , BusPrice, OrderType)
VALUES
(5, '2023-03-02 18:17:00', 1, 25.81, 4.24, 'delivery'); 

INSERT INTO delivery(OrderID, CustID, CustAddress)
VALUES(6, 5, "6745 Wessex St, Anderson SC 29621"); 

INSERT INTO pizza(Size, CrustType, OrderID, PizzaState, PizzaDate, BusPrice, CustPrice)
VALUES
("large", "Thin", 6, "Completed", '2023-03-02 18:17:00', 4.24, 25.81);

INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 14, FALSE 
FROM topping WHERE topping.TopName IN ('Chicken', 'Green Pepper', 'Onion', 'Mushrooms');
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 14, TRUE 
FROM topping WHERE topping.TopName="Four Cheese Blend";

-- -- 0RDER 7--
/* On April 13th at 8:32 pm Milo Auckerman ordered two large thin crust pizzas. One had the Four Cheese
Blend on it (extra) (Price: $18.00, Cost: $2.75), the other was Regular Cheese and Pepperoni (extra) (Price:
$19.25, Cost: $3.25). He used the “Employee” discount on his order. He had them delivered to 8879
Suburban Home, Anderson, SC 29621. His phone number is 864-878-5679 */
USE PIZZERIA;
INSERT INTO customer(CustID, FName, LName, Phone, Address)
VALUES(6, 'Milo', 'Auckerman', 8648785679, "8879 Suburban Home, Anderson, SC, 29621");

INSERT INTO orders(CustID, OrderTimeStamp, isComplete, CustPrice , BusPrice, OrderType)
VALUES
(6, '2023-04-13 20:32:00', 1, 37.25, 6.00, 'delivery'); 

INSERT INTO delivery(OrderID, CustID, CustAddress)
VALUES(7, 6, "8879 Suburban Home, Anderson, SC, 29621" ); 

INSERT INTO pizza(Size, CrustType, OrderID, PizzaState, PizzaDate, BusPrice, CustPrice)
VALUES
("large", "Thin", 7, "Completed", '2023-04-13 20:32:00', 2.75, 18.00),
("large", "Thin", 7, "Completed", '2023-04-13 20:32:00', 3.25, 19.25);

INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 15, TRUE 
FROM topping WHERE topping.TopName='Four Cheese Blend';
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 16, TRUE 
FROM topping WHERE topping.TopName="Pepperoni";
INSERT INTO pizzatoppings(TopID,PizzaID,Isextra)
SELECT topping.TopID, 16, FALSE 
FROM topping WHERE topping.TopName="Regular Cheese";

INSERT INTO orderdiscount(OrderID, DiscountID)
SELECT 7, discount.DiscountID
FROM discount
WHERE DiscountName="Employee";

-- SELECT * FROM pizzatoppings










