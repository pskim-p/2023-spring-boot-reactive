package com.pskim.springboot.reactive.controller;

import com.pskim.springboot.reactive.service.CartService;
import com.pskim.springboot.reactive.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
@Slf4j
public class EcommerceController {

    private final CartService cartService;
    private final InventoryService inventoryService;

    private static final String LIST_TITLE = "Inventory Management !!";


    @GetMapping("/shop")
    Mono<Rendering> shop() {
        Hooks.onOperatorDebug();
        return Mono.just(Rendering.view("shop.html")
                .modelAttribute("list_title", LIST_TITLE)
                .modelAttribute("items", inventoryService.findAll())
                .modelAttribute("cart", cartService.findByDefaultCartId())
                .build()
        );
    }

    @PostMapping("/shop/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        Hooks.onOperatorDebug();
        return cartService.addToCart(id).thenReturn("redirect:/shop");
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
