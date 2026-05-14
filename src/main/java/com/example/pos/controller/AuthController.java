package com.example.pos.controller;

import com.example.pos.dto.request.AddUserRequest;
import com.example.pos.dto.request.LoginRequest;
import com.example.pos.dto.response.AddUserResponse;
import com.example.pos.dto.response.LoginResponse;
import com.example.pos.service.AuthService;
import com.example.pos.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // JWT is stateless, so logout is handled on the client side
        return ResponseEntity.ok("Logged out successfully");
    }


    @PostMapping("/add-user")
    public ResponseEntity<AddUserResponse> addUser(@Valid @RequestBody AddUserRequest addUserRequest) {
        try {
            AddUserResponse response = userService.addUser(addUserRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            // Return error response
            AddUserResponse errorResponse = new AddUserResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}