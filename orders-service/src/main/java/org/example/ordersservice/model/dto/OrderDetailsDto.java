package org.example.ordersservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDto {
    private Long articleId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
}
