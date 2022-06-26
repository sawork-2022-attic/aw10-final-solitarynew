package com.micropos.carts.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Table(value = "cart_item")
public class CartItem implements Persistable<Integer>, Serializable {
    @Id
    private Integer id;
    private Integer amount;
    private Integer productId;
    private Integer cartId;

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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }
}
