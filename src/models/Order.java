package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author 30257320
 */
public class Order {
    //private attributes
    private int orderId;
    private Date orderDate;
    private double orderTotal;
    private String status;
    private HashMap<Integer, OrderLine> orderLine;
    
    //getters & Setters
    public int getOrderId() {return orderId;}
    public void setOrderId(int orderIdIn) {orderId = orderIdIn;}

    public Date getOrderDate() {return orderDate;}
    public void setOrderDate(Date orderDateIn) {orderDate = orderDateIn;}

    public double getOrderTotal() {return orderTotal;}
    public void setOrderTotal(double orderTotalIn) {orderTotal = orderTotalIn;}

    public String getStatus() {return status;}
    public void setStatus(String statusIn) {status = statusIn;}
    
    public HashMap<Integer, OrderLine> getOrderLines() {return orderLine;}
    public void setOrderLines(HashMap<Integer, OrderLine> orderLinesIn) {orderLine = orderLinesIn;}
    
    //get current open orderline
    public Optional<OrderLine> orderLineInBasketWithProduct(int productId){
       Optional<OrderLine> orderLineWithProduct = Optional.empty();
       
       //for every entry in the orderline hashMap
       for(Map.Entry<Integer, OrderLine> olEntry : orderLine.entrySet()){
           //get each product
           OrderLine actualOrderLine = olEntry.getValue();
           //check the product is the same as database
           Product product = actualOrderLine.getProduct();
           
           //if the product id's match
           if(product.getProductId() == productId){
               //add to orderline
               orderLineWithProduct = Optional.of(actualOrderLine);
           }
       }
       return orderLineWithProduct;
    }
    
    //update product availability when purchased
    public void updateProductAvailablility(){
        //for every entry in the orderline hashMap
        for(Map.Entry<Integer, OrderLine> olEntry : orderLine.entrySet()){
            //get each product
            OrderLine actualOrderLine = olEntry.getValue();
            //check the product is the same as database
            Product orderedProduct = actualOrderLine.getProduct();
            //connect to the database
            DBManager db = new DBManager();
            //call the updateProductAvailability method passing in product & orderLine
            db.updateProductAvailability(orderedProduct, actualOrderLine);
        }
    }
    
    public void removeOrderLine(int productId){
        //loop through each orderLine
        Iterator<Map.Entry<Integer, OrderLine>> iter = orderLine.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<Integer, OrderLine> entry = iter.next();
            //if the the procuct id matches
            if(entry.getValue().getProduct().getProductId() == productId){
                //check the orderLineId matches
                int orderLineId = entry.getValue().getOrderLineId();
                iter.remove();
                //remove the current orderLine total from orderTotal
                orderTotal = orderTotal - entry.getValue().getLineTotal();
                
                //access the database
                DBManager db = new DBManager();
                //delete the orderLine using the id as key
                db.deleteOrderLine(orderLineId);
                //update the order total to match
                db.updateOrderTotal(orderId, orderTotal);
            }
        }
    }
    
    public void calculateOrderTotal(){
        orderTotal = 0;
        for(Map.Entry<Integer, OrderLine>olEntry : orderLine.entrySet()){
            OrderLine ol = olEntry.getValue();
            orderTotal = orderTotal + ol.getLineTotal();
        }
        
        DBManager db = new DBManager();
        db.updateOrderTotal(orderId, orderTotal);
    }
    
    public void addOrderLine(OrderLine ol){
        //add order to database
        DBManager db = new DBManager();
        int orderLineId = db.addOrderLine(ol, orderId);
        
        //add new order to hashmap
        orderLine.put(orderLineId, ol);
        
        //update orderid from hashmap to match database
        orderLine.get(orderLineId).setOrderLineId(orderLineId);
        
        calculateOrderTotal();
    }
    
    public int genUniqueOrderLineId(){
        int orderLineId = 1;
        
        //loop until it finds unused id
        for(Map.Entry<Integer, OrderLine>olEntry : orderLine.entrySet()){
            if(orderLine.containsKey(orderLineId)){
                orderLineId++;
            }
        }
        return orderLineId;
    }
    
    //default constructor
    public Order(){
        orderId = 0;
        orderDate = new Date();
        orderTotal = 0.0;
        status = "Processing";
        orderLine = new HashMap();
    }
    
//    //constructor to setting orderDate to new Date() & orderLine to new HashMap
//    public Order(Date orderDateIn, HashMap<String, OrderLine> orderLinesIn){
//        orderDate = new Date();
//        orderLine = new HashMap();
//    }
    
    //Order(orderId, orderDate, orderTotal, status);
    public Order(int orderIdIn, Date orderDateIn, double orderTotalIn, String statusIn){
        orderId = orderIdIn;
        orderDate = orderDateIn;
        orderTotal = orderTotalIn;
        status = statusIn;
        orderLine = new HashMap();
    }
    
//    //constructor for everything but orderLine onderline = new HashMap
//    public Order(int orderIdIn, Date orderDateIn, double orderTotalIn, String statusIn, HashMap<String, OrderLine> orderLinesIn){
//        orderId = orderIdIn;
//        orderDate = new Date();
//        orderDate = orderDateIn;
//        orderTotal = orderTotalIn;
//        status = statusIn;
//        orderLine = new HashMap();
//    } 
}
