package org.example.ordersservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ordersservice.model.entity.DeliveryType;
import org.example.ordersservice.model.entity.PaymentType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private List<OrderDetailsDto> items;
    private String recipient;
    private String deliveryAddress;
    private PaymentType paymentType;
    private DeliveryType deliveryType;
}
