package com.anshika.Re_EquippedEventFlowEngine.Web;

import com.anshika.Re_EquippedEventFlowEngine.Web.Request.CreateEventRequest;
import com.anshika.Re_EquippedEventFlowEngine.Web.Response.EventResponse;
import com.anshika.Re_EquippedEventFlowEngine.Service.EventService;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/events")
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
    @GetMapping
    public List<EventResponse> getAllEvents()
    {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable int id)
    {
        return eventService.getEvent(id);
    }

    @DeleteMapping("/id")
    public void deleteEventById(@PathVariable int id)
    {
        eventService.deleteEvent(id);
    }
}
