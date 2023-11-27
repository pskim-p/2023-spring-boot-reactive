package com.pskim.springboot.reactive.ecommerce.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Item {
    private @Id String id;
    private String name;
    private double price;

    public Item(String name, double price){
        this.name = name;
        this.price = price;
    }
}
