package com.micropos.products.service;

import com.micropos.products.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductService {


    public Flux<Product> products();

    public Mono<Product> getProduct(Integer id);

}
