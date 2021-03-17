package com.example.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "User")
public class User{
    
    @Id
    private String id;
    private String passowrd;
    private String email;
    private String username;


    
    public User(String id, String password, String email, String username){
        this.id = id;
        this.email = email;
        this.username = username;
        this.passowrd = password;
    }
    
    public String getId(){
        return this.id;
    }

    public String getPassword(){
        return this.passowrd;
    }

    public String getUsername(){
        return this.username;
    }

    public String getEmail(){
        return this.email;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassowrd(String password){
        this.passowrd = password;
    }

    public void setUsername(String username){
        this.username = username;
    }

    


}