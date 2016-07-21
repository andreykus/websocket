package com.bivgroup.websocket.websocket;

/**
 * Created by bush on 21.07.2016.
 */
public interface Constants {
    static final String DEFAULT_PORT = "8080";
    static final String DEFAULT_SSL_PORT = "8443";
    static final String DEFAULT_PROTOCOLS = "TLSv1.2";
    static final String DEFAULT_CIPHERS = "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_RC4_128_SHA,TLS_RSA_WITH_AES_256_CBC_SHA";
    static final Integer BUFFER_SIZE = 32768;
    static final Integer HEADER_SIZE = 8192;

    static final String  API_CONTEXT = "/api/broker/";
}
