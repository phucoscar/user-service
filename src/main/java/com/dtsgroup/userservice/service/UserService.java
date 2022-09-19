package com.dtsgroup.userservice.service;

import com.dtsgroup.userservice.dto.UserDTO;
import com.dtsgroup.userservice.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> findAll();

    public User save(UserDTO user, MultipartFile image) throws IOException;

    public Optional<User> findById(String id);

    public void delete(String id);

    public User update(String id, UserDTO userDTO, MultipartFile image) throws IOException;

    Optional<User> findByUsername(String username);
}
