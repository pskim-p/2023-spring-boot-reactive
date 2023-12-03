package com.pskim.springboot.reactive.service;

import com.pskim.springboot.reactive.ecommerce.domain.model.Item;
import com.pskim.springboot.reactive.ecommerce.domain.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Service
public class InventoryService {

    private final ItemRepository itemRepository;
    private final ReactiveFluentMongoOperations reactiveFluentMongoOperations;


    public Flux<Item> findAll(){
        return itemRepository.findAll();
    }
    public Flux<Item> searchByExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, 0.0, description);

        ExampleMatcher exampleMatcher = (useAnd)
                ? ExampleMatcher.matchingAll()
                : ExampleMatcher.matchingAny()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                // ignore null fields by default. but double can't be null so need to ignore manually.
                .withIgnorePaths("price");

        Example<Item> probe = Example.of(item, exampleMatcher);
        return this.itemRepository.findAll(probe);
    }

    public Flux<Item> searchByFluentExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, 0.0, description);
        ExampleMatcher exampleMatcher = (useAnd)
                ? ExampleMatcher.matchingAll()
                : ExampleMatcher.matchingAny()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                // ignore null fields by default. but double can't be null so need to ignore manually.
                .withIgnorePaths("price");

        return reactiveFluentMongoOperations.query(Item.class)
                .matching(Query.query(Criteria.byExample(Example.of(item, exampleMatcher)))).all();
    }
}
