package com.example.pos.dto.response;

public class AddUserResponse {
    private Long id;
    private String username;
    private String name;
    private String role;
    private String message;

    // Constructors
    public AddUserResponse() {}

    public AddUserResponse(Long id, String username, String name, String role, String message) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
        this.message = message;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}