package com.pskim.springboot.reactive.ecommerce.domain.repository;

import com.pskim.springboot.reactive.ecommerce.domain.model.Item;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ItemRepository extends ReactiveCrudRepository<Item, String>, ReactiveQueryByExampleExecutor<Item> {
    Flux<Item> findByNameContaining(String partialName);
}
