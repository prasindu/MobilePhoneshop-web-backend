package com.example.pos.service;

import com.example.pos.dto.request.AddUserRequest;
import com.example.pos.dto.response.AddUserResponse;
import com.example.pos.entity.User;
import com.example.pos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AddUserResponse addUser(AddUserRequest request) {
        // Check if username already exists
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Validate role
        User.Role role;
        try {
            role = User.Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role. Must be MANAGER or CASHIER");
        }

        // Create new user
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Encode password
        newUser.setName(request.getName());
        newUser.setRole(role);

        // Save user
        User savedUser = userRepository.save(newUser);

        // Return response
        return new AddUserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getName(),
                savedUser.getRole().toString(),
                "User created successfully"
        );
    }
}