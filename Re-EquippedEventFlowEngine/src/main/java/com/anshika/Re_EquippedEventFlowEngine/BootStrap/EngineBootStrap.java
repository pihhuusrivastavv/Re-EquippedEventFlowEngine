package com.anshika.Re_EquippedEventFlowEngine.BootStrap;
import com.anshika.Re_EquippedEventFlowEngine.FileStorage.EventRecovery;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.EventQueue;
import com.anshika.Re_EquippedEventFlowEngine.QueueInitialization.ConfirmedEventStore;
import com.anshika.Re_EquippedEventFlowEngine.ProducerQueue.EventProducer;
import com.anshika.Re_EquippedEventFlowEngine.ConsumerQueue.EventConsumer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;


@Component
public class EngineBootStrap
{
    private static final Logger logger =
            Logger.getLogger(EngineBootStrap.class.getName());

    private EventQueue queue;
    private EventProducer producer;
    private EventConsumer consumer1;
    private EventConsumer consumer2;

    @PostConstruct
    public void startEngine() throws InterruptedException {

        queue = new EventQueue();
        EventRecovery recovery = new EventRecovery();
        ConfirmedEventStore confirmStore = new ConfirmedEventStore();

        recovery.loadEvents(queue, confirmStore);

        producer = new EventProducer(queue);
        consumer1 = new EventConsumer(queue, confirmStore);
        consumer2 = new EventConsumer(queue, confirmStore);

        producer.setName("Producer");
        consumer1.setName("Consumer-1");
        consumer2.setName("Consumer-2");

        consumer1.setDaemon(true);

        producer.start();
        consumer1.start();
        consumer2.start();

        logger.info("Event engine started successfully.");
    }

    @PreDestroy
    public void shutdownEngine() {

        logger.info("Shutdown initiated..");

        producer.interrupt();
        queue.shutDownQueue();

        logger.info("Engine shutdown completed.");
    }

}
