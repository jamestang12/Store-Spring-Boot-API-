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

import com.example.demo.dao.PostRepository;
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
        post.setid(id);

        Post insertedPost = postRepository.insert(post);
        return ResponseEntity.ok(insertedPost);

    }


    @GetMapping(value="/post/{id}")
    public ResponseEntity findPostById(@PathVariable("id") String id){
        // Post post = postRepository.findById(id);
        Optional<Post> post = postRepository.findById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping(value="/posts")
    public ResponseEntity findPostById(){
       // List<Post> post = postRepository.findall();
        
        return ResponseEntity.ok(postRepository.findAll());
    }

    @PutMapping(value="/post/bid/{id}")
    public ResponseEntity bidItem(@RequestHeader("token") String token,@RequestHeader String bid, @PathVariable("id") String id){
        Claims claims;
        try {
            claims = decodeJWT(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
        }

        Optional<Post> post = postRepository.findById(id);
        Post _post;
        if(post.isPresent()){
             _post = post.get();
            if(_post.getUsername().equals(claims.get("username").toString()) || _post.getBud() >= Double.parseDouble(bid) ||_post.getPrice() >= Double.parseDouble(bid)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request fail");
            }
             
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not authorized");
        }
        _post.setBud(Double.parseDouble(bid));     
        _post.setBudPerson(claims.get("username").toString());   

        postRepository.save(_post);

        return ResponseEntity.ok(_post);
    }

    @PutMapping(value="/post/uploadImage/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE  )
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile file, @RequestHeader("token") String token, @PathVariable("id") String id) throws IOException{
        if(file.isEmpty() || file == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File empty");
        }

        Optional<Post> post = postRepository.findById(id);
        Post _post;
        if(post.isPresent()){
            _post = post.get();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
        
        Claims claims;
        try {
            claims = decodeJWT(token);
            if(! _post.getUsername().equals(claims.get("username").toString())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
        }

        try {
            File convertFile = new File(uploadFolder + id + "_" +file.getOriginalFilename());
            convertFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(convertFile);
            fout.write(file.getBytes());
            fout.close();

            if(_post.getImages() == null){
                ArrayList<String> list=new ArrayList<String>();  
                list.add(uploadFolder + id + "_" +file.getOriginalFilename());
                _post.setImages(list);
                postRepository.save(_post);
            }else{
                ArrayList<String> list = _post.getImages();
                list.add(uploadFolder + id + "_" +file.getOriginalFilename());
                _post.setImages(list);
                postRepository.save(_post);
            }

            return ResponseEntity.ok(_post);
        } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }


    @PutMapping(value="/post/info/{id}")
    @ResponseBody
    public ResponseEntity updatePostInfo(@RequestHeader("token") String token, @PathVariable("id") String id, @RequestBody Post postInfo){
        Claims claims;
        try {
            claims = decodeJWT(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
        }


        Optional<Post> post = postRepository.findById(id);
        Post _post;
        if(post.isPresent()){
             _post = post.get();  
             if(! _post.getUsername().equals(claims.get("username").toString())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
             }
             if(postInfo.getTitle() != null && postInfo.getTitle() != ""){
                _post.setTitle(postInfo.getTitle());
             }
             if(postInfo.getPrice() != null && postInfo.getPrice() >= 0){
                 _post.setPrice(postInfo.getPrice());
             }
             _post.setLocation(postInfo.getLocation());
             _post.setDesc(postInfo.getDesc());

             postRepository.save(_post);

        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
       

        return ResponseEntity.ok(_post);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity deledePost(@RequestHeader("token") String token, @PathVariable("id") String id){
        Claims claims;

        Optional<Post> post = postRepository.findById(id);
        Post _post;
        if(post.isPresent()){
            _post = post.get();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
        try {
            claims = decodeJWT(token);
            if(! _post.getUsername().equals(claims.get("username").toString())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not authorized");
            }
            postRepository.deleteById(id);
            return ResponseEntity.ok("Post have been removed");

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