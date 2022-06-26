package com.micropos.counter.repository;

import com.micropos.dto.CartDto;
import org.springframework.stereotype.Service;

@Service
public class CounterServiceImpl implements CounterService {

    @Override
    public Double checkout(CartDto cartDto) {
        return cartDto.getItems().stream()
                .mapToDouble(item -> item.getAmount() * item.getProduct().getPrice())
                .sum();
    }
}
