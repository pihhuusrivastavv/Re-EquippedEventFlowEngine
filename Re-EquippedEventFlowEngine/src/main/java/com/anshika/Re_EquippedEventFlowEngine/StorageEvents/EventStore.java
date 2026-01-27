package com.anshika.Re_EquippedEventFlowEngine.StorageEvents;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import java.io.IOException;
import java.io.FileWriter;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventStore
{
    private static final String file="events.info";
    private final Logger logger=LoggerFactory.getLogger(EventStore.class);
    public synchronized void store(Event event)
    {
        try(FileWriter writer=new FileWriter(file,true))
        {
            String record= LocalDateTime.now()+" | "+event.getId()+" | "+event.getType()+" | "+event.getMessage()+"\n";
            writer.write(record);
            logger.info("Event record written");
        }
        catch(IOException e)
        {
            logger.error("Failed to submit a confirmed event");
        }
    }
}
