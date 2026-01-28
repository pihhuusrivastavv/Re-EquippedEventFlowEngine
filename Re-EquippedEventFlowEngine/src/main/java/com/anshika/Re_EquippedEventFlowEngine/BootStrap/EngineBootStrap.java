package com.anshika.Re_EquippedEventFlowEngine.BootStrap;
import com.anshika.Re_EquippedEventFlowEngine.DeadLetterQueue.DeadAndFailedEventStore;
import com.anshika.Re_EquippedEventFlowEngine.FileStorage.EventRecovery;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.EventQueue;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.ConfirmedEventStore;
import com.anshika.Re_EquippedEventFlowEngine.ProducerQueue.EventProducer;
import com.anshika.Re_EquippedEventFlowEngine.ConsumerQueue.EventConsumer;
import com.anshika.Re_EquippedEventFlowEngine.StorageEvents.EventStore;
import com.anshika.Re_EquippedEventFlowEngine.Engine.EventThreadManager;
//import com.anshika.Re_EquippedEventFlowEngine.Engine.MultiConsumerRunnable;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    private final EventThreadManager threadManager;

    @Value("${eventflow.consumer.thread-count}")
    private int consumerThreads;

    @Value("${eventflow.consumer.max-retries}")
    private int maxRetires;

    @Value("${eventflow.producer.event-count}")
    private int producerEventCount;

    private EventConsumer[] consumers;
    private EventProducer producer;


    public EngineBootStrap(EventQueue queue,EventRecovery recovery,ConfirmedEventStore confirmStore,EventThreadManager threadManager)
    {
        this.queue=queue;
        this.recovery=recovery;
        this.confirmStore=confirmStore;
        this.threadManager=threadManager;
    }

    @PostConstruct
    public void startEngine()
    {
        logger.info("Starting EventFlowEngine...\n");
        logger.info("Config | consumers= {} maxRetries= {} producerEvents= {} \n" ,consumerThreads,maxRetires,producerEventCount);

        recovery.loadEvents(queue, confirmStore);

        producer = new EventProducer(queue,producerEventCount);

        consumers=new EventConsumer[consumerThreads];

        for(int i=0;i<consumerThreads;i++)
        {
            consumers[i]=new EventConsumer(queue,confirmStore,new DeadAndFailedEventStore(),new EventStore(),maxRetires);
        }
        threadManager.startingEngine(producer, consumers);

        logger.info("EventFlowEngine started successfully.");

    }

    @PreDestroy
    public void shutdownEngine()
    {

        logger.info("Shutdown initiated..");
        queue.shutDownQueue();
        logger.info("Queue Shutdown has been initiated");

        threadManager.stopEngine();

        logger.info("Engine shutdown completed.");
    }

}
