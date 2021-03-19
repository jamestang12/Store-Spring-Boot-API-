package com.example.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;



@Document(collection = "Offer")
public class Offer{
    @Id
    private String id;
    private String postid;
    private String sender;
    private String owner;
    private LocalDateTime date;
    private String text;


    public Offer(){}

    public Offer(String id, String postid, String sender, String owner, LocalDateTime date, String text){
        this.id = id;
        this.postid = postid;
        this.sender = sender;
        this.owner = owner;
        this.date = date;
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public String getId(){
        return this.id;
    }

    public String getPostid(){
        return this.postid;
    }

    public String getSender(){
        return this.sender;
    }

    public String getOwner(){
        return this.owner;
    }

    public LocalDateTime getDate(){
        return this.date;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setPostid(String postid){
        this.postid = postid;
    }

    public void setSender(String sender){
        this.sender = sender;
    }

    public void sentOwner(String owner){
        this.owner = owner;
    }

    public void setdate(LocalDateTime date){
        this.date = date;
    }

    public void setText(String text){
        this.text = text;
    }

}

