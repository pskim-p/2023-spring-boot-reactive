
package com.pskim.springboot.reactive.service;

import com.pskim.springboot.reactive.ecommerce.domain.model.Cart;
import com.pskim.springboot.reactive.ecommerce.domain.model.CartItem;
import com.pskim.springboot.reactive.ecommerce.domain.repository.CartRepository;
import com.pskim.springboot.reactive.ecommerce.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

/***
 * service for blockhound test
 */
@RequiredArgsConstructor
@Service
public class BlockCartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    private static final String DEFAULT_CART_ID = "My Cart";


    public Mono<Cart> findByDefaultCartId(){
       return cartRepository.findById(DEFAULT_CART_ID)
                .defaultIfEmpty(new Cart(DEFAULT_CART_ID));
    }

    public Mono<Cart> addToCart(String itemId){
        return this.addToCart(DEFAULT_CART_ID, itemId);
    }

    public Mono<Cart> addToCart(String cartId, String itemId) {
        Cart myCart = this.cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .block();

        return myCart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                        .findAny()
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(myCart);
                        })
                        .orElseGet(() -> this.itemRepository.findById(itemId)
                                .map(CartItem::new)
                                .map(cartItem -> {
                                    myCart.getCartItems().add(cartItem);
                                    return myCart;
                                }))
                .flatMap(this.cartRepository::save);
    }
}
