package com.bivgroup.websocket.websocket.spring;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bush on 25.07.2016.
 */
@Controller
public class WebSocketController {
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(WebSocketController.class);
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/add")
    @SendTo("/topic/message")
    //@SendTo("/topic/showResult")
    public String addNum(String input) throws Exception {
        Thread.sleep(2000);
        String result = "sdsds";
        return result;
    }

    @RequestMapping("/start")
    public String start() {
        return "start";
    }


    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

    @SubscribeMapping("/topic")
    public List<String> getPositions(Principal principal) throws Exception {
        return Collections.EMPTY_LIST;
    }

    @Scheduled(fixedDelay=1500)
    public void sendNotifications() {

        Map<String, Object> map = new HashMap<>();
        map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
        logger.info("WebSocketController");
        //this.messagingTemplate.convertAndSendToUser("user", "/topic/test", "object", map);



    }
}

