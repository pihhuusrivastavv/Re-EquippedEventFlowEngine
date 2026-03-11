package com.anshika.Re_EquippedEventFlowEngine.Service;

import com.anshika.Re_EquippedEventFlowEngine.Web.Response.EventResponse;
import java.util.List;

public interface  EventService
{
    void publishEvent(String type,String Payload) throws InterruptedException;
    EventResponse getEvent(int id);
    List<EventResponse> getAllEvents();

    void deleteEvent(int id);
}
