package com.example.demo.Controller;

import com.example.demo.dao.UserRepository;
import com.example.demo.model.User;
import com.example.demo.Encryption.AES;
import com.example.demo.model.User;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;




@RestController
public class AuthController{

    @Value("tokenKey")
    private String key;

    @Value("${secretKey}")
    private String secretKey;

    @Autowired
    public UserRepository userRepository;


    @PostMapping(value="/login")
    @ResponseBody
    public ResponseEntity login(@RequestBody User user){
        if(user.getEmail() == null || user.getEmail() == "" || user.getPassowrd() == null || user.getPassowrd() == ""){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fail to login");
        }

        String encryptedString = AES.encrypt(user.getPassowrd(), secretKey);
        User userValue = userRepository.findByEmailAndPassword(user.getEmail(), encryptedString);

        if(userValue == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fail to login");
        }


        String token = createJWT(36000000, userValue);

        return new ResponseEntity<>(token,HttpStatus.OK);
    }


    public String createJWT(long ttlMillis, User user){

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setSubject("Login Token")
                .setIssuer("Demo")
                .signWith(signatureAlgorithm, signingKey)
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("id", user.getId());
                
        if(ttlMillis >= 0){
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }
}