package com.example.pos.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {
    @NotBlank
    private String name;

    public CategoryRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
