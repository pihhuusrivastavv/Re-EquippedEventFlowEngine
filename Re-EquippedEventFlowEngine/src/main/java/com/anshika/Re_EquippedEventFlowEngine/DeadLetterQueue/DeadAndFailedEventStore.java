package com.anshika.Re_EquippedEventFlowEngine.DeadLetterQueue;

import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.EventType;
import java.io.IOException;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class DeadAndFailedEventStore
{
    private static final String file_name="Dead.events.log";
    private static final Logger logger=Logger.getLogger(DeadAndFailedEventStore.class.getName());
    public synchronized void persistDeadEvents(Event event,String customerName,String reason)
    {
        try(FileWriter writer=new FileWriter(file_name,true))
        {
            String record=LocalDateTime.now()+" | "+customerName+" | "+event.getType()+" | "+event.getId()+" | "+event.getMessage()+" | "+reason;
            writer.write(record);
            writer.write(System.lineSeparator());
            logger.warning("Event "+event.getId()+" moved to DLQ ");
        }
        catch(IOException e)
        {
            logger.severe("Failed to write to DLQ: "+event.getType());
        }
    }
}
