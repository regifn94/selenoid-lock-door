#include <WiFi.h>
#include <WebSocketsClient.h>

const char* ssid = "joel";
const char* password = "Hana@2022";

// Ganti IP dan port sesuai alamat server WebSocket
const char* websocket_host = "192.168.78.164";
const int websocket_port = 8080;
const char* websocket_path = "/ws/door";

#define RELAY_PIN 16

WebSocketsClient webSocket;

void setup() {
  Serial.begin(115200);
  pinMode(RELAY_PIN, OUTPUT);
  digitalWrite(RELAY_PIN, LOW); // Default = terkunci

  // Koneksi WiFi
  WiFi.begin(ssid, password);
  Serial.println("Connecting to WiFi...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  Serial.println();
  Serial.println("Connected to WiFi");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());

  // Koneksi WebSocket
  webSocket.begin(websocket_host, websocket_port, websocket_path);
  webSocket.onEvent(webSocketEvent);
  webSocket.setReconnectInterval(5000);
}

void loop() {
  webSocket.loop();
}

// Fungsi untuk mengecek status relay saat ini
String getSolenoidStatus() {
  int relayState = digitalRead(RELAY_PIN); // Baca status pin
  if (relayState == HIGH) {
    return "UNLOCKED";
  } else {
    return "LOCKED";
  }
}

// Kirim status ke server frontend/backend
void sendStatus() {
  String status = getSolenoidStatus();
  String jsonStatus = "{\"status\":\"" + status + "\"}";
  webSocket.sendTXT(jsonStatus);
  Serial.println("Sent status: " + status);
}

void webSocketEvent(WStype_t type, uint8_t * payload, size_t length) {
  switch (type) {
    case WStype_DISCONNECTED:
      Serial.println("[WebSocket] Disconnected");
      break;

    case WStype_CONNECTED:
      Serial.println("[WebSocket] Connected to server");
      sendStatus(); // Kirim status saat pertama terhubung
      break;

    case WStype_TEXT: {
      String message = String((char*)payload);
      Serial.printf("[WebSocket] Message received: %s\n", payload);

      if (message == "UNLOCK") {
        Serial.println("Command: UNLOCK → Opening lock...");
        digitalWrite(RELAY_PIN, HIGH);  // Relay ON
        delay(100); // optional: debounce
        sendStatus();
      } else if (message == "LOCK") {
        Serial.println("Command: LOCK → Locking door...");
        digitalWrite(RELAY_PIN, LOW);   // Relay OFF
        delay(100); // optional: debounce
        sendStatus();
      } else if (message == "STATUS") {
        Serial.println("Command: STATUS → Sending current solenoid status...");
        sendStatus();
      }
      break;
    }
  }
}
