package org.example.ordersservice.service;

import org.example.ordersservice.model.dto.OrderRequestDto;
import org.example.ordersservice.model.dto.OrderResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    OrderResponseDto createOrder(OrderRequestDto request);
    OrderResponseDto getOrderById(Long id);
    List<OrderResponseDto> getOrdersByDateAndAmount(LocalDate date, BigDecimal amount);
    List<OrderResponseDto> getOrdersWithoutProductAndBetweenDates(String productName, LocalDate startDate, LocalDate endDate);

}
