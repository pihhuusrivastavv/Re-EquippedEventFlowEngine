package com.anshika.Re_EquippedEventFlowEngine.web;

import com.anshika.Re_EquippedEventFlowEngine.ProducerQueue.EventPublisher;
import com.anshika.Re_EquippedEventFlowEngine.web.request.CreateEventRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController
{
    private final EventPublisher publisher;

    public EventController(EventPublisher publisher)
    {
        this.publisher=publisher;
    }

    @PostMapping
    public void publish(@RequestBody CreateEventRequest request)throws InterruptedException
    {
        publisher.publish(request.getType(), request.getEventLoad());
    }
}
