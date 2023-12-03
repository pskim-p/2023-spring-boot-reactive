package com.pskim.springboot.reactive.service;

import com.pskim.springboot.reactive.ecommerce.domain.model.Item;
import com.pskim.springboot.reactive.ecommerce.domain.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

@DataMongoTest
public class MongoDbSliceTest {
    // this is slice test for reducing test duration time.
    // it can be tested only with 60% times.
    @Autowired
    ItemRepository itemRepository;

    @Test
    void itemRepositorySaveItems(){
        // given
        Item sampleItem = new Item("name", 19.99);

        StepVerifier
                // when
                .create(itemRepository.save(sampleItem))
                // then
                .expectNextMatches(
                        item -> {
                            Assertions.assertThat(item.getId()).isNotNull();
                            Assertions.assertThat(item.getName()).isEqualTo(sampleItem.getName());
                            Assertions.assertThat(item.getPrice()).isEqualTo(sampleItem.getPrice());
                            return true;
                        }
                ).verifyComplete();
    }
}
