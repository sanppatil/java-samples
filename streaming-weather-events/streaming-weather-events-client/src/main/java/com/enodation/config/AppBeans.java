package com.enodation.config;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class AppBeans {

    @Autowired
    private AppConfiguration appConfig;

    @Autowired
    private EventHubConfiguration eventHubConfig;

    @Autowired
    private Environment env;

    @Bean
    public BlockingQueue<String> dataQueue() {
        return new LinkedBlockingQueue<String>();
    }

    @Bean(name = "event-publisher-thread-pool")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("event-sender-");
        executor.initialize();
        return executor;
    }

    @Bean
    public EventHubProducerClient getEventHubProducerClient() {
        return new EventHubClientBuilder()
                //.connectionString(env.getProperty("EVENT_HUB_CONNECTION_STRING"), eventHubConfig.getEventHubName())
                .connectionString(eventHubConfig.getConnectionString(), eventHubConfig.getEventHubName())
                .buildProducerClient();
    }

}
