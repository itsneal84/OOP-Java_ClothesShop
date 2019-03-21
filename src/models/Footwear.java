package models;

/**
 *
 * @author 30257320
 */
public class Footwear extends Product{
    //private attribures
    private int size;
    
    //getters & stters
    public int getSize() {return size;}
    public void setSize(int sizeIn) {size = sizeIn;}
    
    //default constructor
    public Footwear(){
        super();
        size = 8;
    }
    
    //constructor for everything except productId
    public Footwear(int sizeIn, String productNameIn, double priceIn, int stockLevelIn){
        super(productNameIn, priceIn, stockLevelIn);
        size = sizeIn;
    }
    
    //constructor for everything
    public Footwear(int sizeIn, String productNameIn, int productIdIn, double priceIn, int stockLevelIn){
        super(productIdIn, productNameIn, priceIn, stockLevelIn);
        size = sizeIn;
    }
}
