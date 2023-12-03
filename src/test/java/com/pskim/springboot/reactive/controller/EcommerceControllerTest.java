package com.pskim.springboot.reactive.controller;

import com.pskim.springboot.reactive.ecommerce.domain.model.Cart;
import com.pskim.springboot.reactive.ecommerce.domain.model.Item;
import com.pskim.springboot.reactive.service.CartService;
import com.pskim.springboot.reactive.service.InventoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(EcommerceController.class)
class EcommerceControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean InventoryService inventoryService;
    @MockBean
    CartService cartService;

    @Test
    void homePage() {
        // given
        when(inventoryService.findAll()).thenReturn(
                Flux.just(
                        new Item("test1", 19.99).setId("id1"),
                        new Item("test2", 29.99).setId("id2")
                ));

        when(cartService.findByDefaultCartId()).thenReturn(Mono.just((new Cart("My Cart"))));

        // when
        client.get().uri("/shop").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(exchangeResult -> {
                    Assertions.assertThat(exchangeResult.getResponseBody()).contains("action=\"/shop/add/id1");
                    Assertions.assertThat(exchangeResult.getResponseBody()).contains("action=\"/shop/add/id2");
                });
    }
}
