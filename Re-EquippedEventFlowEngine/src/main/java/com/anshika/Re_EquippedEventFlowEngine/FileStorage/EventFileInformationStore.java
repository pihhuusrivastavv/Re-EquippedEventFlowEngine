package com.anshika.Re_EquippedEventFlowEngine.FileStorage;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import java.io.IOException;
import java.io.FileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventFileInformationStore {
    private final String File_Name = "events_info";
    private final int max_retries = 3;
    private final static Logger logger= LoggerFactory.getLogger(EventFileInformationStore.class);

    public synchronized boolean persist(Event event) {
        int attempt = 0;
        while (attempt < max_retries)
        {
            try (FileWriter writer = new FileWriter(File_Name, true))
            {
                writer.write(event.toString());
                writer.write(System.lineSeparator());
                logger.info("Persisted event "+event);
                return true;
            }
            catch (IOException e)
            {
                attempt++;
                logger.warn("Failed to persist the event "+event+"| for attempt-"+attempt);
            }
        }
        logger.warn("Not able to persist event, "+event+" after max tries");
        return false;
    }
}
