# Pizzeria Database Management System

This project implements a database management system for a pizzeria using MySQL, JDBC, and Java.

## Overview

The system is designed to manage various aspects of a pizzeria's operations, including order processing, inventory management, and customer data. It's built using an Entity-Relationship (ER) model, implemented in MySQL, and accessed through a Java application using JDBC.

## Features

- ER model design for a pizzeria database
- MySQL database implementation
- Java application for database interaction
- JDBC connection for secure and efficient database operations

## Technologies Used

- MySQL
- Java
- JDBC
- LucidChart to draw data model

## Setup

1. Clone this repository
2. Ensure you have MySQL installed and running
3. Update the `DBConnector.java` file with your database credentials:
   - Update `user` with your MySQL username
   - Update `password` with your MySQL password
   - Update `database_name` if different from "PIZZERIA"
   - Update `url` if your database is hosted differently

## Results and Output

This project demonstrates a fully functional integration between a Java application and a MySQL database hosted on AWS RDS. Key outcomes include:

1. Real-time Database Interactions:
   - Data entered through the Java console is immediately reflected in the MySQL database.
   - This ensures up-to-date information for the pizzeria's operations.

2. Query Execution:
   - The system can execute a variety of SQL queries written in Java using JDBC.
   - These queries can retrieve, insert, update, or delete data as needed.

3. AWS RDS Integration:
   - Successful connection and interaction with a MySQL database hosted on AWS RDS.
   - This cloud-based approach allows for scalable and reliable database operations.

4. Data Management:
   - Efficient handling of pizzeria-related data such as orders, inventory, customer information, etc.
   - The ER model ensures proper relationships between different data entities.

5. User Interaction:
   - The Java console provides a user-friendly interface for database operations.
   - Users can input data, which is then processed and stored in the database.

## Sample Outputs

Here are some example scenarios of how the system operates:

1. Order Processing:
Enter customer name: John Doe
Enter pizza type: Margherita
Enter quantity: 2
Order confirmed! Order ID: 1001

2. Inventory Check:
Current stock for Tomato Sauce: 500 units
Current stock for Mozzarella Cheese: 750 units

3. Daily Sales Report:
Total orders today: 45
Total revenue: $1,250.75
Most popular pizza: Pepperoni

These outputs demonstrate how the Java application interacts with the MySQL database, allowing for real-time data management and reporting for the pizzeria.
