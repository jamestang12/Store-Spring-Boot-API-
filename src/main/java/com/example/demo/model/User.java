package com.example.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime; 

@Document(collection = "Users")
public class User{
    
    @Id
    private String id;
    private String password;
    private String email;
    private String username;
    private LocalDateTime dateCreated;

    public User(){}
    
    public User(String id, String password, String email, String username, LocalDateTime dateCreated){
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.dateCreated = dateCreated;
    }
    
    public String getId(){
        return this.id;
    }

    public LocalDateTime getDate(){
        return this.dateCreated;
    }

    public String getPassowrd(){
        return this.password;
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

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

   public void setDate(LocalDateTime date){
       this.dateCreated = date;
   }

    public void setUsername(String username){
        this.username = username;
    }

    


}