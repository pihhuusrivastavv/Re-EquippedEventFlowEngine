/*package com.anshika.Re_EquippedEventFlowEngine.Engine;

import com.anshika.Re_EquippedEventFlowEngine.ConsumerQueue.EventConsumer;


public class MultiConsumerRunnable implements Runnable
{
    private final EventConsumer[] consumers;

    public MultiConsumerRunnable(EventConsumer[] consumers)
    {
        this.consumers=consumers;
    }
    @Override
    public void run()
    {
        for(EventConsumer consumer:consumers)
        {
            consumer.run();
        }
    }
}

 */