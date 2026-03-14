package com.anshika.Re_EquippedEventFlowEngine.ConsumerQueue;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.EnableAsync;

@Component
@EnableAsync
public class ConsumerStarter {

    private final EventConsumer eventConsumer;

    public ConsumerStarter(EventConsumer eventConsumer) {
        this.eventConsumer = eventConsumer;
    }

    @PostConstruct
    public void startConsumer() {
        eventConsumer.run();
    }
}