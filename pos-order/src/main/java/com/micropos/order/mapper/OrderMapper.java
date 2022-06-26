package com.micropos.order.mapper;

import com.micropos.dto.OrderDto;
import com.micropos.order.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper{
    Order toOrder(OrderDto orderDto);
    OrderDto toOrderDto(Order order);
    List<OrderDto> toOrderDtoList(List<Order> orderList);
}
