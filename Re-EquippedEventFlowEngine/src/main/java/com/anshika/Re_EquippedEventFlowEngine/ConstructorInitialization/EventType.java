package com.anshika.Re_EquippedEventFlowEngine.ConstructorInitialization;

public enum EventType
{
    INFO(1),
    WARNING(2),
    SEVERE(3);
    private final int priority;
    EventType( int priority)
    {
        this.priority=priority;
    }
    public int getPriority()
    {
        return priority;
    }
}
