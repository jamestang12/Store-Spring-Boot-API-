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
import org.springframework.web.bind.annotation.RequestHeader;


import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime; 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.Data;

import java.security.Key;
import io.jsonwebtoken.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;






@RestController
public class UserController{


    @Value("${secretKey}")
    private String secretKey;

    @Value("tokenKey")
    private String key;

    @Autowired
    public UserRepository userRepository;

    @GetMapping(value="/user/{username}")
    public User getUserById(@PathVariable("username") String username){
        User user = userRepository.findByUsername(username);
        User returnValue = new User(user.getId(), "", user.getEmail(), user.getId(), user.getDate());
        return returnValue;
    }

    @GetMapping(value="/testing")
    public ResponseEntity testToken(@RequestHeader("token") String token){
        // User user = userRepository.findByUsername(username);
        // User returnValue = new User(user.getId(), "", user.getEmail(), user.getId(), user.getDate());
        
        try {
            Claims claims = decodeJWT(token);
            // Claims claims = decodeJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhNDI2NTk3NS03MTkzLTRkNjQtYWYxOC1hNmVmNTJjZjgxMGYiLCJpYXQiOjE2MTYwOTM1NjEsInN1YiI6IkxvZ2luIFRva2VuIiwiaXNzIjoiRGVtbyIsInVzZXJuYW1lIjoiamFtZXMiLCJlbWFpbCI6ImphbWVzQGdtYWlsLmNvbSIsImlkIjoiY2FkMzM1NDItMGU4My00Y2M1LTljY2UtNzA5YzVhY2I1ZWU5IiwiZXhwIjoxNjE2MTI5NTYxfQ.opfOrDbyqEIEMcw8B1WVy-oTuibb_jePdtFmYZ_1frg");
            return new ResponseEntity<>(claims.get("username").toString(),HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token not valid");
        }

    //    Claims claims = Jwts.parser()
    //             .setSigningKey(DatatypeConverter.parseBase64Binary(key))
    //             .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhNDI2NTk3NS03MTkzLTRkNjQtYWYxOC1hNmVmNTJjZjgxMGYiLCJpYXQiOjE2MTYwOTM1NjEsInN1YiI6IkxvZ2luIFRva2VuIiwiaXNzIjoiRGVtbyIsInVzZXJuYW1lIjoiamFtZXMiLCJlbWFpbCI6ImphbWVzQGdtYWlsLmNvbSIsImlkIjoiY2FkMzM1NDItMGU4My00Y2M1LTljY2UtNzA5YzVhY2I1ZWU5IiwiZXhwIjoxNjE2MTI5NTYxfQ.opfOrDbyqEIEMcw8B1WVy-oTuibb_jePdtFmYZ_1frg").getBody();

    //     String val = claims.get("username").toString();


    
        
        //return "s";
    }

    @PostMapping(value="/createUser")
    @ResponseBody
    public ResponseEntity createUser(@RequestBody User user){
        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setDate(LocalDateTime.now());

        if(user.getEmail() == null || user.getEmail() == "" || user.getPassowrd() == null|| user.getPassowrd() == "" || user.getUsername() == null || user.getUsername() == ""){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fail to caraete user");
        }

        if(userRepository.findByUsername(user.getUsername()) != null ||  userRepository.findByEmail(user.getEmail()) != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fail to caraete user");
        }
     
        String encryptedString = AES.encrypt(user.getPassowrd(), secretKey) ;
        user.setPassword(encryptedString);
        User insertedUser = userRepository.insert(user);


        // return "User created";
        return new ResponseEntity<>("User created",HttpStatus.OK);

    }

    public Claims decodeJWT(String jwt){
        Claims claims = Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(key))
        .parseClaimsJws(jwt).getBody();
        return claims;
    }


   
    
}