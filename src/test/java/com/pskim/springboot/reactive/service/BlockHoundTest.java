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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BlockHoundTest {

    BlockCartService blockCartService;

    @MockBean
    ItemRepository itemRepository;
    @MockBean
    CartRepository cartRepository;

    Item testItem;

    @BeforeEach
    void setup() {
        BlockHound.install();

        // given
        testItem = new Item("test-item", 19.99).setId("id1");
        CartItem cartItem = new CartItem(testItem);
        Cart cart = new Cart("My Cart", Collections.singletonList(cartItem));

        // should hide Mono.empty because, it will be optimized to return singleton object without blocking.
        // Mono.hide prevents optimization to capture valid logics
        when(cartRepository.findById(anyString())).thenReturn(Mono.<Cart> empty().hide());

        when(itemRepository.findById(anyString())).thenReturn(Mono.just(testItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(cart));

        blockCartService = new BlockCartService(itemRepository, cartRepository);

    }

    @Test
    void threadSleepIsABlockingCall() {
        Mono.delay(Duration.ofSeconds(1))
                .flatMap(tick -> {
                    try {
                        Thread.sleep(10);
                        return Mono.just(true);
                    } catch (InterruptedException e) {
                        return Mono.error(e);
                    }
                })
                .as(StepVerifier::create)
                .verifyErrorMatches(throwable -> {
                    Assertions.assertThat(throwable.getMessage()).contains("Blocking call! java.lang.Thread.sleep");
                    return true;
                });
    }

    @Test
    void blockHoundShouldTrapBlockingCall() {
        Mono.delay(Duration.ofSeconds(1))
                .flatMap(tick -> blockCartService.addToCart("My Cart", "id1"))
                .as(StepVerifier::create)
                .verifyErrorSatisfies(
                        throwable -> Assertions.assertThat(throwable)
                                .hasMessageContaining("block()/blockFirst()/blockLast() are blocking")
                );
    }
}
