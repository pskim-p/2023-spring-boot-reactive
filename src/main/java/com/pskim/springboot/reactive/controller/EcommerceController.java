package com.pskim.springboot.reactive.controller;

import com.pskim.springboot.reactive.ecommerce.domain.model.Cart;
import com.pskim.springboot.reactive.ecommerce.domain.model.CartItem;
import com.pskim.springboot.reactive.ecommerce.domain.model.Item;
import com.pskim.springboot.reactive.ecommerce.domain.repository.CartRepository;
import com.pskim.springboot.reactive.ecommerce.domain.repository.ItemRepository;
import com.pskim.springboot.reactive.service.CartService;
import com.pskim.springboot.reactive.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
@Slf4j
public class EcommerceController {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final InventoryService inventoryService;

    private static final String DEFAULT_CART_ID = "My Cart";

    @GetMapping("/shop")
    Mono<Rendering> shop() {
        return Mono.just(Rendering.view("shop.html")
                .modelAttribute("items", itemRepository.findAll())
                .modelAttribute("cart", cartRepository.findById(DEFAULT_CART_ID)
                        .defaultIfEmpty(new Cart(DEFAULT_CART_ID)))
                .build()
        );
    }

    @PostMapping("/shop/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return cartService.addToCart(DEFAULT_CART_ID, id).thenReturn("redirect:/shop");
    }

    @GetMapping("shot/search")
    Mono<Rendering> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam boolean useAnd
    ) {
        return Mono.just(Rendering.view("/shop")
                .modelAttribute("results", inventoryService.searchByExample(name, description, useAnd))
                .build());
    }
}
