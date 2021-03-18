package com.example.demo.Controller;
import java.util.List;
import java.util.UUID;

import com.example.demo.Encryption.AES;
import com.example.demo.dao.UserRepository;
import com.example.demo.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime; 
import org.springframework.beans.factory.annotation.Value;



@RestController
public class UserController{


    @Value("${secretKey}")
    private String secretKey;

    @Autowired
    public UserRepository userRepository;

    // @GetMapping(value="/user/{id}")
    // public User getUserById(@PathVariable("id") String id){
    //     User user = userRepository.findBy(id)
    //     User returnValue = new User(user.getId(), "", user.getEmail(), user.getId(), user.getDate());
    //     return returnValue;
    // }

    @PostMapping(value="/createUser")
    public String createUser(@RequestBody User user){
        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setDate(LocalDateTime.now());

     
        String encryptedString = AES.encrypt(user.getPassowrd(), secretKey) ;
        user.setPassword(encryptedString);
        User insertedUser = userRepository.insert(user);


        return "User created";
    }


    



    
}