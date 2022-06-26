package com.micropos.carts.service;

import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import com.micropos.dto.OrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CartService {

    Mono<CartDto> create(CartDto cartDto);

    Mono<CartDto> addItemToCart(Integer cartId, CartItemDto cartItemDto);

    Flux<CartDto> list();

    Mono<CartDto> getCartById(Integer cartId);

    Mono<Double> getCartTotal(Integer cartId);

    Mono<OrderDto> checkout(Integer cartId);
}
