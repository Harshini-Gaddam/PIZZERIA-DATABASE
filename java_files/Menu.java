package cpsc4620;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cpsc4620.DBConnector;
import static cpsc4620.DBNinja.*;

/*
 * This file is where the front end magic happens.
 * 
 * You will have to write the methods for each of the menu options.
 * 
 * This file should not need to access your DB at all, it should make calls to the DBNinja that will do all the connections.
 * 
 * You can add and remove methods as you see necessary. But you MUST have all of the menu methods (including exit!)
 * 
 * Simply removing menu methods because you don't know how to implement it will result in a major error penalty (akin to your program crashing)
 * 
 * Speaking of crashing. Your program shouldn't do it. Use exceptions, or if statements, or whatever it is you need to do to keep your program from breaking.
 * 
 */

public class Menu {

	public static void main(String[] args) throws SQLException, IOException {

		System.out.println("Welcome to Pizzas-R-Us!");
		
		int menu_option = 0;

		// present a menu of options and take their selection
		
		PrintMenu();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String option = reader.readLine();
		menu_option = Integer.parseInt(option);

		while (menu_option != 9) {
			switch (menu_option) {
			case 1:// enter order
				EnterOrder();
				break;
			case 2:// view customers
				viewCustomers();
				break;
			case 3:// enter customer
				EnterCustomer();
				break;
			case 4:// view order
				// open/closed/date
				ViewOrders();
				break;
			case 5:// mark order as complete
				MarkOrderAsComplete();
				break;
			case 6:// view inventory levels
				ViewInventoryLevels();
				break;
			case 7:// add to inventory
				AddInventory();
				break;
			case 8:// view reports
				PrintReports();
				break;
			}
			PrintMenu();
			option = reader.readLine();
			menu_option = Integer.parseInt(option);
		}

	}

