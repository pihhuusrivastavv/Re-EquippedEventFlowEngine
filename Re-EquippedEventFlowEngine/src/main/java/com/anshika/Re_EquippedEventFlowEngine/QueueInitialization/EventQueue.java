package com.anshika.Re_EquippedEventFlowEngine.QueueInitialization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.LinkedList;
import java.util.Queue;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import java.util.logging.Logger;


@Component
public class EventQueue
{
    private final Queue<Event>queue =new LinkedList<>();
    private final int capacity;
    private boolean shutdown=false;
    private static final Logger logger=Logger.getLogger(EventQueue.class.getName());

    public EventQueue(
            @Value("${eventflow.queue.capacity}")
            int capacity
    )
    {
        this.capacity=capacity;
    }
    public synchronized void publish(Event event) throws InterruptedException
    {
        while(queue.size()==capacity)
        {
            wait();
        }
        queue.add(event);
        logger.info("Published event: "+ event.getMessage());
        notifyAll();
    }
    public synchronized  Event consume() throws InterruptedException
    {
        while(queue.isEmpty() && !shutdown)
        {
            wait();
        }
        if(queue.isEmpty() && shutdown)
        {
            return null;
        }
        Event event=queue.poll();
        notifyAll();
        return event;
    }
    public synchronized void shutDownQueue()
    {
        shutdown=true;
        notifyAll();
    }
}
