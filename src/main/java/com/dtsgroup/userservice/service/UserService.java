package com.dtsgroup.userservice.service;

import com.dtsgroup.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> findAll();

    public User save(User user);

    public Optional<User> findById(String id);

    public void delete(User user);
}
