/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.HashMap;

/**
 *
 * @author 30257320
 */
public class Staff extends User {
    //private attribures
    private String position;
    private double salary;
    private HashMap<Integer, Order>orders;
    
    //add a personal greeting to the logged in customer
    public String greeting(){
        String greeting = "<html>Welcome to the Staff home page "+ this.getFirstname();
        return greeting;
    }
    
    //getters & setters
    public String getPosition() {return position;}
    public void setPosition(String positionIn) {position = positionIn;}

    public double getSalary() {return salary;}
    public void setSalary(double salaryIn) {salary = salaryIn;}
    
    //default constructor    
    public Staff(){
        super();
        position = "Checkout";
        salary = 15.000;
        orders = new HashMap<>();
    }
    
    //constructor for everything
    public Staff(String positionIn, double salaryIn, String usernameIn, String passwordIn, String firstnameIn, String lastnameIn){
        super(usernameIn, passwordIn, firstnameIn, lastnameIn);
        position = positionIn;
        salary = salaryIn;
        orders = new HashMap<>();
    }
}
