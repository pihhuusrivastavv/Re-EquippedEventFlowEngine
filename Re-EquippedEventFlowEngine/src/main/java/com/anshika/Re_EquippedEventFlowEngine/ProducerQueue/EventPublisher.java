package com.anshika.Re_EquippedEventFlowEngine.ProducerQueue;

import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.EventType;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.EventQueue;

import org.springframework.stereotype.Component;


@Component
public class EventPublisher
{
    private final EventQueue queue;
    private static int eventIdCounter=1;

    public EventPublisher(EventQueue queue)
    {
        this.queue=queue;
    }

    public void publish(String type,String eventLoad) throws InterruptedException
    {
        Event event =new Event(eventIdCounter++,eventLoad,EventType.valueOf(type));
        queue.publish(event);
    }
}
