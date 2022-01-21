package com.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;


/*
* This class will auto create the topic but this is not recommended on production that's why we set the profile as local
* */

@Configuration
@Profile("local")
public class AutoCreateConfig {

    @Bean
    public NewTopic libraryEventTopic(){
        return TopicBuilder.name("library-events")
                .partitions(1)
                .replicas(1)
                .build();

    }


}
