package com.anshika.Re_EquippedEventFlowEngine.ProducerQueue;
import  com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.EventType;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.EventQueue;
import java.util.Random;
import org.slf4j.Logger;
import com.anshika.Re_EquippedEventFlowEngine.FileStorage.EventFileInformationStore;
import org.slf4j.LoggerFactory;


public class EventProducer implements Runnable
{

    private final EventQueue queue;
    private final Random random= new Random();
    private int eventNumber=1;
    private final EventFileInformationStore file_info;
    private static final Logger logger= LoggerFactory.getLogger(EventProducer.class);
    private final int totalEvents;

    public EventProducer(EventQueue queue,int totalEvents)
    {
        this.queue=queue;
        this.totalEvents=totalEvents;
        this.file_info=new EventFileInformationStore();
    }
    @Override
    public void run()
    {
        try
        {
            while(eventNumber<=totalEvents)
            {
                EventType type = EventType.values()[random.nextInt(EventType.values().length)];
                Event event =new Event(eventNumber," Event- "+eventNumber,type);
                if(file_info.persist(event))
                {
                    queue.publish(event);
                    logger.info("Produced Event | type={} message={}",event.getType(),event.getMessage());
                }
                else
                {
                    logger.error("Event Dropped due to persistence: "+event);
                }
                eventNumber++;
                Thread.sleep(500);
            }

            queue.shutDownQueue();
            logger.info("Queue shutdown has been initiated");
        }
        catch(InterruptedException e)
        {
            logger.warn("Producer interrupted,exiting...");
            Thread.currentThread().interrupt();
        }
    }
}
