package com.pskim.springboot.reactive.ecommerce.domain.model;

import com.mongodb.client.model.geojson.Point;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Accessors(chain = true)
@Data
public class Item {
    private @Id String id;
    private String name;
    private double price;
    private String description;
    private String distributorRegion;
    private Date releaseDate;
    private int availableUnit;
    private Point location;
    private boolean active;

    private Item(){}

    public Item(String name, double price){
        this.name = name;
        this.price = price;
    }

    public Item(String name, double price, String description){
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
