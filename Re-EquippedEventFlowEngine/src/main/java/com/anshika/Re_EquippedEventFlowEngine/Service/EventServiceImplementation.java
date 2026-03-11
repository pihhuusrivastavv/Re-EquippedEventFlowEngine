package com.anshika.Re_EquippedEventFlowEngine.Service;

import com.anshika.Re_EquippedEventFlowEngine.Exception.EventErrorException;
import com.anshika.Re_EquippedEventFlowEngine.FileStorage.EventFileInformationStore;
import com.anshika.Re_EquippedEventFlowEngine.ProducerQueue.EventPublisher;
import com.anshika.Re_EquippedEventFlowEngine.Web.Response.EventResponse;
import com.anshika.Re_EquippedEventFlowEngine.Repository.EventRepository;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImplementation implements EventService
{
    private final EventFileInformationStore file;
    private final EventPublisher publishEvent;
    private final EventRepository eventRepository;


    public EventServiceImplementation(EventFileInformationStore file, EventPublisher publishEvent,EventRepository eventRepository)
    {
        this.file = file;
        this.publishEvent = publishEvent;
        this.eventRepository = eventRepository;
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

    @Override
    @Transactional
    public List<EventResponse> getAllEvents()
    {
        return eventRepository.findAll().stream().map(EventResponse::new).toList();
    }

    @Override
    @Transactional
    public void deleteEvent(int id)
    {
        if(!eventRepository.existsById(id))
        {
            throw new EventErrorException(id);
        }
        eventRepository.deleteById(id);
    }
}
