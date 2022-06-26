package com.micropos.carts.repository;

import com.micropos.carts.model.CartItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface CartItemRepository extends R2dbcRepository<CartItem, Integer> {
    Flux<CartItem> findByCartId(Integer cartId);
}
