package com.anshika.Re_EquippedEventFlowEngine.BootStrap;

import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.EventQueue;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.ConfirmedEventStore;
import com.anshika.Re_EquippedEventFlowEngine.FileStorage.EventRecovery;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EngineConfig
{
    @Bean
    public EventQueue eventQueue(@Value("${eventflow.queue.capacity}") int capacity)
    {
        return new EventQueue(capacity);
    }

    @Bean
    public ConfirmedEventStore confirmedEventStore()
    {
        return new ConfirmedEventStore();
    }

    @Bean
    public EventRecovery eventrecovery()
    {
        return new EventRecovery();
    }
}