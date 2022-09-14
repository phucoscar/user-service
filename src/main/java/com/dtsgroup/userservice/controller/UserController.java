package com.dtsgroup.userservice.controller;

import com.dtsgroup.userservice.entity.User;
import com.dtsgroup.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController  {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser (@PathVariable String id, @RequestBody User user) {
       Optional<User> optionalUser = userService.findById(id);
       if (!optionalUser.isPresent()) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
        User updateUser = optionalUser.get();
        updateUser.setName(user.getName());
        updateUser.setEmail(user.getEmail());
        updateUser.setImage(user.getImage());
        userService.save(updateUser);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) {
        Optional<User> optionalUser = userService.findById(id);
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        userService.delete(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
