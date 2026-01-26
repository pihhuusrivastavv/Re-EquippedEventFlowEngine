package com.anshika.Re_EquippedEventFlowEngine.QueueInitialization;
import java.util.HashSet;
import java.util.Set;

public class ConfirmedEventStore
{
    private final Set<Integer>confirmedEventIds=new HashSet<>();
    public synchronized void confirmedEvent(int eventId)
    {
        confirmedEventIds.add(eventId);
    }
    public synchronized boolean isConfirmed(int eventId)
    {
        return confirmedEventIds.contains(eventId);
    }
}
