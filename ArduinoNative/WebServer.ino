#include <SPI.h>
#include <Ethernet.h>

byte mac[] = { 
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress ip(192,168,11,177);

EthernetServer server(80);
char startReading = 0;
char req;

void setup() {
  Ethernet.begin(mac, ip);
  server.begin();

  //*** two pin means only one wheel, specify two more pins to run a robot. *****//
  pinMode (5, OUTPUT);
  pinMode (6, OUTPUT);
}


void loop() {
  EthernetClient client = server.available();
  if (client) {
    boolean currentLineIsBlank = true;
    while (client.connected()) {
      if (client.available()) {
        char c = client.read();
        
        // 192.168.11.177/?a=1 type ko command send garnu parcha,
        if (startReading) {
           startReading = 0;
           if (c == '1'){
             //digitalWRite other pins as well if you want to run forward backward and other functions *****//
             digitalWrite (5, HIGH);
             digitalWrite (6, LOW);
             digitalWrite (2, LOW);
             digitalWrite (3, LOW);
           } else if (c == '2'){
             digitalWrite (5, LOW);
             digitalWrite (6, HIGH);
            digitalWrite (2, LOW);
             digitalWrite (3, LOW); 
           } else if (c == '3'){
             digitalWrite (5, LOW);
             digitalWrite (6, LOW);
            digitalWrite (2, HIGH);
             digitalWrite (3, LOW); 
           } else if (c == '4'){
             digitalWrite (5, LOW);
             digitalWrite (6, LOW);
            digitalWrite (2, LOW);
             digitalWrite (3, HIGH); 
           }
           req = c;
        }
        if (c == '='){
          startReading = 1;
        }
        if (c == '\n' && currentLineIsBlank) {
          client.println("HTTP/1.1 200 OK");
          client.println("Content-Type: text/html");
          client.println("Connection: close"); 
	  client.println("Refresh: 5");  
          client.println();
          client.println("<!DOCTYPE HTML>");
          client.println("<html>");
          client.print("Request is ");
          client.println(req);
          client.print("<br/>Input status is<br/>");
          for (int analogChannel = 0; analogChannel < 6; analogChannel++) {
            int sensorReading = analogRead(analogChannel);
            client.print("analog input ");
            client.print(analogChannel);
            client.print(" is ");
            client.print(sensorReading);
            client.println("<br />");       
          }
          client.println("</html>");
          break;
        }
        if (c == '\n') {
          currentLineIsBlank = true;
        } 
        else if (c != '\r') {
          currentLineIsBlank = false;
        }
      }
    }
    delay(10);
    client.stop();
    Serial.println("client disonnected");
  }
  
}

