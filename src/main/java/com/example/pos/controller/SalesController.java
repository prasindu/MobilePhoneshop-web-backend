package com.example.pos.controller;

import com.example.pos.dto.request.SaleRequest;
import com.example.pos.dto.response.SaleResponse;
import com.example.pos.service.SalesService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController {
    private static final Logger logger = LoggerFactory.getLogger(SalesController.class);

    @Autowired
    private SalesService salesService;

    @GetMapping
    public ResponseEntity<List<SaleResponse>> getAllSales(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        try {
            List<SaleResponse> sales;

            if (search != null && !search.trim().isEmpty()) {
                sales = salesService.searchSales(search.trim());
            } else if (date != null) {
                sales = salesService.getSalesByDate(date);
            } else if (startDate != null && endDate != null) {
                sales = salesService.getSalesByDateRange(startDate, endDate);
            } else {
                // Return all sales when no filters are applied
                sales = salesService.getAllSales();
            }

            // Return the sales list (could be empty but not null)
            return ResponseEntity.ok(sales != null ? sales : Collections.emptyList());

        } catch (Exception e) {
            logger.error("Error fetching sales with params - search: {}, date: {}, startDate: {}, endDate: {}. Error: {}",
                    search, date, startDate, endDate, e.getMessage(), e);

            // Return empty list instead of error to prevent frontend crash
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getSaleById(@PathVariable Long id) {
        try {
            SaleResponse sale = salesService.getSaleById(id);
            return ResponseEntity.ok(sale);
        } catch (Exception e) {
            logger.error("Error fetching sale with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<SaleResponse> getSaleByInvoiceId(@PathVariable String invoiceId) {
        try {
            SaleResponse sale = salesService.getSaleByInvoiceId(invoiceId);
            return ResponseEntity.ok(sale);
        } catch (Exception e) {
            logger.error("Error fetching sale with invoice ID {}: {}", invoiceId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody SaleRequest request) {
        try {
            SaleResponse sale = salesService.createSale(request);
            return new ResponseEntity<>(sale, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating sale: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/return")
    public ResponseEntity<SaleResponse> processReturn(
            @RequestParam String originalInvoiceId,
            @RequestParam String returnReason,
            @RequestBody List<SaleRequest.SaleItemRequest> returnItems) {
        try {
            SaleResponse returnSale = salesService.processReturn(originalInvoiceId, returnItems, returnReason);
            return new ResponseEntity<>(returnSale, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error processing return for invoice {}: {}", originalInvoiceId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/today")
    public ResponseEntity<List<SaleResponse>> getTodaysSales() {
        try {
            List<SaleResponse> sales = salesService.getSalesByDate(LocalDate.now());
            return ResponseEntity.ok(sales != null ? sales : Collections.emptyList());
        } catch (Exception e) {
            logger.error("Error fetching today's sales: {}", e.getMessage(), e);
            return ResponseEntity.ok(Collections.emptyList());
        }
    }
}