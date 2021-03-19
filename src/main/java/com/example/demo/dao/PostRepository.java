package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Post;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;

public interface PostRepository extends MongoRepository<Post, String>{


    // Post findById(String postid);
    //Post findByUsername(String username);
    List<Post> findByUsername(String username);
}