	// allow for a new order to be placed
	public static void EnterOrder() throws SQLException, IOException 
	{

		/*
		 * EnterOrder should do the following:
		 * 
		 * Ask if the order is delivery, pickup, or dinein
		 *   if dine in....ask for table number
		 *   if pickup...
		 *   if delivery...
		 * 
		 * Then, build the pizza(s) for the order (there's a method for this)
		 *  until there are no more pizzas for the order
		 *  add the pizzas to the order
		 *
		 * Apply order discounts as needed (including to the DB)
		 * 
		 * return to menu
		 * 
		 * make sure you use the prompts below in the correct order!
		 */

		 // User Input Prompts...
		/*System.out.println("Is this order for: \n1.) Dine-in\n2.) Pick-up\n3.) Delivery\nEnter the number of your choice:");
		System.out.println("Is this order for an existing customer? Answer y/n: ");
		System.out.println("Here's a list of the current customers: ");
		System.out.println("Which customer is this order for? Enter ID Number:");
		System.out.println("ERROR: I don't understand your input for: Is this order an existing customer?");
		System.out.println("What is the table number for this order?");
		System.out.println("Let's build a pizza!");
		System.out.println("Enter -1 to stop adding pizzas...Enter anything else to continue adding pizzas to the order.");
		System.out.println("Do you want to add discounts to this order? Enter y/n?");
		System.out.println("Which Order Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");
		System.out.println("What is the House/Apt Number for this order? (e.g., 111)");
		System.out.println("What is the Street for this order? (e.g., Smile Street)");
		System.out.println("What is the City for this order? (e.g., Greenville)");
		System.out.println("What is the State for this order? (e.g., SC)");
		System.out.println("What is the Zip Code for this order? (e.g., 20605)");
		
		
		System.out.println("Finished adding order...Returning to menu..."); */

		try {
			
		    BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
	
			int cus_id = 0;
			String od_type = "";
			int orderId = (DBNinja.getOrdersByDate("PlaceholderParam").size() + 1);
	
			// Prompt 1: Order for an existing customer?
			System.out.println("Is the order for an existing customer? Answer y/n: ");
			String str_choice = rd.readLine();
	
			if (str_choice.equals("y") || str_choice.equals("Y")) {
				System.out.println("Here's a list of current customers:");
				viewCustomers();
				System.out.println("Which customer is this order for? Enter ID Number ");
				cus_id = Integer.parseInt(rd.readLine());
	
			} else if (str_choice.equals("n") || str_choice.equals("N")) {
				Menu.EnterCustomer();
				System.out.println("Here's a list of current customers:");
				viewCustomers();
				cus_id = DBNinja.getCustomerList().size();
			}
	
			// Customer c = null;
	
			// Prompt 2: Order Type
			System.out.println("Is this order for:");
			System.out.println("1.) Dine-In");
			System.out.println("2.) Pick-Up");
			System.out.println("3.) Delivery");
			System.out.println("Enter the number of your choice: ");
	
			Order o = null;
			int orderChoice = Integer.parseInt(rd.readLine());
			int table_num = 0;
			String addr = "";
	
			switch (orderChoice) {
				case 1:
					// Dine-in
					od_type="dinein";
					o = new DineinOrder(orderId, 0, "", 0, 0, 0, table_num);
					o.setOrderType(DBNinja.dine_in);

					System.out.println("Enter table number that you wanted: ");
					table_num = Integer.parseInt(rd.readLine());
					break;
				case 2:
					// Pick-up
					od_type="pickup";
					o = new PickupOrder(orderId, cus_id, "", 0, 0, 0, 0);
					o.setOrderType(DBNinja.pickup);
					break;
				case 3:
					// Delivery
					od_type="delivery";
					o = new DeliveryOrder(orderId, cus_id, "", 0, 0, 0, addr);
					o.setOrderType(DBNinja.delivery);
					System.out.println("What is the House/Apt Number for this order? (e.g., 111)");
                    int houseNumber = Integer.parseInt(rd.readLine());

                    System.out.println("What is the Street for this order? (e.g., Smile Street)");
                    String street = rd.readLine();

                    System.out.println("What is the City for this order? (e.g., Greenville)");
                    String city = rd.readLine();

                    System.out.println("What is the State for this order? (e.g., SC)");
                    String state = rd.readLine();

                    System.out.println("What is the Zip Code for this order? (e.g., 20605)");
                    int zipCode = Integer.parseInt(rd.readLine());

                    addr = houseNumber + " " + street + ", " + city + ", " + state + " " + zipCode;
                    break;

					
			}
	
			// Database insertion for order
			String sqlOrderQuery = "INSERT INTO orders (OrderID, CustID, OrderTimeStamp, isComplete, BusPrice, CustPrice, OrderType) VALUES ("+ orderId + ", " + cus_id + ", CURRENT_TIMESTAMP, 0, 0.0, 0.0, '" + od_type + "');";
			//System.out.println(sqlOrderQuery);
			DBNinja.DBInsert(sqlOrderQuery);
	
			// Database insertion for order type specific details
			switch (orderChoice) {
				case 1:
					String sqlQueryDineIn = "insert INTO dinein(OrderID,TableNum)values(" + orderId + "," + table_num + ");";
					DBNinja.DBInsert(sqlQueryDineIn);
					break;
				case 2:
					String sqlQueryPickup = "Insert into pickup(OrderID,CustID)values(" + orderId + ", " + cus_id + ");";
					DBNinja.DBInsert(sqlQueryPickup);
					break;
				case 3:
					String sqlQueryDelivery = "Insert into delivery(OrderID,CustID, CustAddress)values(" + orderId + ", " + cus_id + ", '"+addr+"');";
					DBNinja.DBInsert(sqlQueryDelivery);
					break;
			}
            // Build the pizza
			System.out.println("Let's build a pizza!");

	
			boolean flag_ordr = false;
	
			while (!flag_ordr) {
			
				Pizza pz = buildPizza(orderId);
				DBNinja.addPizza(pz);
				DBNinja.ToppingInsert(pz.getToppings());
	
				System.out.println("Enter -1 to stop adding pizzas... Enter anything else to continue adding more pizzas to the order.");
				if (rd.readLine().equals("-1")) {
					flag_ordr = true;
				}
			}
	
			// Additional prompts for discounts
			/*System.out.println("Do you want to add discounts to this order? Enter y/n?");
			String addDiscounts = rd.readLine().toLowerCase();
			if (addDiscounts.equals("y")) {
				// Add logic to add discounts
				System.out.println("Which Order Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");
				// Add logic to handle discount input
			} */
	
			System.out.println("Finished adding order...Returning to menu...");
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void viewCustomers() throws SQLException, IOException 
	{
		/*
		 * Simply print out all of the customers from the database. 
		 */
		try {
			ArrayList<Customer> custinfo = DBNinja.getCustomerList();

			for (Customer c : custinfo) {
				System.out.println(c);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
			
	}
	

	// Enter a new customer in the database
	public static void EnterCustomer() throws SQLException, IOException 
	{
		/*
		 * Ask for the name of the customer:
		 *   First Name <space> Last Name
		 * 
		 * Ask for the  phone number.
		 *   (##########) (No dash/space)
		 * 
		 * Once you get the name and phone number, add it to the DB
		 */
		
		// User Input Prompts...
		/* System.out.println("What is this customer's name (first <space> last");
		 System.out.println("What is this customer's phone number (##########) (No dash/space)"); */
 
		try {
			String Fname, Lname, Phone;
			ArrayList<Customer> cus = DBNinja.getCustomerList();
			Integer cus_id = cus.size() + 1;
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("What is this customer's name (first <space> last");
			String[] fullname = rd.readLine().split(" ");

			
			Fname = fullname[0];
			Lname = fullname[fullname.length-1];
			System.out.println("What is this customer's phone number (##########) (No dash/space)");
			Phone = rd.readLine();
			
			Customer c = new Customer(cus_id, Fname, Lname, Phone);
			System.out.println(c.toString());
			DBNinja.addCustomer(c);
		} catch(Exception e){
			e.printStackTrace();
		}

	}

	// View any orders that are not marked as completed
	public static void ViewOrders() throws SQLException, IOException 
	{
		/*  
		* This method allows the user to select between three different views of the Order history:
		* The program must display:
		* a.	all open orders
		* b.	all completed orders 
		* c.	all the orders (open and completed) since a specific date (inclusive)
		* 
		* After displaying the list of orders (in a condensed format) must allow the user to select a specific order for viewing its details.  
		* The details include the full order type information, the pizza information (including pizza discounts), and the order discounts.
		* 
		*/ 
			
		
		// User Input Prompts...
		/*System.out.println("Would you like to:\n(a) display all orders [open or closed]\n(b) display all open orders\n(c) display all completed [closed] orders\n(d) display orders since a specific date");
		System.out.println("What is the date you want to restrict by? (FORMAT= YYYY-MM-DD)");
		System.out.println("I don't understand that input, returning to menu");
		System.out.println("Which order would you like to see in detail? Enter the number (-1 to exit): ");
		System.out.println("Incorrect entry, returning to menu.");
		System.out.println("No orders to display, returning to menu."); */


		try {
			String str_choice = "";
			boolean flag = false;
			BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
			

			while(flag != true) {
				System.out.println("Would you like to:");
				System.out.println("Press (a) to display all orders [open or closed]");
				System.out.println("Press (b) to display all open orders");
				System.out.println("Press (c) to display all completed orders");
				System.out.println("Press (d) to display orders since a specific date");
				str_choice = rd.readLine();

				if (str_choice.equals("a") || str_choice.equals("b") || str_choice.equals("c") || str_choice.equals("d")) {
					flag = true;
				}
			}
			
			switch(str_choice){
				case "a":
					//print all orders
					ArrayList<Order> viewodr = DBNinja.getOrdersByDate("PlaceholderParam");
					for (Order o : viewodr) {
						System.out.println(o.toSimplePrint());
					}

					break;
				case "b": 
				    //	display all open orders
					List<Order> openOrders = DBNinja.getOrders(false);
                    for (Order o : openOrders) {
						System.out.println(o.toSimplePrint());
                    }
                    break;

				case "c":
				    // display all completed orders
					List<Order> completedOrders = DBNinja.getOrders(true);
                    for (Order o : completedOrders) {
						System.out.println(o.toSimplePrint());
                    }
                    break;

				case "d":
				    // print date restricted
					System.out.println("What is the date you want to restrict by? (FORMAT = YYYY-MM-DD)");
					String d = rd.readLine();
					ArrayList<Order> orList = DBNinja.getOrdersByDate(d);
					for (Order o : orList){
						System.out.println(o.toSimplePrint());
					}

					break;

				default:
					System.out.println("I don't understand that input, returning to menu");
					PrintMenu();	

			}
			System.out.println("Which order would you like to see in detail? Enter the number (-1 to exit): ");

            try {
				int order_id = -1; 
                boolean validInput = false;

                while (!validInput) {
					try {
						order_id = Integer.parseInt(rd.readLine());

                        if (order_id == -1) {
							// user chose to exit
							break;
                    	}
					} catch (Exception e){
						e.printStackTrace();
					}
					Order o = DBNinja.getOrderById(order_id);

                    if (o != null) {
						validInput = true;

                        if (o.getOrderType().equals("delivery")) {
							String concataddr = DBNinja.getAddressById(order_id);
                            System.out.println(o.toString() + " | Delivered to: " + concataddr);
                        } else {
							System.out.println(o.toString());
                        }

						List<Pizza> pizzas = DBNinja.getPizzasByOrderId(order_id);
                        for (Pizza pizza : pizzas) {
							System.out.println("Pizza details: " + pizza.toString());
                        }
                    } else {
						System.out.println("Incorrect entry, returning to menu. ");
						PrintMenu();
                    }
					if (order_id == -1) {
						System.out.println("Returning to menu.");
					} else if (order_id != -1 && !validInput) {
						System.out.println("No orders to display, returning to menu.");
					}
                } 
			}catch (NumberFormatException e) {
					System.out.println("Incorrect entry, please enter a valid order number (-1 to exit): ");
            }

		}	catch (Exception e) {
				e.printStackTrace();
			}	
    } 

	
	// When an order is completed, we need to make sure it is marked as complete
	public static void MarkOrderAsComplete() throws SQLException, IOException {
		/*
		 * All orders that are created through java (part 3, not the orders from part 2) should start as incomplete
		 * 
		 * When this method is called, you should print all of the "opoen" orders marked
		 * and allow the user to choose which of the incomplete orders they wish to mark as complete
		 * 
		 */
		
		
		
		// User Input Prompts...
		/*System.out.println("There are no open orders currently... returning to menu...");
		System.out.println("Which order would you like mark as complete? Enter the OrderID: ");
		System.out.println("Incorrect entry, not an option"); */

		try {
			ArrayList<Order> ordrs = DBNinja.getOrdersByDate("PlaceholderParam");;
			int countr = 0;
			for (Order o : ordrs) {
				if (o.getIsComplete() == 0) {
					System.out.println(o.toSimplePrint());
						countr++;
				}
			}
	
			if (countr == 0) {
				System.out.println("There are no open orders currently... returning to menu...");
				PrintMenu();

			} else {
				BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Which order would you like to mark as complete? Enter Order ID ");
	            int ch_order = Integer.parseInt(rd.readLine());
				Order o = DBNinja.getOrderById(ch_order);
				DBNinja.completeOrder(o);
				
			}
		} catch(Exception e){
			e.printStackTrace();
		}

	}

	public static void ViewInventoryLevels() throws SQLException, IOException 
	{
		/*
		 * Print the inventory. Display the topping ID, name, and current inventory
		*/
		try{
			DBNinja.printInventory();
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}


	public static void AddInventory() throws SQLException, IOException 
	{
		/*
		 * This should print the current inventory and then ask the user which topping (by ID) they want to add more to and how much to add
		 */
		
		
		// User Input Prompts...
		/*System.out.println("Which topping do you want to add inventory to? Enter the number: ");
		System.out.println("How many units would you like to add? ");
		System.out.println("Incorrect entry, not an option"); */
	
		try {
			Boolean flag = false;
			Integer topping_Id;
			DBNinja.printInventory();

			BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));

			do {
				System.out.println("Which topping would you like to add inventory to? Enter Number");
			    topping_Id = Integer.parseInt(rd.readLine());
				if (topping_Id < 1 || topping_Id > 17) {
					System.out.println("Incorrect entry, not an option");
				}
			} while (topping_Id < 1 || topping_Id > 17);
	
			System.out.println("How many units would you like to add?");
			double topAmt = Double.parseDouble(rd.readLine());
			while(topAmt<=0){
				
				System.out.println("How many units would you like to add?");
				topAmt = Double.parseDouble(rd.readLine());
			}

			ArrayList<Topping> topp = DBNinja.getInventory();
			Topping dt = topp.get(1);

			for (int i = 0; i< topp.size(); i++) {
				if (topp.get(i).getTopID() == topping_Id) {
					dt = topp.get(i);

				}
			}
			
			if (dt != null) {
				DBNinja.addToInventory(dt, topAmt);
				System.out.println(dt.toString());
			} else {
				System.out.println("Invalid topping ID.");
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	// A method that builds a pizza. Used in our add new order method
	public static Pizza buildPizza(int orderID) throws SQLException, IOException {
		
		/*
		 * This is a helper method for first menu option.
		 * 
		 * It should ask which size pizza the user wants and the crustType.
		 * 
		 * Once the pizza is created, it should be added to the DB.
		 * 
		 * We also need to add toppings to the pizza. (Which means we not only need to add toppings here, but also our bridge table)
		 * 
		 * We then need to add pizza discounts (again, to here and to the database)
		 * 
		 * Once the discounts are added, we can return the pizza
		 */

		 Pizza ret = null;
		
		// User Input Prompts...
		/*System.out.println("What size is the pizza?");
		System.out.println("1."+DBNinja.size_s);
		System.out.println("2."+DBNinja.size_m);
		System.out.println("3."+DBNinja.size_l);
		System.out.println("4."+DBNinja.size_xl);
		System.out.println("Enter the corresponding number: ");
		System.out.println("What crust for this pizza?");
		System.out.println("1."+DBNinja.crust_thin);
		System.out.println("2."+DBNinja.crust_orig);
		System.out.println("3."+DBNinja.crust_pan);
		System.out.println("4."+DBNinja.crust_gf);
		System.out.println("Enter the corresponding number: ");
		System.out.println("Available Toppings:");
		System.out.println("Which topping do you want to add? Enter the TopID. Enter -1 to stop adding toppings: ");
		System.out.println("Do you want to add extra topping? Enter y/n");
		System.out.println("We don't have enough of that topping to add it...");
		System.out.println("Which topping do you want to add? Enter the TopID. Enter -1 to stop adding toppings: ");
		System.out.println("Do you want to add discounts to this Pizza? Enter y/n?");
		System.out.println("Which Pizza Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");
		System.out.println("Do you want to add more discounts to this Pizza? Enter y/n?"); */
					// User Input Prompts...
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Order temp = DBNinja.getOrderById(orderID);
				
		try {
			// Asking for pizza size
			String sz = "";
			while (sz.equals("")){
				System.out.println("What size is the pizza?");
			    System.out.println("1. " + DBNinja.size_s);
			    System.out.println("2. " + DBNinja.size_m);
			    System.out.println("3. " + DBNinja.size_l);
			    System.out.println("4. " + DBNinja.size_xl);
			    System.out.println("Enter the corresponding number: ");
			
			  	int sizeChoice = Integer.parseInt(reader.readLine());
			    
				switch (sizeChoice) {
					case 1:
					    sz = DBNinja.size_s;
				        break;
				    case 2:
					    sz = DBNinja.size_m;
					    break;
				    case 3:
					    sz = DBNinja.size_l;
					    break;
				    case 4:
					    sz= DBNinja.size_xl;
					    break;
				    default:
					    System.out.println("Invalid size choice. Returning to menu.");
					    return ret;
				}		
			}

				
			// Asking for crust type
			String crust = "";
			while (crust.equals("")) {
				System.out.println("What crust for this pizza?");
			    System.out.println("1. " + DBNinja.crust_thin);
			    System.out.println("2. " + DBNinja.crust_orig);
			    System.out.println("3. " + DBNinja.crust_pan);
		        System.out.println("4. " + DBNinja.crust_gf);
			    System.out.println("Enter the corresponding number: ");
			    int crustChoice = Integer.parseInt(reader.readLine());
			    switch (crustChoice) {
					case 1:
					    crust = DBNinja.crust_thin;
					    break;
				    case 2:
					    crust = DBNinja.crust_orig;
					    break;
				    case 3:
					    crust = DBNinja.crust_pan;
					    break;
			        case 4:
					    crust = DBNinja.crust_gf;
					    break;
				    default:
					    System.out.println("Invalid crust choice. Returning to menu.");
					    return ret;
				}
			}
       
			Integer p_id = DBNinja.getMaxPizzaID() + 1;
			Double p_cus_price_t = DBNinja.getBaseCustPrice(sz, crust);
			Double p_bus_price_t = DBNinja.getBaseBusPrice(sz,crust);

			ret = new Pizza(p_id, sz, crust, orderID, "incomplete", temp.getDate(), DBNinja.getBaseCustPrice(sz, crust), DBNinja.getBaseBusPrice(sz,crust));
				
			ArrayList<Topping> cur_inv = DBNinja.getInventory();
			ArrayList<Topping> cur_inv_Selected = new ArrayList<Topping>();
			int topn_ch = 0; boolean flag_top_inv = false;
			while (flag_top_inv!=true) {
				DBNinja.printInventory();
				System.out.println("Which topping do you want to add? Enter the TopID. Enter -1 to stop adding toppings:");
				topn_ch = Integer.parseInt(reader.readLine());

				if (topn_ch != -1) {
					for(Topping t_indx: cur_inv){
						if(t_indx.getTopID() == topn_ch){
							System.out.print("Would you like to add extra topping? y/n: ");
							String yn = reader.readLine();
							boolean isExtra = yn.equals("y");
							cur_inv_Selected.add(t_indx);
							if(isExtra){
								ret.addToppings(t_indx, isExtra);
								// System.out.println(isExtra);
							} else{
								ret.addToppings(t_indx, isExtra);
							}
							break;
						}
					}
				}else {
					flag_top_inv=true;

				}
			}

			ret.setToppings(cur_inv_Selected);

			//
			for (int i = 0; i < cur_inv_Selected.size(); i++) {
				String sqlQueryForToppingInvUpdate = "update topping set CurINVT = CurINVT -1 where TopID ="
						+ cur_inv_Selected.get(i).getTopID() + ";";
				DBNinja.updateToppingTable(sqlQueryForToppingInvUpdate);
			}

			System.out.println("Do you want to add discounts to this Pizza? Enter y/n:");
			ArrayList<Discount> arraydic = new ArrayList<Discount>();
			String yn = reader.readLine();
			if (yn.equals("y")) {
				
				int disc_ch = 0;
				ArrayList<Discount> disc = DBNinja.getDiscountList();
				boolean flag_disc = false;
				System.out.println("Getting discount list...");
				while (flag_disc!=true) {
					for (Discount d : disc) {
						System.out.println(d.toString());
					}

					System.out.println("Which discount do you want to add? Enter the number. Enter -1 to stop adding discounts: ");

					disc_ch = Integer.parseInt(reader.readLine());

					if (disc_ch != -1) {
						for(Discount d : disc) {
							if(d.getDiscountID() == disc_ch ) {
								arraydic.add(d);
								//System.out.println(ret.getPizzaID() + ", " + disc_ch);
								//DBNinja.usePizzaDiscount(ret.getPizzaID(), disc_ch );
							}
						}
						
					} else if(disc_ch == -1){
						System.out.println("Do you want to add more discounts to this Pizza? Enter y/n:");
						boolean m_choice = reader.readLine().equals("y");
						if (m_choice) {
							continue;
						}
						break;
					} else{
						flag_disc=true;
					}
				}
			}

			System.out.println("Do you want to add discounts to this Order? Enter y/n:");
			ArrayList<Discount> Orderarraydic = new ArrayList<Discount>();
			String yn1 = reader.readLine();
			if (yn1.equals("y")) {
				
				int disc_ch1 = 0;
				ArrayList<Discount> disc = DBNinja.getDiscountList();
				boolean flag_disc = false;
				System.out.println("Getting discount list...");
				while (flag_disc!=true) {
					for (Discount d : disc) {
						System.out.println(d.toString());
					}

					System.out.println("Which discount do you want to add? Enter the number. Enter -1 to stop adding discounts: ");

					disc_ch1 = Integer.parseInt(reader.readLine());

					if (disc_ch1 != -1) {
						
						
						for(Discount d : disc) {
							if(d.getDiscountID() == disc_ch1 ) {
								Orderarraydic.add(d);
								
								DBNinja.useOrderDiscount(ret.getOrderID(), disc_ch1 );
								
							}

						}
					
					} else if(disc_ch1 == -1){
						System.out.println("Do you want to add more discounts to this Order? Enter y/n:");
						boolean m_choice = reader.readLine().equals("y");
						if (m_choice) {
							continue;
						}
						break;
					} else{
						flag_disc=true;
					}
				}
			}
			DBNinja.PriceAndCostCalculation(ret.getOrderID(),ret.getPizzaID(),p_cus_price_t,p_bus_price_t,cur_inv_Selected, arraydic);


		} catch (NumberFormatException e) {
			System.out.println("Invalid Input Entered !!!");
			System.out.println("Message     : " + e.getMessage());
		}

		return ret;
	}

	
	
	public static void PrintReports() throws SQLException, NumberFormatException, IOException{
		/*
		 * This method asks the use which report they want to see and calls the DBNinja method to print the appropriate report.
		 * 
		 */

		// User Input Prompts...
		/*System.out.println("Which report do you wish to print? Enter\n(a) ToppingPopularity\n(b) ProfitByPizza\n(c) ProfitByOrderType:");
		System.out.println("I don't understand that input... returning to menu...");*/

		try{
			BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
		
			int uChoice = 0;
			System.out.println("Which report do you wish to print?");
			System.out.println("(a) ToppingPopularity");
			System.out.println("(b) ProfitByPizza");
			System.out.println("(c) ProfitByOrderType");
			uChoice = rd.readLine().toLowerCase().charAt(0);
			   
			System.out.println();
			switch(uChoice){
				case 'a':
					DBNinja.printToppingPopReport();
					break;
				case 'b':
					DBNinja.printProfitByPizzaReport();
					break;
				case 'c':
					DBNinja.printProfitByOrderType();
					break;

			    default:
				    System.out.println("I don't understand that input... returning to menu");
				    break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}



	//Prompt - NO CODE SHOULD TAKE PLACE BELOW THIS LINE
	// DO NOT EDIT ANYTHING BELOW HERE, THIS IS NEEDED TESTING.
	// IF YOU EDIT SOMETHING BELOW, IT BREAKS THE AUTOGRADER WHICH MEANS YOUR GRADE WILL BE A 0 (zero)!!

	public static void PrintMenu() {
		System.out.println("\n\nPlease enter a menu option:");
		System.out.println("1. Enter a new order");
		System.out.println("2. View Customers ");
		System.out.println("3. Enter a new Customer ");
		System.out.println("4. View orders");
		System.out.println("5. Mark an order as completed");
		System.out.println("6. View Inventory Levels");
		System.out.println("7. Add Inventory");
		System.out.println("8. View Reports");
		System.out.println("9. Exit\n\n");
		System.out.println("Enter your option: ");
	}

	/*
	 * autograder controls....do not modiify!
	 */

	public final static String autograder_seed = "6f1b7ea9aac470402d48f7916ea6a010";

	
	private static void autograder_compilation_check() {

		try {
			Order o = null;
			Pizza p = null;
			Topping t = null;
			Discount d = null;
			Customer c = null;
			ArrayList<Order> alo = null;
			ArrayList<Discount> ald = null;
			ArrayList<Customer> alc = null;
			ArrayList<Topping> alt = null;
			double v = 0.0;
			String s = "";

			DBNinja.addOrder(o);
			DBNinja.addPizza(p);
			DBNinja.useTopping(p, t, false);
			//DBNinja.usePizzaDiscount(p, d);
			//DBNinja.useOrderDiscount(o, d);
			DBNinja.addCustomer(c);
			DBNinja.completeOrder(o);
			alo = DBNinja.getOrders(false);
			o = DBNinja.getLastOrder();
			alo = DBNinja.getOrdersByDate("01/01/1999");
			ald = DBNinja.getDiscountList();
			d = DBNinja.findDiscountByName("Discount");
			alc = DBNinja.getCustomerList();
			c = DBNinja.findCustomerByPhone("0000000000");
			alt = DBNinja.getToppingList();
			t = DBNinja.findToppingByName("Topping");
			DBNinja.addToInventory(t, 1000.0);
			v = DBNinja.getBaseCustPrice("size", "crust");
			v = DBNinja.getBaseBusPrice("size", "crust");
			DBNinja.printInventory();
			DBNinja.printToppingPopReport();
			DBNinja.printProfitByPizzaReport();
			DBNinja.printProfitByOrderType();
			s = DBNinja.getCustomerName(0);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}


}


