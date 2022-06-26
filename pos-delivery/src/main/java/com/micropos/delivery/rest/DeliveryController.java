package com.micropos.delivery.rest;

import com.micropos.api.DeliveriesApi;
import com.micropos.delivery.service.DeliveryService;
import com.micropos.dto.DeliveryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping("api")
public class DeliveryController implements DeliveriesApi {

    @Resource
    private DeliveryService deliveryService;

    @Override
    public Mono<ResponseEntity<DeliveryDto>> createDelivery(Mono<DeliveryDto> deliveryDto, ServerWebExchange exchange) {
        return deliveryDto
                .flatMap(deliveryService::createDelivery)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Flux<DeliveryDto>>> listDeliveries(ServerWebExchange exchange) {
        return Mono
                .just(deliveryService.listDeliveries())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<DeliveryDto>> showDeliveryById(Integer deliveryId, ServerWebExchange exchange) {
        return deliveryService.getDeliveryById(deliveryId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
