package com.micropos.carts.rest;

import com.micropos.api.CartsApi;
import com.micropos.carts.mapper.CartMapper;
import com.micropos.carts.repository.CartRepository;
import com.micropos.carts.service.CartService;
import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import com.micropos.dto.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class CartController implements CartsApi {

    @Resource
    private CartService cartService;

    @Override
    public Mono<ResponseEntity<CartDto>> addItemToCart(Integer cartId, Mono<CartItemDto> cartItemDto, ServerWebExchange exchange) {
        return cartItemDto
                .flatMap(cartItemDto1 -> cartService.addItemToCart(cartId, cartItemDto1))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @Override
    public Mono<ResponseEntity<CartDto>> createCart(Mono<CartDto> cartDto, ServerWebExchange exchange) {
        return cartDto
                .flatMap(cartDto1 -> cartService.create(cartDto1))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Flux<CartDto>>> listCarts(ServerWebExchange exchange) {
        return Mono
                .just(cartService.list())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<CartDto>> showCartById(Integer cartId, ServerWebExchange exchange) {
        return cartService.getCartById(cartId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Double>> showCartTotal(Integer cartId, ServerWebExchange exchange) {
        return cartService.getCartTotal(cartId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<OrderDto>> checkoutCart(Integer cartId, ServerWebExchange exchange) {
        return cartService.checkout(cartId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
