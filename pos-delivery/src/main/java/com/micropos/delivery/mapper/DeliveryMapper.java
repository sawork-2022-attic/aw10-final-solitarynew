package com.micropos.delivery.mapper;

import com.micropos.delivery.model.Delivery;
import com.micropos.dto.DeliveryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DeliveryMapper {
    DeliveryDto deliveryToDeliveryDto(Delivery delivery);
    Delivery deliveryDtoToDelivery(DeliveryDto deliveryDto);
    List<DeliveryDto> deliveriesToDeliveryDtos(List<Delivery> deliveries);
}
