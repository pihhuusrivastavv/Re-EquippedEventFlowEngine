package com.anshika.Re_EquippedEventFlowEngine.StorageEvents;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import java.io.IOException;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class EventStore
{
    private static final String file="events.info";
    private final Logger logger=Logger.getLogger(EventStore.class.getName());
    public synchronized void store(Event event)
    {
        try(FileWriter writer=new FileWriter(file,true))
        {
            String record= LocalDateTime.now()+" | "+event.getId()+" | "+event.getType()+" | "+event.getMessage()+"\n";
            writer.write(record);
        }
        catch(IOException e)
        {
            logger.severe("Failed to submit a confirmed event");
        }
    }
}
