

package com.bivgroup.websocket.websocket.jetty;

import org.apache.logging.log4j.LogManager;

import javax.websocket.*;
import java.nio.charset.Charset;


public class TextMessage extends AbstractMessage {
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(TextMessage.class);

    private String key = "";
    private String message;

    public TextMessage(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }

    public TextMessage(String topic, String key, String message) {
        this.topic = topic;
        this.key = key;
        this.message = message;
    }

    @Override
    public Boolean isKeyed() {
        return !key.isEmpty();
    }

    @Override
    public byte[] getMessageBytes() {
        return message.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    static public class TextMessageDecoder implements Decoder.Text<TextMessage> {


        public TextMessageDecoder() {
        }

        @Override
        public TextMessage decode(String s) throws DecodeException {
                    return new TextMessage("", s);
        }

        @Override
        public boolean willDecode(String s) {
            return true;
        }

        @Override
        public void init(EndpointConfig endpointConfig) {

        }

        @Override
        public void destroy() {

        }
    }

    static public class TextMessageEncoder implements Encoder.Text<TextMessage> {
        public TextMessageEncoder() {

        }

        @Override
        public String encode(TextMessage textMessage) throws EncodeException {
            return textMessage.getMessage();
        }

        @Override
        public void init(EndpointConfig endpointConfig) {

        }

        @Override
        public void destroy() {

        }
    }
}
