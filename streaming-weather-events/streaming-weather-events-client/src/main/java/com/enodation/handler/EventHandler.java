package com.enodation.handler;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class EventHandler implements Runnable {

    public static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

    @Autowired
    private EventHubProducerClient eventHubProducerClient;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private BlockingQueue<String> dataQueue;

    @Override
    public void run() {
        try {
            StopWatch stopWatch = StopWatch.createStarted();
            EventDataBatch eventDataBatch = eventHubProducerClient.createBatch();
            while (true) {
                EventData eventData = new EventData(dataQueue.take());
                if (eventDataBatch.tryAdd(eventData) && (stopWatch.getTime(TimeUnit.SECONDS)<=10)) {
                    continue;
                }
                logger.info("Current batch is full, so we publish it and create new batch for upcoming events...");
                eventPublisher.publishToEventHub(eventDataBatch);
                stopWatch = StopWatch.createStarted();
                eventDataBatch = eventHubProducerClient.createBatch();
                if (!eventDataBatch.tryAdd(eventData)) {
                    logger.warn("Event is too large for an empty batch. Skipping. Max size: %s.", eventDataBatch.getMaxSizeInBytes());
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
