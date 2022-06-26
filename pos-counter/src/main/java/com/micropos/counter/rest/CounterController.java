package com.micropos.counter.rest;


import com.micropos.api.CounterApi;
import com.micropos.counter.repository.CounterService;
import com.micropos.dto.CartDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class CounterController implements CounterApi {

    @Resource
    private CounterService counterService;

    @Override
    public Mono<ResponseEntity<Double>> checkout(Mono<CartDto> cartDto, ServerWebExchange exchange) {
        return cartDto
                .map(counterService::checkout)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

