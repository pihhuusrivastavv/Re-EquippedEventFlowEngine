package com.anshika.Re_EquippedEventFlowEngine.FileStorage;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.EventType;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.ConfirmedEventStore;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.EventQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventRecovery
{
    private final String file="events_info.txt";
    private final Logger logger =LoggerFactory.getLogger(EventRecovery.class);

    public void loadEvents(EventQueue queue, ConfirmedEventStore confirmStore)
    {
        try(BufferedReader reader=new BufferedReader(new FileReader(file)))
        {
            String line;
            while((line=reader.readLine())!=null)
            {
                Event event= parseEvent(line);
                if(event != null)
                {
                    logger.info("Recovered event: "+event.getId());
                    queue.publish(event);
                }
            }
        }
        catch(IOException  | InterruptedException e)
        {
            logger.info("Error during the event recovery",e);

        }
    }

    private Event parseEvent(String line)
    {
        String[] parts = line.split("\\|");

        if(parts.length < 3)
        {
            logger.warn("Skipping corrupted event line: {}", line);
            return null;
        }

        int id;
        String msg;
        EventType type;

        if(parts.length == 3)
        {
            id = Integer.parseInt(parts[0].trim());
            msg = parts[1].trim();
            type = EventType.valueOf(parts[2].trim());
        }
        else
        {
            id = Integer.parseInt(parts[1].trim());
            type = EventType.valueOf(parts[2].trim());
            msg = parts[3].trim();
        }

        return new Event(id,msg,type);
    }
}
