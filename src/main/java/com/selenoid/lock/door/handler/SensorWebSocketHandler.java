//package com.selenoid.lock.door.handler;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class SensorWebSocketHandler extends TextWebSocketHandler {
//    private static final Set<WebSocketSession> clients =
//            Collections.newSetFromMap(new ConcurrentHashMap<>());
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) {
//        clients.add(session);
//        System.out.println("Client terhubung: " + session.getId());
//    }
//
//
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        System.out.println("Data diterima: " + message.getPayload());
//        for (WebSocketSession s : clients) {
//            if (s.isOpen()) {
//                s.sendMessage(new TextMessage("Suhu: " + message.getPayload()));
//            }
//        }
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//        clients.remove(session);
//        System.out.println("Client terputus: " + session.getId());
//    }
//
//    public void sendDataToClients(String data) {
//        for (WebSocketSession client : clients) {
//            if (client.isOpen()) {
//                try {
//                    System.out.println("server : " + data);
//                    client.sendMessage(new TextMessage("server : " + data));
//                    System.out.println("📤 Sent to ESP8266: " + data);
//                } catch (IOException e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//        }
//    }
//}