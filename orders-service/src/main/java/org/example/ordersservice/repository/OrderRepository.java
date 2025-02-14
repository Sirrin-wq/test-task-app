package org.example.ordersservice.repository;

import org.example.ordersservice.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDateAndTotalAmountGreaterThanEqual(LocalDate date, BigDecimal amount);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate AND NOT EXISTS (SELECT i FROM o.items i WHERE i.productName = :productName)")
    List<Order> findOrdersWithoutProductAndBetweenDates(
            @Param("productName") String productName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
