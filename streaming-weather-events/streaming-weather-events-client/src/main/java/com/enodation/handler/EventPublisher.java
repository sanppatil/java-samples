package com.enodation.handler;

import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    public static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    @Autowired
    private EventHubProducerClient eventHubProducerClient;

    @Async("event-publisher-thread-pool")
    public void publishToEventHub(EventDataBatch eventDataBatch) {
        eventHubProducerClient.send(eventDataBatch);
        logger.info(String.format("Batch is published to EventHub. BatchSizeInBytes: %d | NumberOfEventInBatch: %d", eventDataBatch.getSizeInBytes(), eventDataBatch.getCount()));

    }
}
