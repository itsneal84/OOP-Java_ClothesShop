package models;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 30257320
 */
public class Customer extends User {
    //private attribures
    private String addressLine1;
    private String addressLine2;
    private String town;
    private String postcode;
    private boolean isRegistered;
    private HashMap<Integer, Order>orders;
    
    //getters & setters
    public String getAddressLine1() {return addressLine1;}
    public void setAddressLine1(String addressLine1In) {addressLine1 = addressLine1In;}

    public String getAddressLine2() {return addressLine2;}
    public void setAddressLine2(String addressLine2In) {addressLine2 = addressLine2In;}

    public String getTown() {return town;}
    public void setTown(String townIn) {town = townIn;}

    public String getPostcode() {return postcode;}
    public void setPostcode(String postcodeIn) {postcode = postcodeIn;}

    public boolean getIsRegistered() {return isRegistered;}
    public void setIsRegistered(boolean isRegisteredIn) {isRegistered = isRegisteredIn;}
    
    public HashMap<Integer, Order>getOrders(){return orders;}
    public void setOrders(HashMap<Integer, Order> ordersIn) {orders = ordersIn;}
    
    public Order findLatestOrder(){
        Order currentOrder = new Order();
        //int uniqueOrderId = genUniqueOrderId();
        //currentOrder.setOrderId(uniqueOrderId);
        
        //check if the customer has any current order
        if(orders.isEmpty()){
            //if so add to the order
            addOrder(currentOrder);
        }
        else{
            //if not create a new order
            Order lastOrder = orders.entrySet().iterator().next().getValue();
            for(Map.Entry<Integer, Order> oEntry : orders.entrySet()){
                Order actualOrder = oEntry.getValue();
                if(actualOrder.getOrderDate().after(lastOrder.getOrderDate())){
                    lastOrder = actualOrder;
                }
            }
            //make sure previous order iwas complete or still active
            if(lastOrder.getStatus().equals("Complete")){
                //if so add to order
                addOrder(currentOrder);
            }
            else{
                //if not create a new order
                currentOrder = lastOrder;
            }
        }
        return currentOrder;
    }
    
    public void addOrder(Order order){
        //add order to database
        DBManager db = new DBManager();
        int orderId = db.addOrder(order, this.getUsername());
        
        //add order to hashmap
        orders.put(orderId, order);
        
        //update orderid to match database
        orders.get(orderId).setOrderId(orderId);
    }
    
    public int genUniqueOrderId(){
        int orderId = 0;
        
        //loop until it finds unused id
        for(Map.Entry<Integer, Order>oEntry : orders.entrySet()){
            if(orders.containsKey(orderId)){
                orderId++;
            }
        }
        return orderId;
    }
    
    //add a personal greeting to the logged in customer
    public String greeting(){
        String greeting = "<html>Welcome to your personal home page "+ this.getFirstname();
        return greeting;
    }

    //default contructor
    public Customer(){
        super();
        addressLine1 = "21";
        addressLine2 = "Random Road";
        town = "Glasgow";
        postcode = "G4 5GH";
        isRegistered = false;
        orders = new HashMap<>();
    }
    
    //contructor for all but isRegistered
    public Customer(String addressLine1In, String addressLine2In, String townIn, String postcodeIn, String usernameIn, String passwordIn, String firstnameIn, String lastnameIn){
        super(usernameIn, passwordIn, firstnameIn, lastnameIn);
        addressLine1 = addressLine1In;
        addressLine2 = addressLine2In;
        town = townIn;
        postcode = postcodeIn;
        isRegistered = true;
        orders = new HashMap<>();
    }
}
