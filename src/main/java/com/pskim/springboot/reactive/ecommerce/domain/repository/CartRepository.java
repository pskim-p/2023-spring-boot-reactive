package com.pskim.springboot.reactive.ecommerce.domain.repository;

import com.pskim.springboot.reactive.ecommerce.domain.model.Cart;
import com.pskim.springboot.reactive.ecommerce.domain.model.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends ReactiveCrudRepository<Cart, String> {
}
