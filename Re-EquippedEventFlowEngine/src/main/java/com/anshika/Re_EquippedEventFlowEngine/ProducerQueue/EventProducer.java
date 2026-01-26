package com.anshika.Re_EquippedEventFlowEngine.ProducerQueue;
import  com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.EventType;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.EventQueue;
import java.util.Random;
import java.util.logging.Logger;
import com.anshika.Re_EquippedEventFlowEngine.FileStorage.EventFileInformationStore;

public class EventProducer extends Thread
{
    private final EventQueue queue;
    private final Random random= new Random();
    private int eventNumber=1;
    private final EventFileInformationStore file_info;
    private static final Logger logger=Logger.getLogger(EventProducer.class.getName());

    public EventProducer(EventQueue queue)
    {
        this.queue=queue;
        this.file_info=new EventFileInformationStore();
    }
    @Override
    public void run()
    {
        try
        {
            while(eventNumber<=10)
            {
                EventType type = EventType.values()[random.nextInt(EventType.values().length)];
                Event event =new Event(eventNumber," Event- "+eventNumber,type);
                if(file_info.persist(event))
                {
                    queue.publish(event);
                    logger.info("Produced Event: "+event.getType()+":"+event.getMessage());
                }
                else
                {
                    logger.severe("Event Dropped due to persistence: "+event);
                }
                eventNumber++;
                Thread.sleep(500);
            }
            queue.shutDownQueue();
        }
        catch(InterruptedException e)
        {
            logger.warning("Producer interrupted,exiting...");
        }
    }
}
