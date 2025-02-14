package org.example.ordersservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.mapper.OrderMapper;
import org.example.ordersservice.model.dto.OrderRequestDto;
import org.example.ordersservice.model.dto.OrderResponseDto;
import org.example.ordersservice.model.entity.Order;
import org.example.ordersservice.model.entity.OrderDetails;
import org.example.ordersservice.repository.OrderRepository;
import org.example.ordersservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RestTemplate restTemplate;

    private static final String NUMBER_GENERATE_SERVICE_URL = "http://number-generate-service:80/numbers";

    @Override
    public OrderResponseDto createOrder(OrderRequestDto request) {
        String orderNumber = getOrderNumberFromNumberGenerateService();

        Order order = orderMapper.toEntity(request);
        order.setOrderNumber(orderNumber);
        order.setOrderDate(LocalDate.now());

        List<OrderDetails> items = request.getItems().stream()
                .map(dto -> {
                    OrderDetails item = orderMapper.toEntity(dto);
                    item.setOrder(order);
                    return item;
                })
                .toList();

        order.setItems(items);

        BigDecimal totalAmount = order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toResponseDto(order);
    }

    @Override
    public List<OrderResponseDto> getOrdersByDateAndAmount(LocalDate date, BigDecimal amount) {
        List<Order> orders = orderRepository.findByOrderDateAndTotalAmountGreaterThanEqual(date, amount);
        return orderMapper.toResponseDtoList(orders);
    }

    @Override
    public List<OrderResponseDto> getOrdersWithoutProductAndBetweenDates(String productName, LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findOrdersWithoutProductAndBetweenDates(productName, startDate, endDate);
        return orderMapper.toResponseDtoList(orders);
    }

    private String getOrderNumberFromNumberGenerateService() {
        ResponseEntity<String> response = restTemplate.getForEntity(NUMBER_GENERATE_SERVICE_URL, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get order number from number-generate-service");
        }
    }
}