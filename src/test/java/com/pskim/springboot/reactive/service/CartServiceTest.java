package com.pskim.springboot.reactive.service;

import com.pskim.springboot.reactive.ecommerce.domain.model.Cart;
import com.pskim.springboot.reactive.ecommerce.domain.model.CartItem;
import com.pskim.springboot.reactive.ecommerce.domain.model.Item;
import com.pskim.springboot.reactive.ecommerce.domain.repository.CartRepository;
import com.pskim.springboot.reactive.ecommerce.domain.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CartServiceTest {

    CartService sut;
    ItemRepository itemRepository = mock(ItemRepository.class);
    CartRepository cartRepository = mock(CartRepository.class);

    Item testItem;

    @BeforeEach
    void setup() {
        sut = new CartService(itemRepository, cartRepository);

        // given
        testItem = new Item("test-item", 19.99);
        CartItem cartItem = new CartItem(testItem);
        Cart cart = new Cart("My Cart", Collections.singletonList(cartItem));

        when(cartRepository.findById(anyString())).thenReturn(Mono.empty());
        when(itemRepository.findById(anyString())).thenReturn(Mono.just(testItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(cart));
    }

    @Test
    void addItemToEmptyCartShouldProduceOneCartItem() {
        // top-level test
        // when
        sut.addToCart("My Cart", "item")
                .as(StepVerifier::create)
                // then
                .expectNextMatches(cart -> {
                    Assertions.assertThat(cart.getCartItems())
                            .extracting(CartItem::getQuantity)
                            .containsExactlyInAnyOrder(1);

                    Assertions.assertThat(cart.getCartItems())
                            .extracting(CartItem::getItem)
                            .containsExactly(testItem);

                    return true;
                })
                .verifyComplete();
    }

    @Test
    void alternativeWayToTest() {
        StepVerifier.create(
                        // when
                        sut.addToCart("My Cart", "item")
                ).expectNextMatches(
                        // then
                        cart -> {
                            Assertions.assertThat(cart.getCartItems())
                                    .extracting(CartItem::getQuantity)
                                    .containsExactlyInAnyOrder(1);

                            Assertions.assertThat(cart.getCartItems())
                                    .extracting(CartItem::getItem)
                                    .containsExactly(testItem);

                            return true;
                        })
                .verifyComplete();
    }
}