package org.example.ordersservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ordersservice.model.entity.DeliveryType;
import org.example.ordersservice.model.entity.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String orderNumber;
    private BigDecimal totalAmount;
    private LocalDate orderDate;
    private String recipient;
    private String deliveryAddress;
    private PaymentType paymentType;
    private DeliveryType deliveryType;
    private List<OrderDetailsDto> items;
}
