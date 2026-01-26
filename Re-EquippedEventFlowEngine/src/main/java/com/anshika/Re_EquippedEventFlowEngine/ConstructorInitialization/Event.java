package com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization;
import java.lang.Comparable;

public class Event implements Comparable<Event>
{
    private final int id;
    private final String msg;
    private final EventType type;

    public Event(int id, String msg,EventType type)
    {
        this.id=id;
        this.msg=msg;
        this.type=type;
    }
    public int getId()
    {
        return id;
    }
    public String getMessage()
    {
        return msg;
    }
    public EventType getType()
    {
        return type;
    }
    @Override
    public int compareTo(Event otherEvent)
    {
        return Integer.compare(otherEvent.type.getPriority(),this.type.getPriority());
    }
    public String toString()
    {
        return id+"|"+msg;
    }
}
