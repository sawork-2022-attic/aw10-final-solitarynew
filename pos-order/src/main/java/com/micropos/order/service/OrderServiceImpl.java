package com.micropos.order.service;

import com.micropos.dto.OrderDto;
import com.micropos.order.mapper.OrderMapper;
import com.micropos.order.model.Order;
import com.micropos.order.repository.OrderRepository;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private StreamBridge streamBridge;

    @Override
    public Mono<OrderDto> createOrder(OrderDto orderDto) {
        return Mono.just(orderDto)
                .map(orderMapper::toOrder)
                .flatMap(orderRepository::save)
                .map(order -> {
                    OrderDto orderDto1 = orderMapper.toOrderDto(order);
                    streamBridge.send("sms-in-0", orderDto1);
                    return orderDto1;
                });
    }

    @Override
    public Flux<OrderDto> listOrders() {
        return orderRepository.findAll()
                .map(orderMapper::toOrderDto);
    }

    @Override
    public Mono<OrderDto> getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toOrderDto);
    }

}
