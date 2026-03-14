package com.anshika.Re_EquippedEventFlowEngine.ConsumerQueue;

import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.EventQueue;
import com.anshika.Re_EquippedEventFlowEngine.DeadLetterQueue.DeadAndFailedEventStore;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.ConfirmedEventStore;
import com.anshika.Re_EquippedEventFlowEngine.StorageEvents.EventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;
import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

@Component
public class EventConsumer implements Runnable {

    private final EventQueue queue;
    private final DeadAndFailedEventStore deadEvents;
    private final EventStore eventStore;
    private final ConfirmedEventStore confirmStore;

    private final int maxRetries; // injected
    private final Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    public EventConsumer(EventQueue queue,
                         ConfirmedEventStore confirmStore,
                         DeadAndFailedEventStore deadEvents,
                         EventStore eventStore,
                         @Value("${event.consumer.maxRetries:5}") int maxRetries) {
        this.queue = queue;
        this.confirmStore = confirmStore;
        this.deadEvents = deadEvents;
        this.eventStore = eventStore;
        this.maxRetries = maxRetries;
    }

    @Override
    @Async
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Event event = queue.consume();
                if (event == null) {
                    logger.info(Thread.currentThread().getName() + " queue shutdown observed");
                    return;
                }

                if (confirmStore.isConfirmed(event.getId())) {
                    logger.info("Skipping already confirmed Event " + event.getId());
                    continue;
                }

                int attempt = 0;
                boolean success = false;

                while (attempt < maxRetries && !success) {
                    logger.info("Processing Event " + event.getId());
                    try {
                        // simulate random failure
                        if (random.nextInt(5) == 0) {
                            throw new RuntimeException("Simulated random failure");
                        }

                        confirmStore.confirmedEvent(event.getId());
                        logger.info("Confirmed Event " + event.getId());
                        success = true;

                    } catch (Exception e) {
                        attempt++;
                        logger.warn("Event processing failed: id={} attempt={}", event.getId(), attempt);

                        if (attempt == maxRetries) {
                            deadEvents.persistDeadEvents(event, Thread.currentThread().getName(),
                                    "Processing failed after " + maxRetries + " retries");
                        }
                    }
                }

                eventStore.store(event);
                if (success) {
                    logger.info("Event processed successfully: " + event.getId());
                } else {
                    logger.warn("Event moved to DLQ: " + event.getId());
                }

                Thread.sleep(500);
            }

        } catch (InterruptedException e) {
            logger.info(Thread.currentThread().getName() + " interrupted, exiting gracefully.");
            Thread.currentThread().interrupt();
        }
    }
}