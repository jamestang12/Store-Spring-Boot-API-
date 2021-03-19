package com.example.demo.dao;


import java.util.List;

import com.example.demo.model.Offer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;


public interface OfferRepository extends MongoRepository<Offer, String>{
    List<Offer> findByPostid(String id);
    List<Offer> findByOwner(String id);
    List<Offer> findBySender(String id);

}

