package org.example.ordersservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.ordersservice.model.dto.OrderRequestDto;
import org.example.ordersservice.model.dto.OrderResponseDto;
import org.example.ordersservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "API for managing orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order")
    @ApiResponse(responseCode = "201", description = "Order created")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto request) {
        OrderResponseDto response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        OrderResponseDto response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get orders by date and amount")
    @ApiResponse(responseCode = "200", description = "Orders found")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByDateAndAmount(
            @RequestParam LocalDate date,
            @RequestParam BigDecimal amount) {
        List<OrderResponseDto> response = orderService.getOrdersByDateAndAmount(date, amount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    @Operation(summary = "Get orders without a product and between dates")
    @ApiResponse(responseCode = "200", description = "Orders found")
    public ResponseEntity<List<OrderResponseDto>> getOrdersWithoutProductAndBetweenDates(
            @RequestParam String productName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<OrderResponseDto> response = orderService.getOrdersWithoutProductAndBetweenDates(productName, startDate, endDate);
        return ResponseEntity.ok(response);
    }
}