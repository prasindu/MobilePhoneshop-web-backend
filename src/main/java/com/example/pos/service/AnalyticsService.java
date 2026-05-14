package com.example.pos.service;

import com.example.pos.dto.response.AnalyticsResponse;
import com.example.pos.repository.ProductRepository;
import com.example.pos.repository.SaleRepository;
import com.example.pos.repository.SaleItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    public AnalyticsResponse getAnalytics(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        // Today's statistics
        Double todaysSalesAmount = saleRepository.getTotalSalesByDate(today);
        Double todaysProfitAmount = saleRepository.getTotalProfitByDate(today);
        Long todaysTransactions = saleRepository.getTransactionCountByDate(today);

        // General statistics
        Long totalProducts = productRepository.count();
        Long lowStockItems = (long) productRepository.findByStockLessThanEqual(5).size();

        // Daily sales data for the specified range
        List<AnalyticsResponse.DailySales> dailySalesData = getDailySalesData(startDate, endDate);

        // Category sales data
        List<AnalyticsResponse.CategorySales> categorySalesData = getCategorySalesData();

        AnalyticsResponse response = new AnalyticsResponse();
        response.setTodaysSales(todaysSalesAmount != null ? BigDecimal.valueOf(todaysSalesAmount) : BigDecimal.ZERO);
        response.setTodaysProfit(todaysProfitAmount != null ? BigDecimal.valueOf(todaysProfitAmount) : BigDecimal.ZERO);
        response.setTodaysTransactions(todaysTransactions != null ? todaysTransactions : 0L);
        response.setTotalProducts(totalProducts);
        response.setLowStockItems(lowStockItems);
        response.setDailySalesData(dailySalesData);
        response.setCategorySalesData(categorySalesData);

        return response;
    }

    private List<AnalyticsResponse.DailySales> getDailySalesData(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(6); // Last 7 days
        }

        return startDate.datesUntil(endDate.plusDays(1))
                .map(date -> {
                    Double sales = saleRepository.getTotalSalesByDate(date);
                    Double profit = saleRepository.getTotalProfitByDate(date);
                    Long transactions = saleRepository.getTransactionCountByDate(date);

                    return new AnalyticsResponse.DailySales(
                            date,
                            sales != null ? BigDecimal.valueOf(sales) : BigDecimal.ZERO,
                            profit != null ? BigDecimal.valueOf(profit) : BigDecimal.ZERO,
                            transactions != null ? transactions : 0L
                    );
                })
                .collect(Collectors.toList());
    }

    private List<AnalyticsResponse.CategorySales> getCategorySalesData() {
        List<Object[]> results = saleItemRepository.getCategorySalesData();

        return results.stream()
                .map(result -> new AnalyticsResponse.CategorySales(
                        (String) result[0], // category name
                        (BigDecimal) result[1], // revenue
                        ((Number) result[2]).longValue() // quantity
                ))
                .collect(Collectors.toList());
    }
}
