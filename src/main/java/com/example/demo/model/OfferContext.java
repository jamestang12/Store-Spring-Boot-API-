package com.example.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;


@Document(collection = "OfferContext")
public class OfferContext{
    @Id
    private String id;
    private String senderid;
    private String receverid;
    private String postid;
    private String text;
    private LocalDateTime date;

    public OfferContext(){}

    public OfferContext(String id, String senderid, String receverid, String postid, String text, LocalDateTime date){
        this.id = id;
        this.senderid = senderid;
        this.receverid = receverid;
        this.postid = postid;
        this.text = text;
        this.date = date;
    }

    public String getId(){
        return this.id;
    }

    public String getSenderid(){
        return this.senderid;
    }

    public String getReceverid(){
        return this.receverid;
    }

    public String getPostid(){
        return this.postid;
    }

    public String getText(){
        return this.text;
    }

    public LocalDateTime getDate(){
        return this.date;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setSenderid(String senderId){
        this.senderid = senderId;
    }

    public void setReceverid(String receverid){
        this.receverid = receverid;
    }

    public void setPostid(String postid){
        this.postid = postid;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setDate(LocalDateTime date){
        this.date = date;
    }
     
}
