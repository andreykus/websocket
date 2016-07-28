package com.bivgroup.websocket.websocket.spring;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by bush on 25.07.2016.
 */
@Service
public class ShedulerMessageService implements ApplicationListener<BrokerAvailabilityEvent> {

    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(ShedulerMessageService.class);
    private final MessageSendingOperations<String> messagingTemplate;
    private AtomicBoolean brokerAvailable = new AtomicBoolean();

    @Autowired
    public ShedulerMessageService(MessageSendingOperations<String> messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
        this.brokerAvailable.set(event.isBrokerAvailable());
    }

    @Scheduled(fixedDelay=1000)
    public void sendNotifications() {
       // for (Quote quote : this.quoteGenerator.generateQuotes()) {
          //  String destination = "/topic/price.stock." + quote.getTicker();
        //    this.messagingTemplate.convertAndSend(destination, quote);

        if (this.brokerAvailable.get()) {
            logger.info("ShedulerMessageService");
            this.messagingTemplate.convertAndSend("/topic/test." + "1", "");
        }

        }

}