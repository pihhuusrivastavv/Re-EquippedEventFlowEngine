package com.anshika.Re_EquippedEventFlowEngine.FileStorage;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.EventType;

import java.io.IOException;
import java.io.FileWriter;
import java.io.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.*;


@Component
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
                logger.warn("Failed to persist the event {} |  attempt {}",event,attempt);
            }
        }
        logger.warn("Not able to persist"+event+" after max tries");
        return false;
    }


    public synchronized Optional<Event>getEventById(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(File_Name))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("id" + id + ",")) {
                    return Optional.of(parseEvent(line));
                }

            }
        } catch (IOException e) {
            logger.error("Error in reading File");
        }
        return Optional.empty();
    }
        private Event parseEvent(String line)
        {
            String content=line.substring(line.indexOf("{")+1,line.indexOf("}"));

            String[] part=content.split(",");

            int id=Integer.parseInt(part[0].split("=")[1].trim());
            String eventLoad=(part[1].split("=")[1].trim());
            EventType type=EventType.valueOf(part[2].split("=")[1].trim());

            return new Event(id,eventLoad,type);

        }

    }
