package com.anshika.Re_EquippedEventFlowEngine.BootStrap;
import com.anshika.Re_EquippedEventFlowEngine.DeadLetterQueue.DeadAndFailedEventStore;
import com.anshika.Re_EquippedEventFlowEngine.FileStorage.EventRecovery;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.EventQueue;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.ConfirmedEventStore;
import com.anshika.Re_EquippedEventFlowEngine.ProducerQueue.EventProducer;
import com.anshika.Re_EquippedEventFlowEngine.ConsumerQueue.EventConsumer;
import com.anshika.Re_EquippedEventFlowEngine.StorageEvents.EventStore;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


@Component
public class EngineBootStrap
{
    private static final Logger logger =
            Logger.getLogger(EngineBootStrap.class.getName());

    @Autowired
    private EventQueue queue;

    @Autowired
    private EventRecovery recovery;

    @Autowired
    private ConfirmedEventStore confirmStore;


    private EventConsumer consumer1;
    private EventConsumer consumer2;
    private EventProducer producer;

    private ExecutorService executor;


    @PostConstruct
    public void startEngine() throws InterruptedException
    {

        recovery.loadEvents(queue, confirmStore);

        producer = new EventProducer(queue);

        consumer1=new EventConsumer(queue,confirmStore,new DeadAndFailedEventStore(),new EventStore());
        consumer2=new EventConsumer(queue,confirmStore,new DeadAndFailedEventStore(),new EventStore());

        executor=java.util.concurrent.Executors.newFixedThreadPool(3);
        executor.submit(producer);
        executor.submit(consumer1);
        executor.submit(consumer2);


        logger.info("Event engine started successfully.");
    }

    @PreDestroy
    public void shutdownEngine()
    {

        logger.info("Shutdown initiated..");

        queue.shutDownQueue();

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdown();
            }
        }
        catch(InterruptedException e)
        {
            executor.shutdown();
            Thread.currentThread().interrupt();
        }

        logger.info("Engine shutdown completed.");
    }

}
