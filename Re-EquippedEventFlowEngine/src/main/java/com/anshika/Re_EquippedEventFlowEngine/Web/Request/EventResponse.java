package com.anshika.Re_EquippedEventFlowEngine.Web.Request;

import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.Event;
import com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization.EventType;

public class EventResponse
{
    private long id;
    private String eventLoad;
    private String type;


    public EventResponse(Event event)
    {
        this.id=event.getId();
        this.eventLoad=event.getMessage();
        this.type=event.getType().name();
    }

    public long getId()
    {
        return id;
    }

    public String getMsg()
    {
        return eventLoad;
    }

    public String getType()
    {
        return type;
    }
}
