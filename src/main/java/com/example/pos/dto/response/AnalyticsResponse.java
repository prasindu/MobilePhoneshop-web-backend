package com.example.pos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AnalyticsResponse {
    private BigDecimal todaysSales;
    private BigDecimal todaysProfit;
    private Long todaysTransactions;
    private Long totalProducts;
    private Long lowStockItems;
    private List<DailySales> dailySalesData;
    private List<CategorySales> categorySalesData;

    public static class DailySales {
        private LocalDate date;
        private BigDecimal sales;
        private BigDecimal profit;
        private Long transactions;

        public DailySales(LocalDate date, BigDecimal sales, BigDecimal profit, Long transactions) {
            this.date = date;
            this.sales = sales;
            this.profit = profit;
            this.transactions = transactions;
        }

        // Getters and Setters
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public BigDecimal getSales() { return sales; }
        public void setSales(BigDecimal sales) { this.sales = sales; }

        public BigDecimal getProfit() { return profit; }
        public void setProfit(BigDecimal profit) { this.profit = profit; }

        public Long getTransactions() { return transactions; }
        public void setTransactions(Long transactions) { this.transactions = transactions; }
    }

    public static class CategorySales {
        private String category;
        private BigDecimal revenue;
        private Long quantity;

        public CategorySales(String category, BigDecimal revenue, Long quantity) {
            this.category = category;
            this.revenue = revenue;
            this.quantity = quantity;
        }

        // Getters and Setters
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public BigDecimal getRevenue() { return revenue; }
        public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }

        public Long getQuantity() { return quantity; }
        public void setQuantity(Long quantity) { this.quantity = quantity; }
    }

    // Getters and Setters
    public BigDecimal getTodaysSales() { return todaysSales; }
    public void setTodaysSales(BigDecimal todaysSales) { this.todaysSales = todaysSales; }

    public BigDecimal getTodaysProfit() { return todaysProfit; }
    public void setTodaysProfit(BigDecimal todaysProfit) { this.todaysProfit = todaysProfit; }

    public Long getTodaysTransactions() { return todaysTransactions; }
    public void setTodaysTransactions(Long todaysTransactions) { this.todaysTransactions = todaysTransactions; }

    public Long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(Long totalProducts) { this.totalProducts = totalProducts; }

    public Long getLowStockItems() { return lowStockItems; }
    public void setLowStockItems(Long lowStockItems) { this.lowStockItems = lowStockItems; }

    public List<DailySales> getDailySalesData() { return dailySalesData; }
    public void setDailySalesData(List<DailySales> dailySalesData) { this.dailySalesData = dailySalesData; }

    public List<CategorySales> getCategorySalesData() { return categorySalesData; }
    public void setCategorySalesData(List<CategorySales> categorySalesData) { this.categorySalesData = categorySalesData; }
}