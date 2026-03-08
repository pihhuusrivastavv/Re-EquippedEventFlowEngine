package com.anshika.Re_EquippedEventFlowEngine.Web;

import com.anshika.Re_EquippedEventFlowEngine.ProducerQueue.EventPublisher;
import com.anshika.Re_EquippedEventFlowEngine.Web.Request.CreateEventRequest;
import com.anshika.Re_EquippedEventFlowEngine.Web.Request.EventResponse;
import com.anshika.Re_EquippedEventFlowEngine.FileStorage.EventFileInformationStore;
import com.anshika.Re_EquippedEventFlowEngine.Service.EventService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController
{
   private final EventService eventService;
    public EventController(EventService eventService)
    {
        this.eventService=eventService;
    }


    @PostMapping
    public void publish(@RequestBody CreateEventRequest request)throws InterruptedException
    {
        eventService.publishEvent(request.getType(), request.getEventLoad());
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable int id)
    {
        return eventService.getEvent(id);
    }
}
