package com.dtsgroup.userservice.controller;

import com.dtsgroup.userservice.dto.UserDTO;
import com.dtsgroup.userservice.entity.User;
import com.dtsgroup.userservice.dto.UserRequest;
import com.dtsgroup.userservice.dto.UserResponse;
import com.dtsgroup.userservice.service.UserService;
import com.dtsgroup.userservice.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil util;

    @Autowired
    private AuthenticationManager authenticationManager;



    @GetMapping("/users")
    public List<User> findAll() {
        return userService.findAll();
    }

//    @GetMapping
//    public List<User> getAllUsers(@RequestParam(value = "page", defaultValue = "0") int page,
//                                  @RequestParam(value = "limit", defaultValue = "10") int limit) {
//        List<User> users = new ArrayList<>();
//        return null;
//    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getUsername(),
                            userRequest.getPassword()
                    )
            );
            User user = (User) authentication.getPrincipal();
            String token = util.genarateToken(user.getUsername());
            return ResponseEntity.ok(new UserResponse(token));
        } catch (BadCredentialsException e ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<User> createUser(@RequestPart UserDTO userDTO,
                                           @RequestPart MultipartFile image) throws IOException {
        return new ResponseEntity<>(userService.save(userDTO, image), HttpStatus.CREATED);
    }

    @PutMapping(value = "update/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> updateUser(@PathVariable String id,
                                             @RequestPart UserDTO userDTO,
                                             @RequestPart MultipartFile image) throws IOException {
        User user = userService.update(id, userDTO, image);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        userService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
