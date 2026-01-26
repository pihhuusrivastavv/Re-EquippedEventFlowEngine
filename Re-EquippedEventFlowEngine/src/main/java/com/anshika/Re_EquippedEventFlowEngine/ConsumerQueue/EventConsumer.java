package com.anshika.Re_EquippedEventFlowEngine.ConsumerQueue;

import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.EventQueue;
import com.anshika.Re_EquippedEventFlowEngine.DeadLetterQueue.DeadAndFailedEventStore;
import java.util.Random;
import java.util.logging.Logger;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.ConfirmedEventStore;
import com.anshika.Re_EquippedEventFlowEngine.StorageEvents.EventStore;

public class EventConsumer extends Thread
{

    private final EventQueue queue;
    private final Random random=new Random();
    private static final int max_retries=3;
    private static final Logger logger=Logger.getLogger(EventConsumer.class.getName());
    private final DeadAndFailedEventStore deadEvents=new DeadAndFailedEventStore();
    private final ConfirmedEventStore confirmStore;
    private final EventStore eventStore=new EventStore();

    public EventConsumer(EventQueue queue ,ConfirmedEventStore confirmStore)
    {
        this.queue=queue;
        this.confirmStore=confirmStore;
    }
    @Override
    public void run()
    {
        try
        {
            while(true)
            {
                Event event = queue.consume();
                if(event==null)
                    break;
                if(confirmStore.isConfirmed(event.getId()))
                {
                    logger.info("Skipping already confirmed Event "+event.getId());
                    continue;
                }
                int attempt=0;
                boolean success=false;
                while(attempt<max_retries && !success) {
                    try {
                        if (random.nextInt(5) == 0)
                            throw new RuntimeException("Random error...");

                        logger.info(Thread.currentThread().getName() + " processed " + event.getType() + ":" + event.getMessage());
                        success = true;

                        confirmStore.confirmedEvent(event.getId());
                        logger.info("Confirmation received for Event " + event.getId());
                    } catch (Exception e) {
                        attempt++;
                        logger.warning(Thread.currentThread().getName() + " failed processing " + event.getType() + ":" + "| attempt " + attempt);

                        if (attempt == max_retries) {
                            logger.severe(Thread.currentThread().getName() + " sending to DLQ file " + event.getType() + ":" + event.getMessage());
                            deadEvents.persistDeadEvents(event, Thread.currentThread().getName(), "Processing failed after " + max_retries + " retries");
                        }
                    }
                }
                if (success)
                {
                    eventStore.store(event);
                    logger.info("Event processed successfully: "+event.getId());
                }
                else
                {
                    eventStore.store(event);
                    deadEvents.persistDeadEvents(event,Thread.currentThread().getName()," Failed after: "+max_retries+" retries");
                    logger.severe("Event moved to DLQ: "+event.getId());
                }

                Thread.sleep(500);
            }
            logger.info(Thread.currentThread().getName()+ " exited gracefully.");
        }
        catch(InterruptedException e)
        {
            logger.info(Thread.currentThread().getName()+ " interrupted, exiting. ");
        }
    }

}
