package com.micropos.delivery.repository;

import com.micropos.delivery.model.Delivery;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends R2dbcRepository<Delivery, Integer> {

}
