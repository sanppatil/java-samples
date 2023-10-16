package com.enodation.processor;

import com.enodation.service.LiveStreamConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Processor implements CommandLineRunner  {

    @Autowired
    private LiveStreamConsumer livestreamConsumer;

    @Override
    public void run(String... args) throws Exception {
        livestreamConsumer.readFromLivestream();
    }
}
