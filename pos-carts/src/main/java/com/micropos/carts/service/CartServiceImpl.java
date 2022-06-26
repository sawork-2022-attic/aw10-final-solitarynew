package com.micropos.carts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micropos.carts.mapper.CartMapper;
import com.micropos.carts.model.Cart;
import com.micropos.carts.model.CartItem;
import com.micropos.carts.repository.CartItemRepository;
import com.micropos.carts.repository.CartRepository;
import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import com.micropos.dto.OrderDto;
import com.micropos.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    @Resource
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Resource
    private CircuitBreakerFactory circuitBreakerFactory;

    @Resource
    private CartMapper cartMapper;

    @Resource
    private CartRepository cartRepository;

    @Resource
    private CartItemRepository cartItemRepository;

    @Override
    public Mono<CartDto> create(CartDto cartDto) {
        // 踩过的坑，整整调试了一天...
        // 1. 实体类需要实现Persistable接口才可以save，究其原因只有一个save接口，在自己指定id而不通过数据库时，不能判断是否是新增还是更新
        // jpa对其的处理是不报错，视情况插入或者更新
        // r2dbc需要实现Persistable接口来判断是否为更新，经过测试发现默认认为是更新，且没有实际的更新内容时仍然会报错。这个问题在只有一列的数据库中更为严重，因为只有一列永远无法更新
        // 2. 很多情况下需要多次使用cartDto, 所以先用一个flatmap提取出这个变量
        // 3. 无法多次使用同一个Mono，所以使用then或者map重复使用cartDto时，需要先提取出cartDto
        // 4. Flux转Mono可以用collectList或者collectMap，Mono转Flux可以使用Flux()或者flatMap返回一个Flux
        return Flux.fromIterable(cartDto.getItems())
                        .map(cartItemDto -> {
                            CartItem cartItem = cartMapper.toCartItem(cartItemDto);
                            cartItem.setProductId(cartItemDto.getProduct().getId());
                            cartItem.setCartId(cartDto.getId());
                            return cartItem;
                        })
                        .flatMap(cartItemRepository::save)
                        .then(Mono.just(cartDto))
                        .map(cartMapper::toCart)
                        .flatMap(cartRepository::save)
                        .then(Mono.just(cartDto));
    }

    @Override
    public Mono<CartDto> addItemToCart(Integer cartId, CartItemDto cartItemDto) {
        return Mono.just(cartItemDto)
                .map(cartItemDto1 -> {
                    CartItem cartItem = cartMapper.toCartItem(cartItemDto1);
                    cartItem.setProductId(cartItemDto1.getProduct().getId());
                    cartItem.setCartId(cartId);
                    return cartItem;
                })
                .flatMap(cartItemRepository::save)
                .then(Mono.just(cartId))
                .flatMap(cartRepository::findById)
                .flatMap(cart -> {
                    Flux<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
                    Map<Integer, ProductDto> productDtoMap = getProducts();
                    return cartMapper.toCartDto(cart, cartItems, productDtoMap);
                });
    }

    @Override
    public Flux<CartDto> list() {
        return cartRepository.findAll()
                .flatMap(cart -> {
                    Flux<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
                    Map<Integer, ProductDto> productDtoMap = getProducts();
                    return cartMapper.toCartDto(cart, cartItems, productDtoMap);
                });
    }

    @Override
    public Mono<CartDto> getCartById(Integer cartId) {
        return cartRepository.findById(cartId)
                .flatMap(cart -> {
                    Flux<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
                    Map<Integer, ProductDto> productDtoMap = getProducts();
                    return cartMapper.toCartDto(cart, cartItems, productDtoMap);
                });
    }

    @Override
    public Mono<Double> getCartTotal(Integer cartId) {
        return cartRepository.findById(cartId)
                .flatMap(cart -> {
                    Flux<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
                    Map<Integer, ProductDto> productDtoMap = getProducts();
                    return cartMapper.toCartDto(cart, cartItems, productDtoMap);
                })
                .map(this::getTotal);
    }


    @Override
    public Mono<OrderDto> checkout(Integer cartId) {
        return cartRepository.findById(cartId)
                .flatMap(cart -> {
                    Flux<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
                    Map<Integer, ProductDto> productDtoMap = getProducts();
                    return cartMapper.toCartDto(cart, cartItems, productDtoMap);
                })
                .map(cartDto -> {
                    Double total = getTotal(cartDto);
                    OrderDto orderDto = new OrderDto();
                    orderDto.setId(cartDto.getId());
                    orderDto.setCartId(cartDto.getId());
                    orderDto.setTotal(total);
                    return checkout(orderDto);
                });
    }

    public Map<Integer, ProductDto> getProducts() {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> {
            ResponseEntity<String> productResponseEntity = restTemplate.
                    getForEntity("http://pos-products/api/products/", String.class);
            String productResponse = productResponseEntity.getBody();
            List<ProductDto> productDtoList = null;
            try {
                productDtoList = new ObjectMapper().readValue(productResponse, new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return productDtoList.stream().collect(
                    LinkedHashMap::new,
                    (map, productDto) -> map.put(productDto.getId(), productDto),
                    Map::putAll
            );
        }, ex -> {
            log.error("Error while getting products", ex);
            return null;
        });
    }

    public Double getTotal(CartDto cartDto) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<CartDto> httpEntity = new HttpEntity<>(cartDto, headers);
            ResponseEntity<Double> productResponseEntity = restTemplate.
                    postForEntity("http://pos-counter/api/counter/checkout", httpEntity, Double.class);
            return productResponseEntity.getBody();
        }, ex -> {
            log.error(ex.getMessage(), ex);
            return null;
        });
    }

    public OrderDto checkout(OrderDto orderDto) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<OrderDto> httpEntity = new HttpEntity<>(orderDto, headers);
            ResponseEntity<OrderDto> productResponseEntity = restTemplate.
                    postForEntity("http://pos-order/api/orders", httpEntity, OrderDto.class);
            return productResponseEntity.getBody();
        }, ex -> {
            log.error(ex.getMessage(), ex);
            return null;
        });
    }
}
