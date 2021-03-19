package com.example.demo.dao;


import com.example.demo.model.OfferContext;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;




public interface OfferContextRepository extends MongoRepository<OfferContext, String>{
    
}