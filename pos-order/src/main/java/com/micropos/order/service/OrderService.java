package com.micropos.order.service;


import com.micropos.dto.OrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrderService {

    Mono<OrderDto> createOrder(OrderDto orderDto);

    Flux<OrderDto> listOrders();

    Mono<OrderDto> getOrderById(Integer orderId);
}
