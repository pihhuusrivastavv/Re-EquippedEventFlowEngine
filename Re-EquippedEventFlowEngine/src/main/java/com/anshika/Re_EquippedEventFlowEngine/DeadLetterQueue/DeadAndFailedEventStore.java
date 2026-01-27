package com.anshika.Re_EquippedEventFlowEngine.DeadLetterQueue;

import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.EventType;
import java.io.IOException;
import java.io.FileWriter;
import java.time.LocalDateTime;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class DeadAndFailedEventStore
{
    private static final String file_name="Dead.events.log";
    private static final Logger logger=LoggerFactory.getLogger(DeadAndFailedEventStore.class);
    public synchronized void persistDeadEvents(Event event,String customerName,String reason)
    {
        try(FileWriter writer=new FileWriter(file_name,true))
        {
            String record=LocalDateTime.now()+" | "+customerName+" | "+event.getType()+" | "+event.getId()+" | "+event.getMessage()+" | "+reason;
            logger.info("Record has been written into the file");
            writer.write(record);
            writer.write(System.lineSeparator());
            logger.warn("Event moving to DLQ "+event.getId());
        }
        catch(IOException e)
        {
            logger.error("Failed to write to DLQ "+event.getType());
        }
    }
}
