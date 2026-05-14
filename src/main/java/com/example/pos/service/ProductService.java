package com.example.pos.service;

import com.example.pos.dto.request.ProductRequest;
import com.example.pos.dto.response.ProductResponse;
import com.example.pos.entity.Category;
import com.example.pos.entity.Product;
import com.example.pos.exception.ResourceNotFoundException;
import com.example.pos.repository.CategoryRepository;
import com.example.pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToResponse(product);
    }

    public ProductResponse getProductByBarcode(String barcode) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with barcode: " + barcode));
        return convertToResponse(product);
    }

    public List<ProductResponse> searchProducts(String searchTerm) {
        return productRepository.findBySearchTerm(searchTerm).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getLowStockProducts(Integer threshold) {
        return productRepository.findByStockLessThanEqual(threshold).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByBarcode(request.getBarcode())) {
            throw new IllegalArgumentException("Product with barcode already exists: " + request.getBarcode());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setReceivedPrice(request.getReceivedPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setStock(request.getStock());
        product.setBarcode(request.getBarcode());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Check if barcode is being changed and if it already exists
        if (!product.getBarcode().equals(request.getBarcode()) &&
                productRepository.existsByBarcode(request.getBarcode())) {
            throw new IllegalArgumentException("Product with barcode already exists: " + request.getBarcode());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setReceivedPrice(request.getReceivedPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setStock(request.getStock());
        product.setBarcode(request.getBarcode());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public void updateStock(Long productId, Integer quantity, boolean isIncrease) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        int newStock = isIncrease ? product.getStock() + quantity : product.getStock() - quantity;
        if (newStock < 0) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
        }

        product.setStock(newStock);
        productRepository.save(product);
    }

    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setReceivedPrice(product.getReceivedPrice());
        response.setSellingPrice(product.getSellingPrice());
        response.setStock(product.getStock());
        response.setBarcode(product.getBarcode());
        response.setImageUrl(product.getImageUrl());
        response.setCategory(product.getCategory() != null ? product.getCategory().getName() : null);
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}