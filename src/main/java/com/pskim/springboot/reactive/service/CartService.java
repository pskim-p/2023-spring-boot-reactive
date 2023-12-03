package com.pskim.springboot.reactive.service;

import com.pskim.springboot.reactive.ecommerce.domain.model.Cart;
import com.pskim.springboot.reactive.ecommerce.domain.model.CartItem;
import com.pskim.springboot.reactive.ecommerce.domain.repository.CartRepository;
import com.pskim.springboot.reactive.ecommerce.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

@RequiredArgsConstructor
@Service
public class CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    public Mono<Cart> addToCart(String cartId, String itemId) {
        return this.cartRepository.findById(cartId)
                .log("foundCart", Level.FINE)
                .defaultIfEmpty(new Cart(cartId))
                .log("emptyCart")
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                        .findAny()
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(cart).log("newCartItem");
                        })
                        .orElseGet(() -> this.itemRepository.findById(itemId)
                                .log("fetchedItem")
                                .map(CartItem::new)
                                .log("cartItem")
                                .map(cartItem -> {
                                    cart.getCartItems().add(cartItem);
                                    return cart;
                                }))).log("addedCartItem")
                .log("cartWithAnotherItem")
                .flatMap(this.cartRepository::save)
                .log("savedCart");
    }
}
