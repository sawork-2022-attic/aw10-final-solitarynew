package com.micropos.order.rest;

import com.micropos.api.OrdersApi;
import com.micropos.dto.OrderDto;
import com.micropos.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping("api")
public class OrderController implements OrdersApi {

    @Resource
    private OrderService orderService;

    @Override
    public Mono<ResponseEntity<OrderDto>> createOrder(Mono<OrderDto> orderDto, ServerWebExchange exchange) {
        return orderDto
                .flatMap(orderService::createOrder)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @Override
    public Mono<ResponseEntity<Flux<OrderDto>>> listOrders(ServerWebExchange exchange) {
        return Mono
                .just(orderService.listOrders())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @Override
    public Mono<ResponseEntity<OrderDto>> showOrderById(Integer orderId, ServerWebExchange exchange) {
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
