package com.micropos.delivery.service;

import com.micropos.delivery.mapper.DeliveryMapper;
import com.micropos.delivery.model.Delivery;
import com.micropos.delivery.repository.DeliveryRepository;
import com.micropos.dto.DeliveryDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Resource
    private DeliveryMapper deliveryMapper;

    @Resource
    private DeliveryRepository deliveryRepository;

    @Override
    public Mono<DeliveryDto> createDelivery(DeliveryDto deliveryDto) {
        return Mono.just(deliveryDto)
                .map(deliveryMapper::deliveryDtoToDelivery)
                .flatMap(deliveryRepository::save)
                .map(deliveryMapper::deliveryToDeliveryDto);

    }

    @Override
    public Flux<DeliveryDto> listDeliveries() {
        return deliveryRepository.findAll()
                .map(deliveryMapper::deliveryToDeliveryDto);
    }

    @Override
    public Mono<DeliveryDto> getDeliveryById(Integer deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .map(deliveryMapper::deliveryToDeliveryDto);
    }

}
