package com.pskim.springboot.reactive.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor(staticName = "of")
@Getter
@ToString
public class Dish {
    private final String name;
    private boolean delivered = false;

    public static Dish deliver(Dish dish) {
        Dish deliveredDish = Dish.of(dish.name);
        deliveredDish.delivered = true;
        return deliveredDish;
    }
}
