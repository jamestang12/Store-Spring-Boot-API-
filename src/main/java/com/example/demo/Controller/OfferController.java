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
import javax.swing.text.html.Option;
import javax.xml.bind.DatatypeConverter;

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

    @PostMapping(value="/makeOffer")
    @ResponseBody
    public ResponseEntity makeOffer(@RequestHeader("token") String token){

        Claims claims;
        try {
            claims = decodeJWT(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
        }


        return ResponseEntity.ok("testing");
    }

    public Claims decodeJWT(String jwt){
        Claims claims = Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(key))
        .parseClaimsJws(jwt).getBody();
        return claims;
    }

}