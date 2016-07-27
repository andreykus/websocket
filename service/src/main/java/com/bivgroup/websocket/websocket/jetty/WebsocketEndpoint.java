/*
    Copyright 2014 Benjamin Black

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.bivgroup.websocket.websocket.jetty;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;


/**
 * Process Context endpoin websocket
 */

@ServerEndpoint(
        value = Constants.API_CONTEXT,
        subprotocols = {"kafka-text", "kafka-binary"},
        decoders = {BinaryMessage.BinaryMessageDecoder.class, TextMessage.TextMessageDecoder.class},
        encoders = {BinaryMessage.BinaryMessageEncoder.class, TextMessage.TextMessageEncoder.class},
        configurator = WebsocketEndpoint.Configurator.class
)
public class WebsocketEndpoint {

    private static Logger logger = LogManager.getLogger(WebsocketEndpoint.class);

    /**
     * Parser param context to map
     */
    public static Map<String, String> getQueryMap(String query) {
        Map<String, String> map = Maps.newHashMap();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] nameval = param.split("=");
                map.put(nameval[0], nameval[1]);
            }
        }
        return map;
    }

    /**
     * Where connect to socket from external client
     */
    @OnOpen
    @SuppressWarnings("unchecked")
    public void onOpen(final Session session) {
        String groupId = "";
        String topics = "";
        logger.debug("Opening new session {}", session.getId());
        Map<String, String> queryParams = getQueryMap(session.getQueryString());
        if (queryParams.containsKey("group.id")) {
            groupId = queryParams.get("group.id");
            logger.debug("Session {} group.id are {}", session.getId(), groupId);
        }
        if (queryParams.containsKey("topics")) {
            topics = queryParams.get("topics");
            logger.debug("Session {} topics are {}", session.getId(), topics);
            //TODO Insert Consummer submit method
        }
    }

    /**
     * Where close session
     */
    @OnClose
    public void onClose(final Session session) {
        //TODO Close Producer
        //TODO Close Consummer
    }

    /**
     * Where message recived
     */
    @OnMessage
    public void onMessage(final BinaryMessage message, final Session session) {
        logger.debug("BinaryMessage Received binary message: topic - {}; message - {}",
                message.getTopic(), message.getMessage());
        //TODO Insert Producer send method
        RemoteEndpoint.Async remoteEndpoint= session.getAsyncRemote();
        remoteEndpoint.sendObject(message);
    }

    /**
     * Where message recived
     */
    @OnMessage
    public void onMessage(final TextMessage message, final Session session) {
        logger.debug("TextMessage Received binary message: topic - {}; message - {}",
                message.getTopic(), message.getMessage());
        //TODO Insert Producer
        //TODO replace This to Consumer Processor init

        //this echo
        RemoteEndpoint.Async remoteEndpoint= session.getAsyncRemote();
        remoteEndpoint.sendObject(message);
    }


    private void closeSession(Session session, CloseReason reason) {
        //session.close(new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, e.getMessage()));
        try {
            session.close(reason);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configurate endPoint
     */
    public static class Configurator extends ServerEndpointConfig.Configurator {

        @Override
        public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
            T endpoint = super.getEndpointInstance(endpointClass);

            if (endpoint instanceof WebsocketEndpoint) {
                return endpoint;
            }
            throw new InstantiationException(
                    MessageFormat.format("Expected instanceof \"{0}\". Got instanceof \"{1}\".",
                            WebsocketEndpoint.class, endpoint.getClass()));
        }
    }
}

