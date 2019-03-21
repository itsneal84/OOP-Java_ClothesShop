package models;

/**
 *
 * @author 30257320
 */
public class OrderLine {
    //private attributes
    private int orderLineId;
    private Product product;
    private int quantity;
    private double lineTotal;
    
    //getters & setters
    public int getOrderLineId() {return orderLineId;}
    public void setOrderLineId(int orderLineIdIn) {orderLineId = orderLineIdIn;}

    public Product getProduct() {return product;}
    public void setProduct(Product productIn) {product = productIn;}

    public int getQuantity() {return quantity;}
    public void setQuantity(int quantityIn) {quantity = quantityIn;}

    public double getLineTotal() {return lineTotal;}
    public void setLineTotal(double lineTotalIn) {lineTotal = lineTotalIn;}
    
    //constructor
    public OrderLine(int orderLineIdIn, Product productIn, int quantityIn){
        orderLineId = orderLineIdIn;
        product = productIn;
        quantity = quantityIn;
        lineTotal = product.getPrice() * quantityIn;
    }
}
