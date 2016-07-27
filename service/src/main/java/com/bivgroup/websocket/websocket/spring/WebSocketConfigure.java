package com.bivgroup.websocket.websocket.spring;

import org.apache.logging.log4j.LogManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by bush on 25.07.2016.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfigure extends AbstractWebSocketMessageBrokerConfigurer {
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(WebSocketConfigure.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/api");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        SockJsServiceRegistration js =registry.addEndpoint("/sockJS").withSockJS();
    }
}
