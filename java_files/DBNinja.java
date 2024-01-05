package cpsc4620;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.text.ParseException;


/*
 * This file is where most of your code changes will occur You will write the code to retrieve
 * information from the database, or save information to the database
 * 
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 * 
 * This class also has static string variables for pickup, delivery and dine-in. If your database
 * stores the strings differently (i.e "pick-up" vs "pickup") changing these static variables will
 * ensure that the comparison is checking for the right string in other places in the program. You
 * will also need to use these strings if you store this as boolean fields or an integer.
 * 
 * 
 */

/**
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
	private static Connection conn;
    private static Statement st;
	// Change these variables to however you record dine-in, pick-up and delivery, and sizes and crusts
	public final static String pickup = "pickup";
	public final static String delivery = "delivery";
	public final static String dine_in = "dinein";

	public final static String size_s = "Small";
	public final static String size_m = "Medium";
	public final static String size_l = "Large";
	public final static String size_xl = "XLarge";

	public final static String crust_thin = "Thin";
	public final static String crust_orig = "Original";
	public final static String crust_pan = "Pan";
	public final static String crust_gf = "Gluten-Free";



	
	private static boolean connect_to_db() throws SQLException, IOException {

		try {
			conn = DBConnector.make_connection();
			return true;
		} catch (SQLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	
	public static void addOrder(Order o) throws SQLException, IOException 
	{
		connect_to_db();
		/*
		 * add code to add the order to the DB. Remember that we're not just
		 * adding the order to the order DB table, but we're also recording
		 * the necessary data for the delivery, dinein, and pickup tables
		 * 
		 */
	
		String order_stmt = "insert into Orders(OrderID, CustID, OrderTimeStamp, isComplete, BusPrice, CustPrice, OrderType) values (?,?, NOW(), ?, ?, ?, ?);";
		String dinein_stmt = "insert into dinein(OrderID, TableNum) values (?, ?);";
		String pickup_stmt = "insert into pickup(OrderID, CustID) values(?, ?);";
		String delivery_stmt = "insert into delivery(OrderID, CustID) values (?, ?)";

        try {
			boolean order_compl = false;
			if (o.getIsComplete() == 1) {
			   order_compl = true;
			}
   
			PreparedStatement prepStatement = conn.prepareStatement(order_stmt);
			prepStatement.setInt(1, o.getOrderID());
			prepStatement.setInt(2, o.getCustID());
			prepStatement.setString(3, o.getOrderType());
			prepStatement.setBoolean(4, order_compl);
			prepStatement.setDouble(5, o.getCustPrice());
			prepStatement.setDouble(6, o.getBusPrice());
			int flag_st = prepStatement.executeUpdate();
			if (flag_st == 0) {
			   System.out.println("Error in adding to customerOrder table");
			}
   
			PreparedStatement pic_st;
			if (o instanceof DineinOrder) {
			   pic_st = conn.prepareStatement(dinein_stmt);
			   pic_st.setInt(1, o.getOrderID());
			   pic_st.setInt(2, ((DineinOrder)o).getTableNum());
			   flag_st = pic_st.executeUpdate();
			   if (flag_st == 0) {
				  System.out.println("Error in adding to DineIn Order");
			   }
			} else if (o instanceof PickupOrder) {
			   pic_st = conn.prepareStatement(pickup_stmt);
			   pic_st.setInt(1, o.getOrderID());
			   pic_st.setInt(2, o.getIsComplete());
			   flag_st = pic_st.executeUpdate();
			   if (flag_st == 0) {
				  System.out.println("Error in adding to Pickup Order");
			   }
			} else {
			   pic_st = conn.prepareStatement(delivery_stmt);
			   pic_st.setInt(1, o.getOrderID());
			   pic_st.setInt(2, o.getIsComplete());
			   pic_st.setString(3, getAddressById(o.getOrderID()));
			   flag_st = pic_st.executeUpdate();
			   if (flag_st == 0) {
				  System.out.println("Error in adding to Delivery Order");
			   }
			}
   
			if (flag_st != 0) {
			   System.out.println("Adding DineIn/Pick/Delivery Successful");
			   Iterator var12 = o.getPizzaList().iterator();
   
			   while(var12.hasNext()) {
				  Pizza p = (Pizza)var12.next();
				  addPizza(p);
			   }
   
			   var12 = o.getDiscountList().iterator();
   
			   while(var12.hasNext()) {
				  Discount d = (Discount)var12.next();
				  usePizzaDiscount(o.getOrderID(), d.getDiscountID());
			   }
			}
		} catch (SQLException e) {
			System.out.println("Error adding an order - addorder method");
   
			while(e != null) {
			   System.out.println("Message     : " + e.getMessage());
			   e = e.getNextException();
			}
		}finally{
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
		}	
        
    }

	public static void usePizzaDiscount(int pizzaID, int discountID) throws IOException, SQLException {
		connect_to_db();
		String pizzadiscquery = "insert into pizzadiscount(PizzaID, DiscountID) values (?, ?)";
  
		try {
		   PreparedStatement ps = conn.prepareStatement(pizzadiscquery);
		   ps.setInt(1, pizzaID);
		   ps.setInt(2, discountID);
		   ps.executeUpdate();
		} catch (SQLException e) {
		   e.printStackTrace();
  
		}finally{
			if (conn != null) {
				try {
					if (!conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
		}
	}

	public static void useOrderDiscount(int orderID, int discountID) throws IOException, SQLException {
		connect_to_db();

		try {
			String orderdiscquery = "insert into orderdiscount(OrderID, DiscountID) values (?, ?)";

			PreparedStatement prepStmt = conn.prepareStatement(orderdiscquery);
			prepStmt.setInt(1, orderID);
			prepStmt.setInt(2, discountID);

			prepStmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Error adding orderDiscount");
			while (e != null) {
				System.out.println("Message     : " + e.getMessage());
				e = e.getNextException();
			}
		
	    } finally {
			if (conn != null) {
				try {
					if (!conn.isClosed()) {
						conn.close();
                    }
                } catch (SQLException e) {
					e.printStackTrace();
                }
            }
        }

		
	}


	public static ArrayList<Order> getOrdersByDate(String datefilter) throws SQLException, IOException {
		connect_to_db();
		/*
		 * This function should return an arraylist of all of the orders.
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 * 
		 * Also, like toppings, whenever we print out the orders using menu function 4 and 5
		 * these orders should print in order from newest to oldest.
		 */
		ArrayList<Order> CurrentOrdlist = new ArrayList<>();
		try{
			/* Taking parameter date filter */
			String curorder_query = (datefilter.equals("PlaceholderParam"))? 
			        "SELECT orders.*, CONCAT(customer.Fname, ' ', customer.Lname) AS CustomerName " + 
					"FROM orders " +
					"INNER JOIN customer ON orders.CustID = customer.CustID " +
                    "ORDER BY OrderTimeStamp DESC;" :
                    "SELECT orders.*, CONCAT(customer.Fname, ' ', customer.Lname) AS CustomerName " +
                    "FROM orders " +
                    "INNER JOIN customer ON orders.CustID = customer.CustID " +
                    "WHERE OrderTimeStamp >= ? "+
                    "ORDER BY OrderTimeStamp DESC;";
					//ORDER BY OrderTimeStamp DESC;" : "SELECT * FROM orders where OrderTimeStamp >= '" + datefilter + "' ORDER BY OrderTimeStamp DESC;";
            
			try (PreparedStatement ps = conn.prepareStatement(curorder_query)) {
				if (!datefilter.equals("PlaceholderParam")) {
					ps.setString(1, datefilter);
				}
			    ResultSet rs = ps.executeQuery();

			    while(rs.next()){
					int OrderID = rs.getInt("OrderID");
				    int CustID = rs.getInt("CustID");
				    String OrderTimeStamp = rs.getTimestamp("OrderTimeStamp").toString();
				    int isComplete = rs.getInt("isComplete");
				    double BusPrice = rs.getDouble("BusPrice");
				    double CustPrice = rs.getDouble("CustPrice");
				    String OrderType = rs.getString("OrderType");
				    CurrentOrdlist.add(new Order(OrderID, CustID, OrderType, OrderTimeStamp, CustPrice, BusPrice, isComplete));
				}	
			}
		}catch (SQLException e){
			System.out.println("Could not perform task");
			e.printStackTrace();
			SQLException ne = e.getNextException();
			while (ne != null) {
				System.out.println("Message: " + ne.getMessage());
				ne = ne.getNextException();
			}
		}finally{
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
		}
		return CurrentOrdlist;
	}



	public static void addPizza(Pizza p) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Add the code needed to insert the pizza into into the database.
		 * Keep in mind adding pizza discounts and toppings associated with the pizza,
		 * there are other methods below that may help with that process.
		 * 
		 */
		String pizza_stmt = "insert into pizza(Size, CrustType, OrderID, PizzaState, PizzaDate, BusPrice , CustPrice) VALUES" + "(?,?,?,?,?,?,?);";
		try (PreparedStatement ps = conn.prepareStatement(pizza_stmt)) {
			// ps.setInt(1, p.getPizzaID());
			ps.setString(1, p.getSize());
			ps.setString(2, p.getCrustType());
			ps.setInt(3, p.getOrderID());
		    ps.setString(4, "incomplete");
		    ps.setString(5, p.getPizzaDate());
			ps.setDouble(6,p.getBusPrice());
		    ps.setDouble(7,p.getCustPrice());
		    ps.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}		
	}

		
	public static void useTopping(Pizza p, Topping t, boolean isDoubled) throws SQLException, IOException //this method will update toppings inventory in SQL and add entities to the Pizzatops table. Pass in the p pizza that is using t topping
	{
		connect_to_db();
		/*
		 * This method should do 2 two things.
		 * - update the topping inventory every time we use t topping (accounting for extra toppings as well)
		 * - connect the topping to the pizza
		 *   What that means will be specific to your yimplementatinon.
		 * 
		 * Ideally, you should't let toppings go negative....but this should be dealt with BEFORE calling this method.
		 * 
		 */
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	
	public static void addCustomer(Customer c) throws SQLException, IOException {
		connect_to_db();
		/*
		 * This method adds a new customer to the database.
		 * 
		 */
				
		 try {
			String cus_query = "insert into customer(CustID, FName, LName, Phone) VALUES" + "(?,?,?,?);";
			PreparedStatement ps = conn.prepareStatement(cus_query);
			ps.setInt(1, c.getCustID());
			ps.setString(2, c.getFName());
			ps.setString(3, c.getLName());
			ps.setString(4, c.getPhone());
			ps.executeUpdate();

		}
		catch (SQLException e){
			e.printStackTrace();
			
		} finally {
			// DO NOT FORGET TO CLOSE YOUR CONNECTION
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	public static Order getOrderById(int orderID) throws SQLException, IOException
	{
		connect_to_db();

		try {
			String orderid_stmt = "Select * from orders where OrderID = " + orderID + ";";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(orderid_stmt);
		
			while (rs.next()) {
				int OrderID = rs.getInt("OrderID");
				int CustID = rs.getInt("CustID");
				String OrderType = rs.getString("OrderType");
				String OrderTimeStamp = rs.getTimestamp("OrderTimeStamp").toString();
				int isComplete = rs.getInt("isComplete");
				double BusPrice = rs.getDouble("BusPrice");
				double CustPrice = rs.getDouble("CustPrice");

				Order o = new Order(OrderID, CustID, OrderType, OrderTimeStamp, BusPrice, CustPrice, isComplete );
                return o;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			
		}
			conn.close();
			return null;
	}

	public static List<Pizza> getPizzasByOrderId(int orderID) throws SQLException, IOException
	{
		connect_to_db();

		List<Pizza> pizzas = new ArrayList<>();
		try {
			String pizzaQuery = "SELECT * FROM pizza WHERE OrderID = ?;";
			try (PreparedStatement ps = conn.prepareStatement(pizzaQuery)) {
				ps.setInt(1, orderID);
				ResultSet rs = ps.executeQuery();
	
				while (rs.next()) {
					int PizzaID = rs.getInt("PizzaID");
					String Size = rs.getString("Size");
					String CrustType = rs.getString("CrustType");
					int OrderID = rs.getInt("OrderID");
					String PizzaState = rs.getString("PizzaState");
					String PizzaDate = rs.getString("PizzaDate");
					double CustPrice = rs.getDouble("CustPrice");
					Double BusPrice = rs.getDouble("BusPrice");
	
					Pizza pizza = new Pizza(PizzaID, Size, CrustType, OrderID, PizzaState, PizzaDate, CustPrice, BusPrice);
					pizzas.add(pizza);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	
		return pizzas;
	}


	public static String getAddressById(int orderID) throws SQLException, IOException {
		connect_to_db();

		String addre_stmt = "Select * from delivery where OrderID = " + orderID + ";";
		String concataddr = "";
		
		try (PreparedStatement ps = conn.prepareStatement(addre_stmt)) {
			ResultSet rs = ps.executeQuery();
	
			if (rs.next()) {
				concataddr = rs.getString(3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	
		return concataddr;
	}
		
		

	public static void completeOrder(Order o) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Find the specifed order in the database and mark that order as complete in the database.
		 * 
		 */
		String query = "Update orders SET isComplete = ? WHERE OrderID = ?;";
		PreparedStatement ps = conn.prepareStatement(query);
		int order_Id = o.getOrderID();
		ps.setBoolean(1, true);
		ps.setInt(2, order_Id);
		ps.executeUpdate();

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		conn.close();
	}


	public static ArrayList<Order> getOrders(boolean openOnly) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Return an arraylist of all of the orders.
		 * 	openOnly == true => only return a list of open (ie orders that have not been marked as completed)
		 *           == false => return a list of all the orders in the database
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 * 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		 */
        ArrayList<Order> ordersList = new ArrayList<>();
    
        String ordersQuery = "SELECT * FROM orders WHERE isComplete = ? ORDER BY OrderTimeStamp DESC;" ;
		

        try {
			PreparedStatement ps = conn.prepareStatement(ordersQuery);
			ps.setInt(1, openOnly ? 0 : 1);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
				int OrderID = rs.getInt("OrderID");
                int CustID = rs.getInt("CustID");
                String OrderType = rs.getString("OrderType");
                String OrderTimeStamp = rs.getTimestamp("OrderTimeStamp").toString();
                int isComplete = rs.getInt("isComplete");
                double BusPrice = rs.getDouble("BusPrice");
                double CustPrice = rs.getDouble("CustPrice");

                Order order = new Order(OrderID, CustID, OrderType, OrderTimeStamp, CustPrice, BusPrice, isComplete);
                ordersList.add(order);
            }

        } catch (SQLException e) {
			e.printStackTrace();
        } finally {
        conn.close();
        }

        return ordersList;
    }

	
	public static Order getLastOrder() throws SQLException, IOException {
		/*
		 * Query the database for the LAST order added
		 * then return an Order object for that order.
		 * NOTE...there should ALWAYS be a "last order"!
		 */
		connect_to_db();

		String query = "SELECT * FROM orders ORDER BY OrderTimeStamp DESC, OrderID DESC LIMIT 1;";

        try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int OrderID = rs.getInt("OrderID");
				int CustID = rs.getInt("CustID");
                String OrderType = rs.getString("OrderType");
                String OrderTimeStamp = rs.getTimestamp("OrderTimeStamp").toString();
                int isComplete = rs.getInt("isComplete");
                double BusPrice = rs.getDouble("BusPrice");
                double CustPrice = rs.getDouble("CustPrice");
  

               
                return new Order(OrderID,CustID, OrderType, OrderTimeStamp, CustPrice, BusPrice, isComplete);
            }
			
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	
		return null;
	}


	public static ArrayList<Topping> getInventory() throws SQLException, IOException {
		connect_to_db();
		/*
		 * This function actually returns the toppings. The toppings
		 * should be returned in alphabetical order if you don't
		 * plan on using a printInventory function
		 */
		String inventory_stmt = "Select * from topping ORDER BY TopName ASC;";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(inventory_stmt);

		ArrayList<Topping> tp = new ArrayList<Topping>();
		while (rs.next()){
			tp.add(new Topping(rs.getInt("TopID"), rs.getString("TopName")
					,rs.getInt("PerAMT"),
					rs.getDouble("MedAMT"), rs.getDouble("LgAMT"), rs.getDouble("XLAMT"),rs.getDouble("BusPrice") ,rs.getDouble("CustPrice")
					,rs.getInt("MinINVT"),rs.getInt("CurINVT")));
		}

		conn.close();

		return tp;
	}

		
	public static ArrayList<Discount> getDiscountList() throws SQLException, IOException {
		ArrayList<Discount> discs = new ArrayList<Discount>();
		connect_to_db();
		/* 
		 * Query the database for all the available discounts and 
		 * return them in an arrayList of discounts.
		 * 
		*/
		try{
			String discount_stmt = "SELECT * FROM discount";
			conn = DBConnector.make_connection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(discount_stmt);

			while(rs.next()){
				discs.add(new Discount(rs.getInt("DiscountID"), rs.getString("DiscountName"), rs.getDouble("Amount"), rs.getBoolean("isPercent")));
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		conn.close();
		return discs;
	}
		

	public static Discount findDiscountByName(String name){
		/*
		 * Query the database for a discount using it's name.
		 * If found, then return an OrderDiscount object for the discount.
		 * If it's not found....then return null
		 *  
		 */
     
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			connect_to_db();
			String query = "SELECT * FROM discount WHERE DiscountName = ?";
			ps = conn.prepareStatement(query);
			
			ps.setString(1, name);
			
			rs = ps.executeQuery();
		
			if (rs.next()) {
				// Retrieve data from the result set and create a Discount object
	            Discount discount = new Discount(
				rs.getInt("DiscountID"),
				rs.getString("DiscountName"),
				
				rs.getDouble("Amount"),
				rs.getBoolean("isPercent"));
				return discount;
			}
		} catch (SQLException | IOException e) {
				// Handle exceptions appropriately (log, display error messages, etc.)
			e.printStackTrace();
		} 
		
		return null;
	}


	public static ArrayList<Customer> getCustomerList() throws SQLException, IOException {
		ArrayList<Customer> custslist = new ArrayList<Customer>();
		
		/*
		 * Query the data for all the customers and return an arrayList of all the customers. 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		*/
		try
		{
			conn = DBConnector.make_connection();
			connect_to_db();

			String customer_stmt = "SELECT * FROM customer";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(customer_stmt);

			while(rs.next())
			{
				custslist.add(new Customer(rs.getInt("CustID"),rs.getString("FName"), rs.getString("LName"), rs.getString("Phone")));
			}
		}
		catch (SQLException e)
		{
			System.out.println("Could not get customers");
			e.printStackTrace();
		}finally{
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return custslist;
	}

	public static Customer findCustomerByPhone(String phoneNumber){
		/*
		 * Query the database for a customer using a phone number.
		 * If found, then return a Customer object for the customer.
		 * If it's not found....then return null
		 *  
		 */

		try {
			connect_to_db();
			String query = "SELECT * FROM customer WHERE Phone = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, phoneNumber);
			ResultSet rs = ps.executeQuery();
		
			if (rs.next()) {
				Customer customer = new Customer(
						rs.getInt("CustID"),
						rs.getString("FName"),
						rs.getString("LName"),
						rs.getString("Phone"));
				return customer;
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			

		}
		
		return null;
	}


	public static ArrayList<Topping> getToppingList() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Query the database for the aviable toppings and 
		 * return an arrayList of all the available toppings. 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		 */

			
			// Query to retrieve available toppings ordered by name
		
		ArrayList<Topping> toppings = new ArrayList<>();	
		try {
			String query = "SELECT * FROM topping ORDER BY TopName";
			PreparedStatement ps = conn.prepareStatement(query);
		    ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// Retrieve data from the result set and create a Topping object
				Topping topping = new Topping(
						rs.getInt("TopID"),
						rs.getString("TopName"),
						rs.getDouble("PerAMT"),
						rs.getDouble("MedAmt"),
						rs.getDouble("LgAMT"),
						rs.getDouble("XLAMT"),
						rs.getDouble("CustPrice"),
						rs.getDouble("BusPrice"),
						rs.getInt("MinINVT"),
						rs.getInt("CurINVT"));
				toppings.add(topping);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return toppings;
	}

	public static Topping findToppingByName(String name) throws SQLException, IOException{
		/*
		 * Query the database for the topping using it's name.
		 * If found, then return a Topping object for the topping.
		 * If it's not found....then return null
		 *  
		 */
		connect_to_db();
		ArrayList<Topping> toppingList = getToppingList();
		for (Topping topping : toppingList) {
			if (topping.getTopName().equalsIgnoreCase(name)) {
				return topping;
			}
		}

		return null;
	}


	public static void addToInventory(Topping t, double quantity) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Updates the quantity of the topping in the database by the amount specified.
		 * 
		 * */

		try {
			String upi_st = "UPDATE topping set CurINVT = ? where TopID = ?;";
			t.setCurINVT((int)((double)t.getCurINVT() + quantity));
			PreparedStatement ps = conn.prepareStatement(upi_st);
			ps.setInt(1, t.getCurINVT());
			ps.setInt(2, t.getTopID());
			ps.executeUpdate();
		} catch (SQLException var5) {
			System.out.println("Could not perform task");
   
			for(SQLException ne = var5.getNextException(); ne != null; ne = ne.getNextException()) {
			   System.out.println("Message: " + ne.getMessage());
			}
		}
   
		conn.close();
		
		
	}
	
	public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		/* 
		 * Query the database fro the base customer price for that size and crust pizza.
		 * 
		*/
		
		double baseprice = 0.0;
        String custp_stmt = "Select CustPrice from baseprice WHERE BaseSize='" + size + "' and CrustType='" + crust + "';";
        Statement st = conn.createStatement();

        for(ResultSet rs = st.executeQuery(custp_stmt); rs.next(); baseprice = rs.getDouble(1)) {
        }

        conn.close();
        return baseprice;
		
	}

	public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		/* 
		 * Query the database fro the base business price for that size and crust pizza.
		 * 
		*/
		double basep = 0.0;
        String busip_st = "Select BusPrice from baseprice WHERE BaseSize='" + size + "' and CrustType='" + crust + "';";
        Statement stmt = conn.createStatement();

        for(ResultSet rs = stmt.executeQuery(busip_st); rs.next(); basep = rs.getDouble(1)) {
        }
        
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
        conn.close();
        return basep;
		
	}

	public static void printInventory() throws SQLException, IOException {
		
		/*
		 * Queries the database and prints the current topping list with quantities.
		 *  
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */
        try{
			connect_to_db();
		    ArrayList<Topping> t = getInventory();
		    System.out.printf("%-10s %-25s %-30s\n", "ID", "Name", "CurINVT");
		    Iterator var1 = t.iterator();
   
		    while(var1.hasNext()) {
				Topping tp = (Topping)var1.next();
			    System.out.printf("%-10s %-25s %-30s\n", tp.getTopID(), tp.getTopName(), tp.getCurINVT());
		    }
		} finally{
			if (conn != null && !conn.isClosed()) {
				conn.close();
		    }	
	    }
	}

	public static void PriceAndCostCalculation(int orderID, int pizzaID, double p,double c,ArrayList<Topping> getToppings, ArrayList<Discount> arraydic  ) throws SQLException, IOException {
		connect_to_db();

		double pizzaPrice = p;
			double pizzaCost = c;
			double tp = 0.0;
			double tc=0.0;
			for(int i=0; i<getToppings.size();i++) {
				tc += getToppings.get(i).getCustPrice();
				tp += getToppings.get(i).getBusPrice();
			}
			p+=tp;
			c+=tc;

		for (Discount d : arraydic) {
			if (d.isPercent() == false) {
				p =p * (1 - d.getAmount()) / 100;

			} else {
				p-=d.getAmount();
			}
		}

		String u_st = "UPDATE PIZZERIA.pizza SET BusPrice = "+c+", CustPrice = "+p+" WHERE PizzaID ="+orderID+";";

		connect_to_db();
		try {
			Connection conn = DBConnector.make_connection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(u_st);
		} catch (SQLException e) {
			System.out.println("Error updating database: " + e.getMessage());
		}
		conn.close();

		String sqlupdate = "update PIZZERIA.orders SET BusPrice =BusPrice+"+c+" , CustPrice=CustPrice+"+p+" where OrderID="+orderID+";";
		//String pizzadisc = "update PIZZERIA.pizzadiscount SET PizzaID = "+pizzaID+" , DiscountID = where OrderID="+orderID+";";
		connect_to_db();
		try {
			Connection conn = DBConnector.make_connection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sqlupdate);
		} catch (SQLException e) {
			System.out.println("Error updating database: " + e.getMessage());
		}
		conn.close();

	}
	
	public static void printToppingPopReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ToppingPopularity view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */

		String pt_st = "select * from ToppingPopularity;";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(pt_st);
		System.out.printf("%-20s %-25s\n", "Topping", "ToppingCount");
   
		while(rs.next()) {
			System.out.printf("%-20s %-20s\n", rs.getString(1), rs.getInt(2));
			System.out.println();
		}
   
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		conn.close();
	
	}

	public static void insertPizzaDiscount(int pizzaID, int discountID) throws SQLException, IOException {
		connect_to_db();
	
		try {
			String insertQuery = "INSERT INTO pizzadiscount (PizzaID, DiscountID) VALUES (?, ?);";
	
			try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
				ps.setInt(1, pizzaID);
				ps.setInt(2, discountID);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	public static void insertOrderDiscount(int orderID, int discountID) throws SQLException, IOException
	{
		connect_to_db();

        try {
            String insertQuery = "INSERT INTO orderdiscount (OrderID, DiscountID) VALUES (?, ?);";

            try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
                ps.setInt(1, orderID);
                ps.setInt(2, discountID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	
	public static void printProfitByPizzaReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByPizza view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */
		
		String pb_st = "select * from ProfitByPizza;";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(pb_st);
		System.out.printf("%-20s %-20s %-20s %-20s\n", "Size", "Crust", "Profit", "Last Order Date");
   
		while(rs.next()) {
			System.out.printf("%-20s %-20s %-20s %-20s\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
		}
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
		conn.close();
	
	}
	
	public static void printProfitByOrderType() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByOrderType view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */
		
		String pp_st = "select * from ProfitByOrderType;";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(pp_st);
	    System.out.printf("%-20s %-20s %-20s %-20s %-20s\n", "customerType", "OrderMonth", "TotalOrderPrice", "TotalOrderCost", "Profit");
   
		while(rs.next()) {
			System.out.printf("%-20s %-20s %-20s %-20s %-20s\n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
		}
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
		conn.close();
	
	}

	public static void DBInsert(String sqlQuery) throws SQLException, IOException
	{
		connect_to_db();
		if (connect_to_db()) {
			conn = DBConnector.make_connection();
			st = conn.createStatement();
			st.execute(sqlQuery);

		} else {
			System.out.print("Not connected to database");
		}

		conn.close();
	}

	public static int getMaxPizzaID() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * A function I needed because I forgot to make my pizzas auto increment in my DB.
		 * It goes and fetches the largest PizzaID in the pizza table.
		 * You wont need to implement this function if you didn't forget to do that
		 */
		String mx_st = "SELECT MAX(PizzaID) FROM pizza;";
		try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(mx_st)) {
				int m = 0;
			    while (rs.next()) {
					m = rs.getInt(1);
			    }
			    return m;
		}finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	public static void ToppingInsert(ArrayList<Topping> getToppings) throws SQLException, IOException
	{
		ArrayList<Integer> t_id = new ArrayList<Integer>();

		for(int i=0;i<getToppings.size();i++)
		{
			t_id.add(getToppings.get(i).getTopID());

		}
		Dictionary<Integer, Integer> pizzaToppingDetails = new Hashtable<Integer, Integer>();
		

		for (int i = 0; i < t_id.size(); i++) {
			for (int j = 0; j < (t_id.size() - 1 - i); j++) {
				if (t_id.get(j) > t_id.get(j + 1)) {
					int temp = t_id.get(j);
					t_id.set(j, t_id.get(j + 1));
					t_id.set(j + 1, temp);

				}
			}
		} 

		for (int i = 0; i < t_id.size(); i++) {
			int duplicateCount = 0;
			int k = 0;
			int x = t_id.get(i);
			for (int j = i + 1; j < t_id.size(); j++) {
				int y = t_id.get(j);
				if (x == y) {
					duplicateCount++;

					k = j;
				}
			}
			pizzaToppingDetails.put(t_id.get(i), duplicateCount);
			if (k != 0) {
				i = k;
			}

		}


		int pizzaOrderId =getMaxPizzaID();

		for (Enumeration enn = pizzaToppingDetails.keys(); enn.hasMoreElements();) {
			int keyValue = Integer.parseInt(enn.nextElement().toString());
			String pizzaSqlQuery = "Insert into pizzatoppings(PizzaID,TopID,Isextra)value('"
					+ pizzaOrderId + "','" + keyValue + "','" + pizzaToppingDetails.get(keyValue) + "');";
			 // System.out.println(pizzaSqlQuery);
			DBInsert(pizzaSqlQuery);
		}

	}

	public static void updateToppingTable(String sqlQuery) throws SQLException, IOException {
		try{
			if (connect_to_db()) {
				conn = DBConnector.make_connection();
			    st = conn.createStatement();
			    st.executeUpdate(sqlQuery);
		    } else {
				System.out.print("Not connected to db");
		    }
		}finally{
			if (conn != null) {
				conn.close();
			}
		}
		
	}
	
	
	
	public static String getCustomerName(int CustID) throws SQLException, IOException
	{
	/*
		 * This is a helper method to fetch and format the name of a customer
		 * based on a customer ID. This is an example of how to interact with 
		 * your database from Java.  It's used in the model solution for this project...so the code works!
		 * 
		 * OF COURSE....this code would only work in your application if the table & field names match!
		 *
		 */

		 connect_to_db();

		/* 
		 * an example query using a constructed string...
		 * remember, this style of query construction could be subject to sql injection attacks!
		 * 
		 */
		/*String cname1 = "";
		String query = "Select FName, LName From customer WHERE CustID=" + CustID + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		
		while(rset.next())
		{
			cname1 = rset.getString(1) + " " + rset.getString(2); 
		}
         */
		/* 
		* an example of the same query using a prepared statement...
		* 
		*/
		String cname2 = "";
		PreparedStatement os;
		ResultSet rset2;
		String query2;
		query2 = "Select FName, LName From customer WHERE CustomerID=?;";
		os = conn.prepareStatement(query2);
		os.setInt(1, CustID);
		rset2 = os.executeQuery();
		while(rset2.next())
		{
			cname2 = rset2.getString("FName") + " " + rset2.getString("LName"); // note the use of field names in the getSting methods
		}
		conn.close();
		return cname2; 
	}

	/*
	 * The next 3 private methods help get the individual components of a SQL datetime object. 
	 * You're welcome to keep them or remove them.
	 */
	private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(0,4));
	}
	private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(5, 7));
	}
	private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(8, 10));
	}

	public static boolean checkDate(int year, int month, int day, String dateOfOrder)
	{
		if(getYear(dateOfOrder) > year)
			return true;
		else if(getYear(dateOfOrder) < year)
			return false;
		else
		{
			if(getMonth(dateOfOrder) > month)
				return true;
			else if(getMonth(dateOfOrder) < month)
				return false;
			else
			{
				if(getDay(dateOfOrder) >= day)
					return true;
				else
					return false;
			}
		}
	}


}