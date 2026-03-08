package com.anshika.Re_EquippedEventFlowEngine.Service;

import com.anshika.Re_EquippedEventFlowEngine.Exception.EventErrorException;
import com.anshika.Re_EquippedEventFlowEngine.FileStorage.EventFileInformationStore;
import com.anshika.Re_EquippedEventFlowEngine.ProducerQueue.EventPublisher;
import com.anshika.Re_EquippedEventFlowEngine.Web.Response.EventResponse;

import org.springframework.stereotype.Service;

@Service
public class EventServiceImplementation implements EventService
{
    private final EventFileInformationStore file;
    private final EventPublisher publishEvent;

    public EventServiceImplementation(EventFileInformationStore file, EventPublisher publishEvent)
    {
        this.file = file;
        this.publishEvent = publishEvent;
    }
    @Override
    public void publishEvent(String type,String payload) throws InterruptedException
    {
        publishEvent.publish(type,payload);
    }

    @Override
    public EventResponse getEvent(int id)
    {
        return file.getEventById(id).map(EventResponse::new).orElseThrow(()->new EventErrorException(id));

    }
}
