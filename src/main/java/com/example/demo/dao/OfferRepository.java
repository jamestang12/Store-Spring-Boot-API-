package com.example.demo.dao;


import com.example.demo.model.Offer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;


public interface OfferRepository extends MongoRepository<Offer, String>{
    
}

