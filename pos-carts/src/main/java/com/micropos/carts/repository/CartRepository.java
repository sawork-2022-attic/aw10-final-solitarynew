package com.micropos.carts.repository;

import com.micropos.carts.model.Cart;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends R2dbcRepository<Cart, Integer> {

}
