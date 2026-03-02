package com.anshika.Re_EquippedEventFlowEngine.Engine;

import com.anshika.Re_EquippedEventFlowEngine.ConsumerQueue.EventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//Thread Manager for the whole EventFlowEngine.
//owns thread of consumer and producer.
//starts the thread and shuts them down gracefully.


@Component
public class EventThreadManager {
    private static final Logger logger = LoggerFactory.getLogger(EventThreadManager.class);
    private Thread[] consumerThreads;
    private Thread producerThread;

    //Starting Threads
    //Threads are generic
    public void startingEngine(Runnable producer, EventConsumer[] consumers) {
        logger.info("Starting Event Threads of Engine");

        producerThread = new Thread(producer, "EVENT-PRODUCER");
        producerThread.start();

        consumerThreads = new Thread[consumers.length];
        for (int i = 0; i < consumers.length; i++) {
            consumerThreads[i] = new Thread(consumers[i], "EVENT-CONSUMER " + i);
            consumerThreads[i].start();
        }
    }


    //Stopping Thread
    public void stopEngine() {
        logger.info("Stopping Event Threads of Engine");

        if (producerThread != null) {
            producerThread.interrupt();
        }
        if (consumerThreads != null) {
            for (Thread t : consumerThreads) {
                if (t != null) {
                    t.interrupt();
                }
            }
        }
        try {
            if (producerThread != null)
                producerThread.join();
            if (consumerThreads != null) {
                for (Thread t : consumerThreads) {
                    if (t != null)
                        t.join();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.info("All threads stopped gracefully");
    }
}
