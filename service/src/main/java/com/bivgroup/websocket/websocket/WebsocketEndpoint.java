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

package com.bivgroup.websocket.websocket;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

@ServerEndpoint(
    value = "/v2/broker/",
    subprotocols = {"kafka-text", "kafka-binary"},
    decoders = {BinaryMessage.BinaryMessageDecoder.class},
    encoders = {BinaryMessage.BinaryMessageEncoder.class},
    configurator = WebsocketEndpoint.Configurator.class
)
public class WebsocketEndpoint {


    private static Logger logger = LogManager.getLogger( WebsocketEndpoint.class);

    public static Map<String, String> getQueryMap(String query)
    {
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



    @OnOpen
    @SuppressWarnings("unchecked")
    public void onOpen(final Session session) {
        String groupId = "";
        String topics = "";

        Map<String, String> queryParams = getQueryMap(session.getQueryString());
        if (queryParams.containsKey("group.id")) {
            groupId = queryParams.get("group.id");
        }

        logger.debug("Opening new session {}", session.getId());
        if (queryParams.containsKey("topics")) {
            topics = queryParams.get("topics");
            logger.debug("Session {} topics are {}", session.getId(), topics);

        }
    }

    @OnClose
    public void onClose(final Session session) {

    }

    @OnMessage
    public void onMessage(final BinaryMessage message, final Session session) {
        logger.trace("Received binary message: topic - {}; message - {}",
                  message.getTopic(), message.getMessage());

    }


    private void closeSession(Session session, CloseReason reason) {
        try {
            session.close(reason);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Configurator extends ServerEndpointConfig.Configurator
    {

        @Override
        public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException
        {
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

