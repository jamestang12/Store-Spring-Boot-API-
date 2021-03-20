package com.example.demo.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.text.html.Option;
import javax.xml.bind.DatatypeConverter;

import com.example.demo.Encryption.AES;
import com.example.demo.dao.OfferContextRepository;
import com.example.demo.dao.OfferRepository;
import com.example.demo.dao.PostRepository;
import com.example.demo.model.Offer;
import com.example.demo.model.OfferContext;
import com.example.demo.model.Post;
import com.example.demo.model.User;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.jsonwebtoken.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;



@RestController
public class OfferContextController{

    @Value("tokenKey")
    private String key;

    @Autowired
    public OfferContextRepository offerContextRepository;

    @Autowired
    public OfferRepository offerRepository;
    
    
    //@route   POST /sendMessage
    //@desc    Send message btw user with jwt token and text context
    //@access  Private
    @PostMapping(value="/sendMessage")
    @ResponseBody
    public ResponseEntity sendMessage(@RequestHeader("token") String token, @RequestBody OfferContext offerContext){
        
        Optional<Offer> offer = offerRepository.findById(offerContext.getPostid());
        Offer _offer ;
        if(offer.isPresent()){
            _offer = offer.get();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offer not found");
        }

        Claims claims;
        try {
            claims = decodeJWT(token);
            if(! _offer.getSender().equals(claims.get("username").toString()) && ! _offer.getOwner().equals(claims.get("username").toString())){
              
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request fail");


            }else{
                offerContext.setId(UUID.randomUUID().toString());
                offerContext.setDate(LocalDateTime.now());
                offerContext.setSenderid(claims.get("username").toString());
                if( _offer.getOwner().equals(claims.get("username").toString())){
                    offerContext.setReceverid(_offer.getSender());
                }else{
                    offerContext.setReceverid(_offer.getOwner());
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
        }


        return ResponseEntity.ok(offerContextRepository.insert(offerContext));
    }
    


    //@route   GET /offer/messages/{id}
    //@desc    Get offer messages by offer id with user token
    //@access  Private
    @GetMapping(value="/offer/messages/{id}")
    public ResponseEntity getOfferMessages(@PathVariable("id") String id,@RequestHeader("token") String token){
        Claims claims;
        try {
            claims = decodeJWT(token);
            List<OfferContext> offerMessages = offerContextRepository.findByPostid(id);
            if(offerMessages.size() == 0){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Context not found");    
            }
            if(! offerMessages.get(0).getReceverid().equals(claims.get("username").toString()) && ! offerMessages.get(0).getSenderid().equals(claims.get("username").toString())){
              
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request fail");

            }else{
                return ResponseEntity.ok(offerMessages);
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
        }

    }


    public Claims decodeJWT(String jwt){
        Claims claims = Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(key))
        .parseClaimsJws(jwt).getBody();
        return claims;
    }
}