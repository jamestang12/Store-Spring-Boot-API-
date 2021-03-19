package com.example.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;


import org.springframework.data.annotation.Id;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;


@Document(collection = "Posts")
public class Post{

    @Id
    private String id;
    private Double price;
    private String title;
    private LocalDateTime date;
    private Double bud;
    private String desc;
    private String budPerson;
    private String usernmae;
    private ArrayList<String> images;
    private String location;


    public Post(){}

    public Post(String location,String id, Double price, LocalDateTime date, Double bud,String title, String desc, String budPerson, String username, ArrayList<String> images){
        this.id = id;
        this.price = price;
        this.title = title;
        this.date = date;
        this.bud = bud;
        this.desc = desc;
        this.budPerson = budPerson;
        this.usernmae = username;
        this.images = images;
        this.location = location;
    }

    public ArrayList<String> getImages(){
        return this.images;
    }

    public String getLocation(){
        return this.location;
    }

    public String getid(){
        return this.id;
    }

    public Double getPrice(){
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

    public Double getBud(){
        return this.bud;
    }

     public void setid(String id){
        this.id = id;
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

    public void setLocation(String location){
        this.location = location;
    }

    public void setImages(ArrayList<String> images){
        this.images = images;
    }
}
