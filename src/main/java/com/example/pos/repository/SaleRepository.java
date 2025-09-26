package com.example.pos.repository;

import com.example.pos.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findByInvoiceId(String invoiceId);
    List<Sale> findBySaleDate(LocalDate date);
    List<Sale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);
    List<Sale> findByCashierId(Long cashierId);
    List<Sale> findByIsReturn(Boolean isReturn);

    @Query("SELECT s FROM Sale s WHERE " +
            "LOWER(s.invoiceId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.customerName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "s.customerPhone LIKE CONCAT('%', :searchTerm, '%')")
    List<Sale> findBySearchTerm(@Param("searchTerm") String searchTerm);

    @Query("SELECT SUM(s.total) FROM Sale s WHERE s.saleDate = :date AND s.isReturn = false")
    Double getTotalSalesByDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(s.profit) FROM Sale s WHERE s.saleDate = :date AND s.isReturn = false")
    Double getTotalProfitByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.saleDate = :date AND s.isReturn = false")
    Long getTransactionCountByDate(@Param("date") LocalDate date);

    @Query("SELECT p.category.name, SUM(si.quantity * si.unitPrice), SUM(si.quantity) " +
            "FROM SaleItem si JOIN si.product p " +
            "WHERE si.product IS NOT NULL " +
            "GROUP BY p.category.name")
    List<Object[]> getCategorySalesData();
}