package models;

/**
 *
 * @author 30257320
 */
public class Product {
    //private attributes
    private int productId;
    private String productName;
    private double price;
    private int stockLevel;
    
    @Override
    public String toString(){
        String output = productName;
        return output;
    }
    
    //getter methods
    public int getProductId() { return productId;}
    public String getProductName() { return productName;}
    public double getPrice() { return price; }
    public int getStockLevel() { return stockLevel; }

    //setter methods
    public void setProductId(int productIdIn) { productId = productIdIn; }
    public void setProductName(String productNameIn) { productName = productNameIn; }
    public void setPrice(double priceIn) { price = priceIn; }
    public void setStockLevel(int stockLevelIn) { stockLevel = stockLevelIn; }
    
    //default zero constructor
    public Product(){
        productId = 0;
        productName = "";
        price = 0.0;
        stockLevel = 0;
    }
    
    //overloaded constructor without productId
    public Product(String productNameIn, double priceIn, int stockLevelIn){
        productId = 0;
        productName = productNameIn;
        price = priceIn;
        stockLevel = stockLevelIn;
    }
    
    //overloaded constructor with everything
    public Product(int productIdIn, String productNameIn, double priceIn, int stockLevelIn){
        productId = productIdIn;
        productName = productNameIn;
        price = priceIn;
        stockLevel = stockLevelIn;
    }
}
