package com.anshika.Re_EquippedEventFlowEngine.Web;

import com.anshika.Re_EquippedEventFlowEngine.ProducerQueue.EventPublisher;
import com.anshika.Re_EquippedEventFlowEngine.Web.Request.CreateEventRequest;
import com.anshika.Re_EquippedEventFlowEngine.Web.Request.EventResponse;
import com.anshika.Re_EquippedEventFlowEngine.FileStorage.EventFileInformationStore;


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
    private final EventPublisher publisher;
    private final EventFileInformationStore fileStore;
    public EventController(EventPublisher publisher, EventFileInformationStore fileStore)
    {
        this.publisher=publisher;
        this.fileStore=fileStore;
    }

    @PostMapping
    public void publish(@RequestBody CreateEventRequest request)throws InterruptedException
    {
        publisher.publish(request.getType(), request.getEventLoad());
    }

    @GetMapping
    public EventResponse getEventById(@PathVariable int id)
    {
        return fileStore.getEventById(id).map(EventResponse::new).orElseThrow(()->new RuntimeException("Event not found with id "+id));
    }
}
