package com.example.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;


import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;


@Document(collection = "Posts")
public class Post{

    private String postid;
    private Double price;
    private String title;
    private LocalDateTime date;
    private Double bud;
    private String desc;
    private String budPerson;
    private String usernmae;


    public Post(){}

    public Post(String postid, Double price, LocalDateTime date, Double bud,String title, String desc, String budPerson, String username){
        this.postid = postid;
        this.price = price;
        this.title = title;
        this.date = date;
        this.bud = bud;
        this.desc = desc;
        this.budPerson = budPerson;
        this.usernmae = username;
    }

    public String getPostId(){
        return this.getPostId();
    }

    public Double getProce(){
        return this.price;
    }

    public String getTitle(){
        return this.title;
    }

    public LocalDateTime getDate(){
        return this.date;
    }

    public String getDesc(){
        return this.desc;
    }

    public String getBudPerson(){
        return this.budPerson;
    }

    public String getUsername(){
        return this.usernmae;
    }

     public void setPostId(String id){
        this.postid = id;
    }

    public void setPrice(Double price){
        this.price = price;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDate(LocalDateTime date){
        this.date = date;
    }

    public void setBud(Double bud){
        this.bud = bud;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public void setBudPerson(String budPerson){
        this.budPerson = budPerson;
    }

    public void setUsername(String username){
        this.usernmae = username;
    }
}
