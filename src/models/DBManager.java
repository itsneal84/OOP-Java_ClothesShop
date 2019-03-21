package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 30257320
 */
public class DBManager {
    //database access via imported jar libraries
    private final String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
    private final String connectionString = "jdbc:ucanaccess://E:\\OneDrive - City of Glasgow College\\HND\\Object Orientated Programming Java\\Shop_30257320\\data\\ShopDB.accdb";
    
    //--------------------LOAD METHODS--------------------//
    
    public HashMap<String, Staff> loadStaff() //String for key will be username
    {
        HashMap<String, Staff> staffMembers = new HashMap();
        
        //try to open a connection to the database
        try{
            //open connection to database
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            
            //find table Staff
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Staff");
            
            //get the information from the database & save in appropriate variables
            while(rs.next()){
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                String firstname = rs.getString("FirstName");
                String lastname = rs.getString("LastName");
                String position = rs.getString("Position");
                Double salary = rs.getDouble("Salary");
                
                //constructor from staff class: String positionIn, double salaryIn, String usernameIn, String passwordIn, String firstnameIn, String lastnameIn
                Staff staff = new Staff(position, salary, username, password, firstname, lastname);
                
                staffMembers.put(username, staff);//adds staff0 to HashMap using username as key
            }
            //close connection
            conn.close();
        }
        //catch any errors and display the error message
        catch(Exception ex){
            String message = ex.getMessage();
        }
        //return the staff members
        finally{
            return staffMembers;
        }
    }//end loadStaff
    
