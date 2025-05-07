package com.example.todo.todo_api.service;

import java.util.Optional;

import com.example.todo.todo_api.model.User;

public interface UserService {
    User save(User user);
    Optional<User> findByUsername(String username);


}
