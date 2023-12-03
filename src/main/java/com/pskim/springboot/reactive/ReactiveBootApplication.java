package com.pskim.springboot.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.thymeleaf.TemplateEngine;
import reactor.blockhound.BlockHound;

@EnableReactiveMongoRepositories
@SpringBootApplication
public class ReactiveBootApplication extends AbstractReactiveMongoConfiguration {

    private static final String MONGO_DB_NAME = "mydb";

    public static void main(String[] args) {
        BlockHound
                .builder()
                .allowBlockingCallsInside(
                        TemplateEngine.class.getCanonicalName(), "process")
                .install();
        SpringApplication.run(ReactiveBootApplication.class, args);
    }

    @Override
    protected String getDatabaseName() {
        return MONGO_DB_NAME;
    }
}
