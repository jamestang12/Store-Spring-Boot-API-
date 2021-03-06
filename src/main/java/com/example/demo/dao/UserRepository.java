package com.example.demo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;




import com.example.demo.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    User findByUsername(String username);
    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

}