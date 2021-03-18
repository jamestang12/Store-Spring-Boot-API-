package com.example.demo.Controller;

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

import com.example.demo.dao.PostRepository;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

@RestController
public class PostController{

    
    @Value("tokenKey")
    private String key;

    @Autowired
    public PostRepository postRepository;

    @GetMapping(value="/postTesting")
    public String test(){
        return "testing";
    }
}