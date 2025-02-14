package org.example.ordersservice.mapper;

import org.example.ordersservice.model.dto.OrderDetailsDto;
import org.example.ordersservice.model.dto.OrderResponseDto;
import org.example.ordersservice.model.entity.Order;
import org.example.ordersservice.model.dto.OrderRequestDto;
import org.example.ordersservice.model.entity.OrderDetails;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toEntity(OrderRequestDto request);

    @Mapping(target = "items", source = "items")
    OrderResponseDto toResponseDto(Order order);

    List<OrderResponseDto> toResponseDtoList(List<Order> orders);

    @Mapping(target = "order", ignore = true)
    OrderDetails toEntity(OrderDetailsDto Dto);

    OrderDetailsDto toItemDto(OrderDetails item);
}
