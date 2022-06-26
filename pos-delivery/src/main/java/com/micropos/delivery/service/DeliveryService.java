package com.micropos.delivery.service;

import com.micropos.dto.DeliveryDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DeliveryService {

    Mono<DeliveryDto> createDelivery(DeliveryDto deliveryDto);

    Flux<DeliveryDto> listDeliveries();

    Mono<DeliveryDto> getDeliveryById(Integer deliveryId);
}
