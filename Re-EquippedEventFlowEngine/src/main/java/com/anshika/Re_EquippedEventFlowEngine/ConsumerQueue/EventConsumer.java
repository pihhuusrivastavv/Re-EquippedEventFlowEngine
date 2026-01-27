package com.anshika.Re_EquippedEventFlowEngine.ConsumerQueue;

import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.EventQueue;
import com.anshika.Re_EquippedEventFlowEngine.DeadLetterQueue.DeadAndFailedEventStore;
import java.util.Random;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.ConfirmedEventStore;
import com.anshika.Re_EquippedEventFlowEngine.StorageEvents.EventStore;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class EventConsumer implements Runnable
{

    private final EventQueue queue;
    private final DeadAndFailedEventStore deadEvents;
    private final EventStore eventStore;
    private final Random random=new Random();
    private final int maxRetries;
    private static final Logger logger=LoggerFactory.getLogger(EventConsumer.class);

    private final ConfirmedEventStore confirmStore;

    public EventConsumer(EventQueue queue ,ConfirmedEventStore confirmStore,DeadAndFailedEventStore deadEvents,EventStore eventStore,int maxRetries)
    {
        this.queue=queue;
        this.confirmStore=confirmStore;
        this.deadEvents=deadEvents;
        this.eventStore=eventStore;
        this.maxRetries=maxRetries;
    }
    @Override
    public void run()
    {
        try
        {
            while(!Thread.currentThread().isInterrupted())
            {
                Event event = queue.consume();
                if(event==null)
                {
                    logger.info("No events present");
                    break;
                }

                if(confirmStore.isConfirmed(event.getId()))
                {
                    logger.info("Skipping already confirmed Event "+event.getId());
                    continue;
                }
                int attempt=0;
                boolean success=false;
                while(attempt<maxRetries && !success) {
                    logger.info("Checking Event");
                    try {
                        if (random.nextInt(5) == 0) {
                            logger.error("Error");
                        }

                        logger.info(Thread.currentThread().getName() + " processed " + event.getType() + ":" + event.getMessage());

                        confirmStore.confirmedEvent(event.getId());
                        logger.info("Confirmed Event found");
                        success=true;

                    } catch (Exception e) {
                        attempt++;
                        logger.warn(Thread.currentThread().getName() + "Event type failed processing at attempts | type={} attempt={} ",event.getType(), attempt);

                        if (attempt == maxRetries)
                        {
                            deadEvents.persistDeadEvents(event, Thread.currentThread().getName(), "Processing failed after " + maxRetries + " retries");
                        }
                    }
                }
                eventStore.store(event);
                logger.info("Event Persisted and stored");
                if (success)
                {
                    logger.info("Event processed successfully: "+event.getId());
                }
                else
                {
                    logger.warn("Event moved to DLQ: "+event.getId());
                }

                Thread.sleep(500);
            }
            logger.info(Thread.currentThread().getName()+ " exited gracefully.");
        }
        catch(InterruptedException e)
        {
            logger.info(Thread.currentThread().getName()+ " interrupted, exiting. ");
            Thread.currentThread().interrupt();
        }
    }

}
