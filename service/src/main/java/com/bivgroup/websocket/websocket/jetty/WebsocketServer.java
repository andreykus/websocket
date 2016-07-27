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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;
import java.util.Properties;

/**
 * Class WebsocketServer Server on jetty servlet server
 */
public class WebsocketServer {
    private static Logger logger = LogManager.getLogger(WebsocketServer.class);

    private final Properties wsProps;

    public WebsocketServer(Properties wsProps) {
        this.wsProps = wsProps;
    }

    private SslContextFactory newSslContextFactory() {
        logger.info("Configuring TLS.");
        String keyStorePath = wsProps.getProperty("ws.ssl.keyStorePath");
        String keyStorePassword = wsProps.getProperty("ws.ssl.keyStorePassword");
        String trustStorePath = wsProps.getProperty("ws.ssl.trustStorePath", keyStorePath);
        String trustStorePassword = wsProps.getProperty("ws.ssl.trustStorePassword", keyStorePassword);
        String[] protocols = wsProps.getProperty("ws.ssl.protocols", Constants.DEFAULT_PROTOCOLS).split(",");
        String[] ciphers = wsProps.getProperty("ws.ssl.ciphers", Constants.DEFAULT_CIPHERS).split(",");
        String clientAuth = wsProps.getProperty("ws.ssl.clientAuth", "none");


        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(keyStorePath);
        sslContextFactory.setKeyStorePassword(keyStorePassword);
        sslContextFactory.setKeyManagerPassword(keyStorePassword);
        sslContextFactory.setTrustStorePath(trustStorePath);
        sslContextFactory.setTrustStorePassword(trustStorePassword);
        sslContextFactory.setIncludeProtocols(protocols);
        sslContextFactory.setIncludeCipherSuites(ciphers);
        switch (clientAuth) {
            case "required":
                logger.info("Client auth required.");
                sslContextFactory.setNeedClientAuth(true);
                sslContextFactory.setValidatePeerCerts(true);
                break;
            case "optional":
                logger.info("Client auth allowed.");
                sslContextFactory.setWantClientAuth(true);
                sslContextFactory.setValidatePeerCerts(true);
                break;
            default:
                logger.info("Client auth disabled.");
                sslContextFactory.setNeedClientAuth(false);
                sslContextFactory.setWantClientAuth(false);
                sslContextFactory.setValidatePeerCerts(false);
        }
        return sslContextFactory;
    }

    private ServerConnector newSslServerConnector(Server server) {
        Integer securePort = Integer.parseInt(wsProps.getProperty("ws.ssl.port", Constants.DEFAULT_SSL_PORT));
        HttpConfiguration https = new HttpConfiguration();
        https.setSecureScheme("https");
        https.setSecurePort(securePort);
        https.setOutputBufferSize(Constants.BUFFER_SIZE);
        https.setRequestHeaderSize(Constants.HEADER_SIZE);
        https.setResponseHeaderSize(Constants.HEADER_SIZE);
        https.setSendServerVersion(true);
        https.setSendDateHeader(false);
        https.addCustomizer(new SecureRequestCustomizer());

        SslContextFactory sslContextFactory = newSslContextFactory();
        ServerConnector sslConnector =
                new ServerConnector(server,
                        new SslConnectionFactory(sslContextFactory, "HTTP/1.1"), new HttpConnectionFactory(https));
        sslConnector.setPort(securePort);
        return sslConnector;
    }

    public void run() {
        try {
            Server server = new Server();
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(Integer.parseInt(wsProps.getProperty("ws.port", Constants.DEFAULT_PORT)));
            server.addConnector(connector);

            if (Boolean.parseBoolean(wsProps.getProperty("ws.ssl", "false"))) {
                server.addConnector(newSslServerConnector(server));
            }

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            ServerContainer wsContainer = WebSocketServerContainerInitializer.configureContext(context);
            String inputTransformClassName =
                    wsProps.getProperty("ws.inputTransformClass", "ws.transforms.Transform");
            String outputTransformClassName =
                    wsProps.getProperty("ws.outputTransformClass", "ws.transforms.Transform");

            //TODO Init Producer and Consumer
            wsContainer.addEndpoint(WebsocketEndpoint.class);

            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("Failed to start the server: {}", e.getMessage());
        }
    }
}