    public HashMap<String, Customer> loadCustomer() //String for key will be username
    {
        HashMap<String, Customer> customers = new HashMap();
        
        //try to open a connection to the database
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customers");
            
            while(rs.next()){
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                String firstname = rs.getString("FirstName");
                String lastname = rs.getString("LastName");
                String addressLine1 = rs.getString("AddressLine1");
                String addressLine2 = rs.getString("AddressLine2");
                String town = rs.getString("Town");
                String postcode = rs.getString("Postcode");
                
                //constructor from customer class: String addressLine1In, String addressLine2In, String townIn, String postcodeIn, String usernameIn, String passwordIn, String firstnameIn, String lastnameIn
                Customer customer = new Customer(addressLine1, addressLine2, town, postcode, username, password, firstname, lastname);
                
                customers.put(username, customer);//adds customer0 to HashMap using username as key
            }
            //closr connection
            conn.close();
        }
        //catch any errors and display the error message
        catch(Exception ex){
            String message = ex.getMessage();
        }
        //return the staff members
        finally{
            customers = loadCustomerOrders(customers);
            customers = loadCustomerOrderLines(customers);
            return customers;
        }
    }//end loadCustomer
    
    public HashMap<Integer,Product> loadProducts(){ //key will be productId
        HashMap<Integer,Product>products = new HashMap();
        
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Products");
            
            while(rs.next()){
                int productId = rs.getInt("ProductId");
                String productName = rs.getString("ProductName");
                double price = rs.getDouble("Price");
                int stockLevel = rs.getInt("StockLevel");
                String measurement = rs.getString("Measurement");
                int size = rs.getInt("Size");
                
                if(size > 0){
                    //(int sizeIn, String productNameIn, int productIdIn, double priceIn, int stockLevelIn)
                    Footwear footwear = new Footwear(size, productName, productId, price, stockLevel);
                    
                    products.put(productId, footwear);
                }
                else{
                    //(String measurementIn, int productIdIn, String productNameIn, double priceIn, int stockLevelIn){
                    Clothing clothing = new Clothing(measurement, productId, productName, price, stockLevel);
                    
                    products.put(productId, clothing);
                }
            }
            conn.close();
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
        finally{
            return products;
        }
    }//end loadProducts
    
    public HashMap<String,Customer> loadCustomerOrders(HashMap<String, Customer> customer){
        
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Orders");
            
            while(rs.next()){
                int orderId = rs.getInt("OrderId");
                
                String strOrderDate = rs.getString("OrderDate");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date orderDate = format.parse(strOrderDate);
                
                String username = rs.getString("Username");
                double orderTotal = rs.getDouble("OrderTotal");
                String status = rs.getString("Status");
                
                Order loadedOrder = new Order(orderId, orderDate, orderTotal, status);
                
                if(customer.containsKey(username)){
                    Customer cust = customer.get(username);
                    cust.getOrders().put(orderId, loadedOrder);
                }
            }
            conn.close();
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
        finally{
            return customer;
        }
    }//end loadCustomerOrders
    
    public HashMap<String,Customer> loadCustomerOrderLines(HashMap<String, Customer> customer){
        
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM OrderLines");
            
            while(rs.next()){
                int orderLineId = rs.getInt("OrderLineId");
                int productId = rs.getInt("ProductId");
                int quantity = rs.getInt("Quantity");
                double lineTotal = rs.getDouble("LineTotal");
                int orderId = rs.getInt("OrderId");
                
                HashMap<Integer, Product> product = loadProducts();
                Product loadedProduct = new Product();
                
                if(product.containsKey(productId)){
                    loadedProduct = product.get(productId);
                }
                
                //using constructor: int orderLineIdIn, Product productIn, int quantityIn
                OrderLine loadedOrderLine = new OrderLine(orderLineId, loadedProduct, quantity);
                
                for(Map.Entry<String, Customer> custEntry : customer.entrySet()){
                    Customer actualCustomer = custEntry.getValue();
                    
                    if(actualCustomer.getOrders().containsKey(orderId)){
                        Order orderForOrderLine = actualCustomer.getOrders().get(orderId);
                        orderForOrderLine.getOrderLines().put(orderLineId, loadedOrderLine);
                    }
                }
            }
            conn.close();
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
        finally{
            return customer;
        }
    }//end loadCustomerOrders
    
    //--------------------ADD METHODS--------------------//
    
    public void addProduct(Product product){
        String measurement = "";
        int size = 0;
        
        //check if the product is clothing
        if(product.getClass().getName().equals("models.Clothing")){
            Clothing clothing = (Clothing)product;
            
            measurement = String.valueOf(clothing.getMeasurement());
        }
        //if not add as footwear
        else{
            Footwear footwear = (Footwear)product;
            
            size = footwear.getSize();
        }
        
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO Products (ProductName, Price, StockLevel, Measurement, Size) " 
                    + "VALUES ('" + product.getProductName() + "', '" + product.getPrice() + "', '" + product.getStockLevel()
                    + "', '" + measurement + "', '" + size + "')");
            
            conn.close();
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
    }//end addProduct
    
    public int addOrderLine(OrderLine ol, int orderId){
        int orderLineId = 0;
        
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO OrderLines (OrderLineId, ProductId, Quantity, LineTotal, OrderId)" + "VALUES ('"+ ol.getOrderLineId() + "','"
            + ol.getProduct().getProductId() + "','" + ol.getQuantity() + "','" + ol.getLineTotal() + "','" + orderId + "')");
            
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                orderLineId = rs.getInt(1);
            }
            conn.close();
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
        return orderLineId;
    }//end addOrderLine
    
    public int addOrder(Order o, String customerUsername){
        int orderLineId = 0;
        
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO Orders (OrderDate, Username, OrderTotal, Status)" + "VALUES ('"
            + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(o.getOrderDate()) + "','"
            + customerUsername + "','" + o.getOrderTotal() + "','" + o.getStatus() + "')");
            
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                orderLineId = rs.getInt(1);
            }
            conn.close();
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
        return orderLineId;
    }//end addOrder
    
    //--------------------LOGIN METHODS--------------------//
    
    public Staff StaffLogin(String username, String password){
        //load staff members from hashmap
        HashMap<String, Staff>staffMembers = loadStaff();
        
        //search hashmap for username
        if(staffMembers.containsKey(username)){
            Staff foundStaff = staffMembers.get(username);
            
            //if username and password exist
            if(foundStaff.getPassword().equals(password)){
                //return the valis staff memeber
                return foundStaff;
            }
            //if not return nothing
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }//end staffLogin
    
    public Customer CustomerLogin(String username, String password){
        //load customers from hashmap
        HashMap<String, Customer>customers = loadCustomer();
        
        //search hashmap for username
        if(customers.containsKey(username)){
            Customer foundCustomer = customers.get(username);
            
            //if username and password exist
            if(foundCustomer.getPassword().equals(password)){
                //return valid customer
                return foundCustomer;
            }
            //if not return nothing
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }//end customerLogin
    
    //--------------------REGISTER METHODS--------------------//
    
    public boolean customerRegister(Customer customer){
        //try to open a connection to the database
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            
            Statement stmt = conn.createStatement();
            //using data entered in customerregistration add to correct tables in the customer database
            stmt.executeUpdate("INSERT INTO Customers (Username, Password, FirstName, LastName, AddressLine1, AddressLine2, Town, Postcode)" 
                    + "VALUES ('" + customer.getUsername() + "', '" + customer.getPassword() + "', '" + customer.getFirstname() + "', '" + customer.getLastname() + "', '"
                    + customer.getAddressLine1() + "', '" + customer.getAddressLine2() + "', '" + customer.getTown() + "', '" + customer.getPostcode()+ "')");
            
            conn.close();
            //return true if data entered successfuly & customer registration was complete
            return true;
        }
        //catch any errors and display the error message
        catch(Exception ex){
            String message = ex.getMessage();
            //return false if error occured and customer was not registered
            return false;
        }
    }//end customerRegister
    
    //--------------------EDIT METHODS--------------------//
    
    public void editCustomer(Customer customer){
        //try to open a connection to the database
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            
            Statement stmt = conn.createStatement();
            //if changes have been made update correct tables in the customer database
            stmt.executeUpdate("UPDATE Customers SET Password = '" + customer.getPassword() + 
                    "', FirstName = '" + customer.getFirstname() +
                    "', LastName = '" + customer.getLastname() +
                    "', AddressLine1 = '" + customer.getAddressLine1() +
                    "', AddressLine2 = '" + customer.getAddressLine2() +
                    "', Town = '" + customer.getTown() +
                    "', Postcode = '" + customer.getPostcode() + 
                    "' WHERE Username = '" + customer.getUsername() + "'");
            
            conn.close();
        }
        //catch any errors and display the error message
        catch(Exception ex){
            String message = ex.getMessage();
        }
    }//end customerRegister
    
    public void editProduct(Product product){
        String measurement = "";
        String size = "NULL";
        
        //check if the product is clothing
        if(product.getClass().getName().equals("models.Clothing")){
            Clothing clothing = (Clothing)product;
            measurement = String.valueOf(clothing.getMeasurement());
        }
        //if not add as footwear
        else{
            Footwear footwear = (Footwear)product;
            size = String.valueOf(footwear.getSize());
        }
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE Products SET ProductName = '" + product.getProductName() + "',"
                    + "Price = '" + product.getPrice() + "'," 
                    + "StockLevel = '" + product.getStockLevel() + "'," 
                    + "Measurement = '" + measurement + "'," 
                    + "Size = " + size 
                    + " WHERE ProductId = '" + product.getProductId() + "'");
            conn.close();
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
    }//end editProduct
    
    public void editOrderLine(OrderLine ol){
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            Statement stmt= conn.createStatement();
            stmt.executeUpdate("UPDATE OrderLines Set Quantity = '" + ol.getQuantity() + 
                    "' " + "LineTotal = '" + ol.getLineTotal() +
                    " WHERE OrderLineId = '" + ol.getOrderLineId() + "'");
            
            conn.close();
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
    }
    
    public void updateOrderTotal(int orderId, double newTotal){
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            Statement stmt= conn.createStatement();
            stmt.executeUpdate("UPDATE Orders Set OrderTotal = '" + newTotal + "' " + " WHERE OrderId = '" + orderId + "'");
            
            conn.close();
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
    }//end updateOrderTotal
    
    //--------------------DELETE METHODS--------------------//
    
    public void deleteCustomer(Customer customer){
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            Statement stmt = conn.createStatement();
            
            //delete the customer using the username as key
            stmt.executeUpdate("DELETE FROM Customers WHERE Username = '" + customer.getUsername() + "'");
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
    }//end deleteCustomer
    
    public void deleteProduct(Product product){
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            Statement stmt = conn.createStatement();
            
            //delete the product using the productId as key
            stmt.executeUpdate("DELETE FROM Products WHERE ProductId = '" + product.getProductId() + "'");
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
    }//end deleteProduct
    
    public void deleteOrderLine(int orderLineId){
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            Statement stmt = conn.createStatement();
            
            //delete the product using the productId as key
            stmt.executeUpdate("DELETE FROM OrderLines WHERE OrderLineId = '" + orderLineId + "'");
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
    }//end deleteOrderLine
    
    //--------------------ORDER METHODS--------------------//
    
    public void completeOrder(int orderId){
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            Statement stmt = conn.createStatement();
            
            stmt.executeUpdate("UPDATE Orders SET Status = 'Complete' " + " WHERE OrderId = " + orderId + " ");
            conn.close();
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
    }//end completeOrder
    
    public void updateProductAvailability(Product product, OrderLine orderLine){
        int qty = orderLine.getQuantity();
        int newStockLvl = product.getStockLevel() - qty;
        
        try{
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionString);
            Statement stmt = conn.createStatement();
            
            stmt.executeUpdate("UPDATE Products SET StockLevel = '" + newStockLvl + "'WHERE ProductId = '" + product.getProductId() + "'");
            conn.close();
        }
        catch(Exception ex){
            String message = ex.getMessage();
        }
    }//end updateProductAvailability
}
