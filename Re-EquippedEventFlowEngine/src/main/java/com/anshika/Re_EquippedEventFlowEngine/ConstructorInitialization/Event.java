package com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name="events")
public class Event implements Comparable<Event>
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String msg;

    @Enumerated(EnumType.STRING)
    private EventType type;

    public Event()
    {
    }

    public Event(int id,String msg,EventType type)
    {
        this.id=id;
        this.msg=msg;
        this.type=type;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id=id;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg=msg;
    }

    public EventType getType()
    {
        return type;
    }

    public void setType(EventType type)
    {
        this.type=type;
    }

    @Override
    public int compareTo(Event e)
    {
        return Integer.compare(e.type.getPriority(),this.type.getPriority());
    }

    public String toString()
    {
        return id+ " | "+msg;
    }
}