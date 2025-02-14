package org.example.ordersservice.service.impl;

import org.example.ordersservice.mapper.OrderMapper;
import org.example.ordersservice.model.dto.OrderDetailsDto;
import org.example.ordersservice.model.dto.OrderRequestDto;
import org.example.ordersservice.model.dto.OrderResponseDto;
import org.example.ordersservice.model.entity.Order;
import org.example.ordersservice.model.entity.OrderDetails;
import org.example.ordersservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderServiceImpl orderService;

    private final String TEST_ORDER_NUMBER = "1111120241212";
    private final Long TEST_ORDER_ID = 1L;

    @Test
    void createOrderSuccess() {
        OrderRequestDto request = createTestRequest();
        Order mockOrder = createTestOrder();
        OrderResponseDto expectedResponse = createTestResponse();
        OrderDetails mockOrderDetails = new OrderDetails(1L, 1L, "Laptop", 1, new BigDecimal(100), mockOrder);
        OrderDetailsDto mockOrderDetailsDto = new OrderDetailsDto(1L, "Laptop", 1, new BigDecimal(100));

        List<OrderDetailsDto> items = new ArrayList<>();
        items.add(mockOrderDetailsDto);
        request.setItems(items);

        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(TEST_ORDER_NUMBER, HttpStatus.OK));
        when(orderMapper.toEntity(any(OrderRequestDto.class))).thenReturn(mockOrder);
        when(orderMapper.toEntity(any(OrderDetailsDto.class))).thenReturn(mockOrderDetails);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(expectedResponse);

        OrderResponseDto result = orderService.createOrder(request);

        assertNotNull(result);
        assertEquals(TEST_ORDER_NUMBER, mockOrder.getOrderNumber());
        verify(orderRepository).save(mockOrder);
    }

    @Test
    void createOrderFailedToGetOrderNumber() {
        OrderRequestDto request = createTestRequest();

        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.createOrder(request));

        assertTrue(exception.getMessage().contains("Failed to get order number"));
    }

    @Test
    void getOrderByIdSuccess() {
        Order mockOrder = createTestOrder();
        OrderResponseDto expectedResponse = createTestResponse();

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(mockOrder));
        when(orderMapper.toResponseDto(mockOrder)).thenReturn(expectedResponse);

        OrderResponseDto result = orderService.getOrderById(TEST_ORDER_ID);

        assertNotNull(result);
        assertEquals(TEST_ORDER_ID, result.getId());
    }

    @Test
    void getOrderByIdNotFound() {
        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.getOrderById(TEST_ORDER_ID));

        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    void getOrdersByDateAndAmountSuccess() {
        LocalDate date = LocalDate.now();
        BigDecimal amount = BigDecimal.valueOf(100);
        Order mockOrder = createTestOrder();
        OrderResponseDto mockResponse = createTestResponse();

        when(orderRepository.findByOrderDateAndTotalAmountGreaterThanEqual(date, amount))
                .thenReturn(Collections.singletonList(mockOrder));
        when(orderMapper.toResponseDtoList(anyList())).thenReturn(Collections.singletonList(mockResponse));

        List<OrderResponseDto> result = orderService.getOrdersByDateAndAmount(date, amount);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getOrdersWithoutProductAndBetweenDatesSuccess() {
        String productName = "TestProduct";
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        Order mockOrder = createTestOrder();
        OrderResponseDto mockResponse = createTestResponse();

        when(orderRepository.findOrdersWithoutProductAndBetweenDates(productName, startDate, endDate))
                .thenReturn(Collections.singletonList(mockOrder));
        when(orderMapper.toResponseDtoList(anyList())).thenReturn(Collections.singletonList(mockResponse));

        List<OrderResponseDto> result = orderService.getOrdersWithoutProductAndBetweenDates(productName, startDate, endDate);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    private OrderRequestDto createTestRequest() {
        OrderRequestDto request = new OrderRequestDto();
        request.setItems(Collections.singletonList(new OrderDetailsDto()));
        return request;
    }

    private Order createTestOrder() {
        Order order = new Order();
        order.setId(TEST_ORDER_ID);
        order.setOrderNumber(TEST_ORDER_NUMBER);
        order.setTotalAmount(BigDecimal.valueOf(100));
        return order;
    }

    private OrderResponseDto createTestResponse() {
        OrderResponseDto response = new OrderResponseDto();
        response.setId(TEST_ORDER_ID);
        response.setOrderNumber(TEST_ORDER_NUMBER);
        return response;
    }
}
