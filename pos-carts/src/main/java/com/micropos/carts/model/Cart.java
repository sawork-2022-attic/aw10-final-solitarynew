package com.micropos.carts.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Table(value = "cart")
public class Cart implements Persistable<Integer>, Serializable {
    @Id
    private Integer id;

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
}
