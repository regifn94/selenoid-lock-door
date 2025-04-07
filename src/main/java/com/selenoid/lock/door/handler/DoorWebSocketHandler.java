package com.selenoid.lock.door.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class DoorWebSocketHandler extends TextWebSocketHandler {

    private WebSocketSession esp32Session;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.esp32Session = session;
        System.out.println("ESP32 connected: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Message from ESP32: " + message.getPayload());
        // Bisa di-handle jika ESP32 mengirim status dsb
    }

    public void sendUnlockCommand() throws IOException {
        if (esp32Session != null && esp32Session.isOpen()) {
            esp32Session.sendMessage(new TextMessage("UNLOCK"));
        }
    }
}
