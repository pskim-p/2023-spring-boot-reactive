package com.pskim.springboot.reactive.ecommerce.domain.repository;

import com.pskim.springboot.reactive.ecommerce.domain.model.Item;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RepositoryDatabaseLoader {

    CommandLineRunner initialize(ItemRepository repository) {
        return args -> {
            repository.save(new Item("alarmClock", 19.99));
            repository.save(new Item("smart-TV-tray", 244.99));
        };
    }
}
