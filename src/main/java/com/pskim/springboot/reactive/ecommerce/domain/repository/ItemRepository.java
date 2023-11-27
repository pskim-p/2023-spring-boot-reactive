package com.pskim.springboot.reactive.ecommerce.domain.repository;

import com.pskim.springboot.reactive.ecommerce.domain.model.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends ReactiveCrudRepository<Item, String> {
}
