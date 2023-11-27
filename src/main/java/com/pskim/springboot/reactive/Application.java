package com.pskim.springboot.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@SpringBootApplication
public class Application extends AbstractReactiveMongoConfiguration {

    private static final String MONGO_DB_NAME = "mydb";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected String getDatabaseName() {
        return MONGO_DB_NAME;
    }
}
