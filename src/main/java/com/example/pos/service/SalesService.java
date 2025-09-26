package com.example.pos.service;

import com.example.pos.dto.request.SaleRequest;
import com.example.pos.dto.response.AnalyticsResponse;
import com.example.pos.dto.response.SaleResponse;
import com.example.pos.entity.Product;
import com.example.pos.entity.Sale;
import com.example.pos.entity.SaleItem;
import com.example.pos.entity.User;
import com.example.pos.exception.ResourceNotFoundException;
import com.example.pos.repository.ProductRepository;
import com.example.pos.repository.SaleRepository;
import com.example.pos.repository.SaleItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SalesService {
    private static final Logger logger = LoggerFactory.getLogger(SalesService.class);

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private AuthService authService;

    public List<SaleResponse> getSalesByDate(LocalDate date) {
        try {
            List<Sale> sales = saleRepository.findBySaleDate(date);
            return sales.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching sales for date {}: {}", date, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<SaleResponse> getAllSales() {
        try {
            return saleRepository.findAll().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching all sales: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public SaleResponse getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        return convertToResponse(sale);
    }

    public SaleResponse getSaleByInvoiceId(String invoiceId) {
        Sale sale = saleRepository.findByInvoiceId(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with invoice ID: " + invoiceId));
        return convertToResponse(sale);
    }

    public List<SaleResponse> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            return saleRepository.findBySaleDateBetween(startDate, endDate).stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching sales for date range {} to {}: {}", startDate, endDate, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private List<AnalyticsResponse.DailySales> getDailySalesData(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(6); // Last 7 days
        }

        List<AnalyticsResponse.DailySales> result = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            try {
                Double sales = saleRepository.getTotalSalesByDate(date);
                Double profit = saleRepository.getTotalProfitByDate(date);
                Long transactions = saleRepository.getTransactionCountByDate(date);

                result.add(new AnalyticsResponse.DailySales(
                        date,
                        sales != null ? BigDecimal.valueOf(sales) : BigDecimal.ZERO,
                        profit != null ? BigDecimal.valueOf(profit) : BigDecimal.ZERO,
                        transactions != null ? transactions : 0L
                ));
            } catch (Exception e) {
                logger.error("Error processing daily sales data for date {}: {}", date, e.getMessage());
                // Add zero values for this date to maintain continuity
                result.add(new AnalyticsResponse.DailySales(
                        date,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        0L
                ));
            }
        }

        return result;
    }

    public List<SaleResponse> searchSales(String searchTerm) {
        try {
            return saleRepository.findBySearchTerm(searchTerm).stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error searching sales with term '{}': {}", searchTerm, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public SaleResponse createSale(SaleRequest request) {
        User currentUser = authService.getCurrentUser();

        // Validate stock availability for non-custom items
        for (SaleRequest.SaleItemRequest itemRequest : request.getItems()) {
            if (!itemRequest.getIsCustom() && itemRequest.getProductId() != null) {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemRequest.getProductId()));

                if (product.getStock() < itemRequest.getQuantity()) {
                    throw new IllegalArgumentException("Insufficient stock for product: " + product.getName() +
                            ". Available: " + product.getStock() + ", Required: " + itemRequest.getQuantity());
                }
            }
        }

        // Create sale
        Sale sale = new Sale();
        sale.setInvoiceId(request.getInvoiceId());
        sale.setSaleDate(LocalDate.now());
        // Truncate to seconds to avoid nanosecond precision issues
        sale.setSaleTime(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
        sale.setTotal(request.getTotal());
        sale.setProfit(request.getProfit());
        sale.setDiscount(request.getDiscount());
        sale.setDiscountType(Sale.DiscountType.valueOf(request.getDiscountType()));
        sale.setCustomerName(request.getCustomerName());
        sale.setCustomerPhone(request.getCustomerPhone());
        sale.setCustomerEmail(request.getCustomerEmail());
        sale.setCustomerAddress(request.getCustomerAddress());
        sale.setNotes(request.getNotes());
        sale.setCashier(currentUser);

        Sale savedSale = saleRepository.save(sale);

        // Create sale items and update stock
        for (SaleRequest.SaleItemRequest itemRequest : request.getItems()) {
            SaleItem saleItem = new SaleItem();
            saleItem.setSale(savedSale);
            saleItem.setProductName(itemRequest.getProductName());
            saleItem.setQuantity(itemRequest.getQuantity());
            saleItem.setUnitPrice(itemRequest.getUnitPrice());
            saleItem.setDiscount(itemRequest.getDiscount());
            saleItem.setDiscountType(Sale.DiscountType.valueOf(itemRequest.getDiscountType()));
            saleItem.setIsCustom(itemRequest.getIsCustom());

            if (!itemRequest.getIsCustom() && itemRequest.getProductId() != null) {
                Product product = productRepository.findById(itemRequest.getProductId()).get();
                saleItem.setProduct(product);

                // Update stock
                productService.updateStock(itemRequest.getProductId(), itemRequest.getQuantity(), false);
            }

            saleItemRepository.save(saleItem);
        }

        return convertToResponse(savedSale);
    }

    public SaleResponse processReturn(String originalInvoiceId, List<SaleRequest.SaleItemRequest> returnItems, String returnReason) {
        Sale originalSale = saleRepository.findByInvoiceId(originalInvoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Original sale not found with invoice ID: " + originalInvoiceId));

        User currentUser = authService.getCurrentUser();

        // Calculate return totals
        double returnTotal = returnItems.stream()
                .mapToDouble(item -> item.getUnitPrice().doubleValue() * item.getQuantity())
                .sum();

        // Create return sale
        Sale returnSale = new Sale();
        returnSale.setInvoiceId("RTN-" + System.currentTimeMillis());
        returnSale.setSaleDate(LocalDate.now());
        // Truncate to seconds to avoid nanosecond precision issues
        returnSale.setSaleTime(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
        returnSale.setTotal(java.math.BigDecimal.valueOf(-returnTotal));
        returnSale.setProfit(java.math.BigDecimal.valueOf(0)); // Calculate actual return profit
        returnSale.setDiscount(java.math.BigDecimal.ZERO);
        returnSale.setCustomerName(originalSale.getCustomerName());
        returnSale.setCustomerPhone(originalSale.getCustomerPhone());
        returnSale.setCustomerEmail(originalSale.getCustomerEmail());
        returnSale.setCustomerAddress(originalSale.getCustomerAddress());
        returnSale.setNotes("Return for invoice: " + originalInvoiceId);
        returnSale.setCashier(currentUser);
        returnSale.setIsReturn(true);
        returnSale.setReturnReason(returnReason);

        Sale savedReturnSale = saleRepository.save(returnSale);

        // Create return items and restock
        for (SaleRequest.SaleItemRequest itemRequest : returnItems) {
            SaleItem returnItem = new SaleItem();
            returnItem.setSale(savedReturnSale);
            returnItem.setProductName(itemRequest.getProductName());
            returnItem.setQuantity(itemRequest.getQuantity());
            returnItem.setUnitPrice(itemRequest.getUnitPrice());
            returnItem.setIsCustom(itemRequest.getIsCustom());

            if (!itemRequest.getIsCustom() && itemRequest.getProductId() != null) {
                Product product = productRepository.findById(itemRequest.getProductId()).get();
                returnItem.setProduct(product);

                // Restock
                productService.updateStock(itemRequest.getProductId(), itemRequest.getQuantity(), true);
            }

            saleItemRepository.save(returnItem);
        }

        return convertToResponse(savedReturnSale);
    }

    private SaleResponse convertToResponse(Sale sale) {
        try {
            SaleResponse response = new SaleResponse();
            response.setId(sale.getId());
            response.setInvoiceId(sale.getInvoiceId());
            response.setSaleDate(sale.getSaleDate());

            // Handle potential time parsing issues
            try {
                response.setSaleTime(sale.getSaleTime());
            } catch (Exception e) {
                logger.warn("Error parsing sale time for sale ID {}: {}. Using current time.", sale.getId(), e.getMessage());
                response.setSaleTime(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
            }

            response.setTotal(sale.getTotal());
            response.setProfit(sale.getProfit());
            response.setDiscount(sale.getDiscount());
            response.setDiscountType(sale.getDiscountType() != null ? sale.getDiscountType().toString() : "PERCENTAGE");
            response.setCustomerName(sale.getCustomerName());
            response.setCustomerPhone(sale.getCustomerPhone());
            response.setCustomerEmail(sale.getCustomerEmail());
            response.setCustomerAddress(sale.getCustomerAddress());
            response.setNotes(sale.getNotes());
            response.setCashierName(sale.getCashier() != null ? sale.getCashier().getName() : null);
            response.setIsReturn(sale.getIsReturn() != null ? sale.getIsReturn() : false);
            response.setReturnReason(sale.getReturnReason());

            List<SaleItem> saleItems = sale.getSaleItems();
            List<SaleResponse.SaleItemResponse> itemResponses = Collections.emptyList();

            // Convert sale items
            if (saleItems != null && !saleItems.isEmpty()) {
                itemResponses = saleItems.stream()
                        .map(this::convertItemToResponse)
                        .collect(Collectors.toList());
            }

            response.setItems(itemResponses);
            return response;
        } catch (Exception e) {
            logger.error("Error converting sale to response for sale ID {}: {}", sale.getId(), e.getMessage(), e);
            // Return a minimal response to avoid complete failure
            SaleResponse errorResponse = new SaleResponse();
            errorResponse.setId(sale.getId());
            errorResponse.setInvoiceId(sale.getInvoiceId() != null ? sale.getInvoiceId() : "ERROR");
            errorResponse.setSaleDate(sale.getSaleDate() != null ? sale.getSaleDate() : LocalDate.now());
            errorResponse.setSaleTime(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
            errorResponse.setTotal(sale.getTotal() != null ? sale.getTotal() : BigDecimal.ZERO);
            errorResponse.setProfit(sale.getProfit() != null ? sale.getProfit() : BigDecimal.ZERO);
            errorResponse.setItems(Collections.emptyList());
            return errorResponse;
        }
    }

    private SaleResponse.SaleItemResponse convertItemToResponse(SaleItem item) {
        try {
            SaleResponse.SaleItemResponse response = new SaleResponse.SaleItemResponse();
            response.setId(item.getId());
            response.setProductId(item.getProduct() != null ? item.getProduct().getId() : null);
            response.setProductName(item.getProductName());
            response.setQuantity(item.getQuantity());
            response.setUnitPrice(item.getUnitPrice());
            response.setDiscount(item.getDiscount());
            response.setDiscountType(item.getDiscountType() != null ?
                    item.getDiscountType().toString() : "PERCENTAGE");
            response.setIsCustom(item.getIsCustom() != null ? item.getIsCustom() : false);
            return response;
        } catch (Exception e) {
            logger.error("Error converting sale item to response for item ID {}: {}", item.getId(), e.getMessage());
            // Return minimal response
            SaleResponse.SaleItemResponse errorResponse = new SaleResponse.SaleItemResponse();
            errorResponse.setId(item.getId());
            errorResponse.setProductName(item.getProductName() != null ? item.getProductName() : "Error Loading Item");
            errorResponse.setQuantity(item.getQuantity() != null ? item.getQuantity() : 0);
            errorResponse.setUnitPrice(item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO);
            errorResponse.setIsCustom(false);
            return errorResponse;
        }
    }
}