package com.anshika.Re_EquippedEventFlowEngine.StorageEvents;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import com.anshika.Re_EquippedEventFlowEngine.Repository.EventRepository;

import java.io.IOException;
import java.io.FileWriter;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventStore
{
    private static final String file="events.info";
    private final Logger logger=LoggerFactory.getLogger(EventStore.class);
    private final EventRepository eventRepository;
    public EventStore(EventRepository eventRepository)
    {
        this.eventRepository=eventRepository;

    }
    public synchronized void store(Event event)
    {
        eventRepository.save(event);
        logger.info("Event stored to Database");

        try(FileWriter writer=new FileWriter(file,true))
        {
            String record= LocalDateTime.now()+" | "+event.getId()+" | "+event.getType()+" | "+event.getMsg()+"\n";
            writer.write(record);
            logger.info("Event record written");
        }
        catch(IOException e)
        {
            logger.error("Failed to submit a confirmed event",e);
        }

    }
}
