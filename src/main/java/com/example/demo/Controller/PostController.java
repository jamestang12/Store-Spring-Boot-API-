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
import javax.xml.bind.DatatypeConverter;

import com.example.demo.dao.PostRepository;
import com.example.demo.model.Post;
import com.example.demo.model.User;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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

    private static String uploadFolder = "/Users/jamestang/springBootImage/";

    @GetMapping(value="/postTesting")
    public String test(){
        return "testing";
    }

    @PostMapping(value="/uploadImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE  )
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile file, @RequestHeader("token") String token) throws IOException{
        if(file.isEmpty() || file == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File empty");
        }
        

        try {
            File convertFile = new File(uploadFolder + file.getOriginalFilename());
            convertFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(convertFile);
            fout.write(file.getBytes());
            fout.close();
            return ResponseEntity.ok(token);
        } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }


    @PostMapping(value="/createPost")
    @ResponseBody
    public ResponseEntity createPost(@RequestHeader("token") String token, @RequestBody Post post){
        if(post.getPrice() == null || post.getTitle() == null || post.getTitle() == "" ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fail to create post");
        }
        
        Claims claims;
        try {
            claims = decodeJWT(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
        }
        post.setUsername(claims.get("username").toString());
        String id = UUID.randomUUID().toString();
        LocalDateTime date = LocalDateTime.now();
        post.setDate(date);
        post.setPostId(id);

        Post insertedPost = postRepository.insert(post);
        return ResponseEntity.ok(insertedPost);

    }


    @GetMapping(value="/post/{id}")
    public ResponseEntity findPostById(@PathVariable("id") String id){
        Post post = postRepository.findByPostid(id);
        
        return ResponseEntity.ok(post);
    }

    @GetMapping(value="/posts")
    public ResponseEntity findPostById(){
       // List<Post> post = postRepository.findall();
        
        return ResponseEntity.ok(postRepository.findAll());
    }
    


    public Claims decodeJWT(String jwt){
        Claims claims = Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(key))
        .parseClaimsJws(jwt).getBody();
        return claims;
    }


}