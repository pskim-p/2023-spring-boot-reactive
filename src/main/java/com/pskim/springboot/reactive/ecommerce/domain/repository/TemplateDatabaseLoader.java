package com.pskim.springboot.reactive.ecommerce.domain.repository;

import com.pskim.springboot.reactive.ecommerce.domain.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TemplateDatabaseLoader {

    private final ItemRepository itemRepository;

    @Bean
    CommandLineRunner initialize() {
        return args -> itemRepository.findAll()
                .count()
                .subscribe(
                        size -> {
                            if (size <= 0) {
                                itemRepository.save(new Item("alarmClock", 19.99)).subscribe();
                                itemRepository.save(new Item("smart-TV-tray", 244.99)).subscribe();
                            }
                        }
                );
    }
}
