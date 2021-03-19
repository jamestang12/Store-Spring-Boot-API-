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

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.example.demo.dao.OfferRepository;
import com.example.demo.dao.PostRepository;
import com.example.demo.model.Offer;
import com.example.demo.model.Post;

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
public class OfferController{

    @Value("tokenKey")
    private String key;

    @Autowired
    public OfferRepository offerRepository;

    @Autowired
    public PostRepository postRepository;

    @PostMapping(value="/makeOffer")
    @ResponseBody
    public ResponseEntity makeOffer(@RequestHeader("token") String token, @RequestBody Offer offer){

    

        if(offer.getSender() == "" || offer.getSender() == null || offer.getOwner() == "" || offer.getOwner() == null || offer.getText() == null || offer.getText() == ""){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request fail");
        }

        Optional<Post> post = postRepository.findById(offer.getPostid());
        
        if(! post.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
        Post _post = post.get();

        Claims claims;
        try {
            claims = decodeJWT(token);
            if(_post.getUsername().equals(claims.get("username").toString())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request fail");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
        }
        List<Offer> offers = offerRepository.findBySender(claims.get("username").toString());
        if(offers != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request fail");
        }

        String id = UUID.randomUUID().toString();
        offer.setId(id);
        offer.setdate(LocalDateTime.now());

        
        return ResponseEntity.ok(offerRepository.insert(offer));
    }

    @GetMapping(value="/offer/{id}")
    public ResponseEntity findOfferById(@PathVariable("id") String id,@RequestHeader("token") String token){
        Optional<Offer> offer = offerRepository.findById(id);
        if(!offer.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offer not found");
        }

        Offer _offer = offer.get();

        Claims claims;
        try {
            claims = decodeJWT(token);
            
                if( _offer.getOwner().equals(claims.get("username").toString()) || _offer.getSender().equals(claims.get("username").toString())){
                    
                    return ResponseEntity.ok(_offer);
                }else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
                }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
        }


    }

    @GetMapping(value="/offer/postid/{id}")
    public ResponseEntity findOfferBypostId(@PathVariable("id") String id,@RequestHeader("token") String token){
        List<Offer> offer = offerRepository.findByPostid(id);
        if(offer == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offer not found");
        }

        Claims claims;
        try {
            claims = decodeJWT(token);
            
                if( offer.get(0).getOwner().equals(claims.get("username").toString()) ){
                    
                    return ResponseEntity.ok(offer);
                }else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
                }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
        }
    }

    @GetMapping(value="/offer/owner")
    public ResponseEntity findOfferByOwner(@RequestHeader("token") String token){
        
        Claims claims;
        try {
            claims = decodeJWT(token);
                List<Offer> offer = offerRepository.findByOwner(claims.get("username").toString());
                if(offer == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offer not found");
                }
                return ResponseEntity.ok(offer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
        }
    }


    @GetMapping(value="/offer/sender")
    public ResponseEntity findOfferBySender(@RequestHeader("token") String token){
        
        Claims claims;
        try {
            claims = decodeJWT(token);
                List<Offer> offer = offerRepository.findBySender(claims.get("username").toString());
                if(offer == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offer not found");
                }
                return ResponseEntity.ok(offer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
        }
    }

    @DeleteMapping("/offer/{id}")
    public ResponseEntity deleteOffer(@RequestHeader("token") String token, @PathVariable("id") String id){
        Optional<Offer> offer = offerRepository.findById(id);
        Offer _offer;
        if(offer.isPresent()){
            _offer = offer.get();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offer not found");
        }
        Claims claims;

        try {
            claims = decodeJWT(token);
            if(! _offer.getSender().equals(claims.get("username").toString())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
            }
            offerRepository.deleteById(id);
            return ResponseEntity.ok("Offer have been removed");

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