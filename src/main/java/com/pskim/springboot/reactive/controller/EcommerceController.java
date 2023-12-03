package com.pskim.springboot.reactive.controller;

import com.pskim.springboot.reactive.ecommerce.domain.model.Cart;
import com.pskim.springboot.reactive.ecommerce.domain.repository.CartRepository;
import com.pskim.springboot.reactive.ecommerce.domain.repository.ItemRepository;
import com.pskim.springboot.reactive.service.CartService;
import com.pskim.springboot.reactive.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Hooks;
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
    private static final String LIST_TITLE = "Inventory Management !!";


    @GetMapping("/shop")
    Mono<Rendering> shop() {
        Hooks.onOperatorDebug();
        return Mono.just(Rendering.view("shop.html")
                .modelAttribute("list_title", LIST_TITLE)
                .modelAttribute("items", itemRepository.findAll())
                .modelAttribute("cart", cartRepository.findById(DEFAULT_CART_ID)
                        .defaultIfEmpty(new Cart(DEFAULT_CART_ID)))
                .build()
        );
    }

    @PostMapping("/shop/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        Hooks.onOperatorDebug();
        return cartService.addToCart(DEFAULT_CART_ID, id).thenReturn("redirect:/shop");
    }

    @GetMapping("shop/search")
    Mono<Rendering> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam boolean useAnd
    ) {
        Hooks.onOperatorDebug();
        return Mono.just(Rendering.view("/shop")
                .modelAttribute("results", inventoryService.searchByExample(name, description, useAnd))
                .build());
    }
}
