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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    private static String uploadFolder = "/Users/jamestang/springBootImage/";

    @GetMapping(value="/postTesting")
    public String test(){
        return "testing";
    }

    @PostMapping(value="/postImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile file) throws IOException{
        if(file.isEmpty() || file == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File empty");
        }

        try {
            File convertFile = new File(uploadFolder + file.getOriginalFilename());
            convertFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(convertFile);
            fout.write(file.getBytes());
            fout.close();
            return ResponseEntity.ok(uploadFolder + file.getOriginalFilename());

            // byte[] bytes = file.getBytes();
            // Path path = Paths.get(uploadFolder + file.getOriginalFilename());
            // Files.write(path, bytes);
            // return ResponseEntity.ok("File uploaded successfully.");
        } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
  

        // String fileName = file.getOriginalFilename();
        // try {
        //     file.transferTo(new File("file:///Users/" + fileName));
        // } catch (Exception e) {
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        // }

    }
}