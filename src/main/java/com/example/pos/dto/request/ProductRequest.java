package com.example.pos.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal receivedPrice;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal sellingPrice;

    @Min(0)
    private Integer stock = 0;

    @NotBlank
    private String barcode;

    private String imageUrl;

    @NotNull
    private Long categoryId;

    public ProductRequest() {}

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getReceivedPrice() { return receivedPrice; }
    public void setReceivedPrice(BigDecimal receivedPrice) { this.receivedPrice = receivedPrice; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}