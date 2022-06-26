package com.micropos.order.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Table(value = "order1")
public class Order implements Persistable<Integer>, Serializable {
    @Id
    private Integer id;
    private Integer cartId;
    private Double total;

    public Integer getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
