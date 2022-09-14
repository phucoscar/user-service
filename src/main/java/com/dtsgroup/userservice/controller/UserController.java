package com.dtsgroup.userservice.controller;

import com.dtsgroup.userservice.entity.User;
import com.dtsgroup.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController  {
    private static final String CURRENT_URL = "src\\main\\resources\\static\\images";
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestParam String name,
                                           @RequestParam String password,
                                           @RequestParam String email,
                                           @RequestParam MultipartFile image) throws IOException {
        String fileLocation = new File( CURRENT_URL).getAbsolutePath() + "\\" + image.getOriginalFilename();
        OutputStream out = new FileOutputStream(fileLocation);
        out.write(image.getBytes());
        out.close();
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setImage(image.getOriginalFilename());
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateUser (@PathVariable String id,
                                              @RequestParam String name,
                                              @RequestParam String password,
                                              @RequestParam String email,
                                              @RequestBody MultipartFile image) throws IOException {
       Optional<User> optionalUser = userService.findById(id);
       if (!optionalUser.isPresent()) {
           return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
       }
       String fileLocation = new File(CURRENT_URL).getAbsolutePath() + "\\" + image.getOriginalFilename();
       OutputStream out = new FileOutputStream(fileLocation);
       out.write(image.getBytes());
       out.close();
       User updateUser = optionalUser.get();
       updateUser.setName(name);
       updateUser.setEmail(email);
       updateUser.setPassword(password);
       updateUser.setImage(image.getOriginalFilename());
       userService.save(updateUser);
       return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        Optional<User> optionalUser = userService.findById(id);
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        userService.delete(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
