package com.selenoid.lock.door.controller;

import com.selenoid.lock.door.handler.DoorWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/door")
@RequiredArgsConstructor
public class DoorController {

    private final DoorWebSocketHandler doorWebSocketHandler;

    @PostMapping("/unlock")
    public ResponseEntity<String> unlockDoor() {
        try {
            doorWebSocketHandler.sendUnlockCommand();
            return ResponseEntity.ok("Command sent to ESP32");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to send command: " + e.getMessage());
        }
    }
}

