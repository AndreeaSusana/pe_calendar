/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwa;

/**
 *
 * @author cklose
 */
public class SessionHandler {
    static String username;
    int ID;
    
    public static void setUsername(String name){
        username = name;
    }
    
    public void setID(int id){
        ID = id;
    }
    
    public int getUserID(){
        return ID;
    }
    
    public static String getUsername() {
        return username;
    }
}
