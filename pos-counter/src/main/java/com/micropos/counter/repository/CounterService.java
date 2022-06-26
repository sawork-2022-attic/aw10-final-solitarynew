package com.micropos.counter.repository;

import com.micropos.dto.CartDto;

public interface CounterService {
    Double checkout(CartDto cartDto);
}
