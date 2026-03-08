package com.anshika.Re_EquippedEventFlowEngine.Exception;

public class EventErrorException extends RuntimeException
{
    public EventErrorException(int id)
    {
        super("Event not found with id : "+id);
    }
}
