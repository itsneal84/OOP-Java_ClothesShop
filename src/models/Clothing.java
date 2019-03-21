/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author 30257320
 */
public class Clothing extends Product {
    /**
     * @return the measurement
     */
    private String measurement;
    
    public String getMeasurement() {return measurement;}
    public void setMeasurement(String measurementIn) {measurement = measurementIn;}
    
    //default constructor
    public Clothing(){
        super();
        measurement = "M";
    }
    
    //constructor for everything except productId
    public Clothing(String measurementIn, String productNameIn, double priceIn, int stockLevelIn){
        super(productNameIn, priceIn, stockLevelIn);
        measurement = "M";
    }
    
    //constructor for everything
    public Clothing(String measurementIn, int productId, String productNameIn, double priceIn, int stockLevelIn){
        super(productId, productNameIn, priceIn, stockLevelIn);
        measurement = "M";
    }
}
