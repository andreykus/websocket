package com.bivgroup.websocket.websocket.spring;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by bush on 25.07.2016.
 */
@Service
public class QuoteService {

    //private final MessageSendingOperations<String> messagingTemplate;

    @Scheduled(fixedDelay=1000)
    public void sendQuotes() {
       // for (Quote quote : this.quoteGenerator.generateQuotes()) {
          //  String destination = "/topic/price.stock." + quote.getTicker();
        //    this.messagingTemplate.convertAndSend(destination, quote);
        }

}