package com.anshika.Re_EquippedEventFlowEngine.BootStrap;

import com.anshika.Re_EquippedEventFlowEngine.BootStrap.EngineBootStrap;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class EngineStarter
{
    private final EngineBootStrap engineBootStrap;

    public EngineStarter(EngineBootStrap engineBootStrap)
    {
        this.engineBootStrap = engineBootStrap;
    }
    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void onReady()
    {
        engineBootStrap.startEngine();
    }

}
