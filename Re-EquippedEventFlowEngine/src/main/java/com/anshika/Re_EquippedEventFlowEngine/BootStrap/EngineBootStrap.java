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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class EngineBootStrap
{
    private static final Logger logger =
            LoggerFactory.getLogger(EngineBootStrap.class);

    private final EventQueue queue;

    private final EventRecovery recovery;

    private final ConfirmedEventStore confirmStore;

    @Value("${eventflow.consumer.thread-count}")
    private int consumerThreads;

    @Value("${eventflow.consumer.max-retries}")
    private int maxRetires;

    @Value("${eventflow.producer.event-count}")
    private int producerEventCount;

    private EventConsumer[] consumers;
    private EventProducer producer;

    private ExecutorService executor;


    public EngineBootStrap(EventQueue queue,EventRecovery recovery,ConfirmedEventStore confirmStore)
    {
        this.queue=queue;
        this.recovery=recovery;
        this.confirmStore=confirmStore;
    }

    @PostConstruct
    public void startEngine()
    {
        logger.info("Starting EventFlowEngine");
        logger.info("Config | consumers={} maxRetries={} producerEvents{}",consumerThreads,maxRetires,producerEventCount);

        recovery.loadEvents(queue, confirmStore);

        producer = new EventProducer(queue,producerEventCount);

        consumers=new EventConsumer[consumerThreads];
        for(int i=0;i<consumerThreads;i++)
        {
            consumers[i]=new EventConsumer(queue,confirmStore,new DeadAndFailedEventStore(),new EventStore(),maxRetires);
        }

        executor=java.util.concurrent.Executors.newFixedThreadPool(1+consumerThreads);
        logger.info("Executor initialized |  poolSize={}",1+consumerThreads);
        executor.submit(producer);

        for(EventConsumer c: consumers)
            executor.submit(c);

        logger.info("EventFlowEngine started successfully.");

    }

    @PreDestroy
    public void shutdownEngine()
    {

        logger.info("Shutdown initiated..");
        queue.shutDownQueue();
        logger.info("Queue Shutdown has been initiated");

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS))
            {
                logger.warn("Executor failed at terminating on time, force shutdown has been initiated.");
                executor.shutdown();
            }
        }
        catch(InterruptedException e)
        {
            logger.warn("Shutdown interrupted");
            executor.shutdown();
            Thread.currentThread().interrupt();
        }

        logger.info("Engine shutdown completed.");
    }

}
