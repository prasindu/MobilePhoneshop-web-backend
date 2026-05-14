package com.example.pos.controller;

import com.example.pos.dto.request.ProductRequest;
import com.example.pos.dto.response.ProductResponse;
import com.example.pos.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")

public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer lowStock) {

        List<ProductResponse> products;

        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search);
        } else if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId);
        } else if (lowStock != null) {
            products = productService.getLowStockProducts(lowStock);
        } else {
            products = productService.getAllProducts();
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<ProductResponse> getProductByBarcode(@PathVariable String barcode) {
        ProductResponse product = productService.getProductByBarcode(barcode);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse product = productService.createProduct(request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse product = productService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(
            @PathVariable Long id,
            @RequestParam("quantity") int quantity,
            @RequestParam("increase") boolean increase
    ) {
        productService.updateStock(id, quantity, increase);
        return ResponseEntity.ok().build();
    }
}
