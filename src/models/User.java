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
public class User {
    //attributes
    private String username;
    private String password;
    private String firstname;
    private String lastname;

    //getter methods
    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public String getFirstname() {return firstname;}
    public String getLastname() {return lastname;}

    //setter methods
    public void setUsername(String usernameIn) {username = usernameIn;}
    public void setPassword(String passwordIn) {password = passwordIn;}
    public void setFirstname(String firstnameIn) {firstname = firstnameIn;}
    public void setLastname(String lastnameIn) {lastname = lastnameIn;}
    
    //default zero constructor
    public User(){
        username = "";
        password = "";
        firstname = "";
        lastname = "";
    }
    
    //overloaded constructor
    public User(String usernameIn, String passwordIn, String firstnameIn, String lastnameIn){
        username = usernameIn;
        password = passwordIn;
        firstname = firstnameIn;
        lastname = lastnameIn;
    }
}
