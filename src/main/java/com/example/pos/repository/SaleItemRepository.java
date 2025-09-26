package com.example.pos.repository;

import com.example.pos.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    List<SaleItem> findBySaleId(Long saleId);
    List<SaleItem> findByProductId(Long productId);

    @Query("SELECT si FROM SaleItem si JOIN si.sale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    List<SaleItem> findBySaleDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT si.product.category.name, SUM(si.quantity * si.unitPrice) as revenue, SUM(si.quantity) as quantity " +
            "FROM SaleItem si JOIN si.product p JOIN p.category c GROUP BY c.name")
    List<Object[]> getCategorySalesData();
}