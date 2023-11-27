package com.pskim.springboot.reactive.controller;

import com.pskim.springboot.reactive.ecommerce.domain.model.Cart;
import com.pskim.springboot.reactive.ecommerce.domain.model.Item;
import com.pskim.springboot.reactive.ecommerce.domain.repository.CartRepository;
import com.pskim.springboot.reactive.ecommerce.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
@Slf4j
public class EcommerceController {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final MongoOperations mongoOperations;

    private static final String DEFAULT_CART_ID = "My Cart";

    @GetMapping("/shop")
    Mono<Rendering> shop() {
        mongoOperations.findAll(Item.class);
        itemRepository.findAll().log().subscribe(it -> log.info(it.getId()));
        return Mono.just(Rendering.view("shop.html")
                .modelAttribute("items", itemRepository.findAll())
                .modelAttribute("cart", cartRepository.findById(DEFAULT_CART_ID)
                        .defaultIfEmpty(new Cart(DEFAULT_CART_ID)))
                .build()
        );
    }
}
