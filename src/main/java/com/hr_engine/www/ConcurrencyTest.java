package com.hr_engine.www;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CountDownLatch;

public class ConcurrencyTest {

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 5; // simulate 5 users hitting the same room at once
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch doneGate = new CountDownLatch(numThreads);

        String requestBody = """
                {
                    "roomId": 7,
                    "checkInDate": "2027-02-01",
                    "checkOutDate": "2027-02-05",
                    "guestName": "Concurrent Guest",
                    "guestEmail": "concurrent@example.com"
                }
                """;

        HttpClient client = HttpClient.newHttpClient();

        for (int i = 0; i < numThreads; i++) {
            int threadNum = i;
            new Thread(() -> {
                try {
                    startGate.await(); // wait until all threads are released together
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8080/api/reservations/hold"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Thread " + threadNum + " -> Status: " + response.statusCode()
                            + " | Body: " + response.body());
                } catch (Exception e) {
                    System.out.println("Thread " + threadNum + " -> Error: " + e.getMessage());
                } finally {
                    doneGate.countDown();
                }
            }).start();
        }

        Thread.sleep(500); // let all threads reach the gate
        System.out.println("Releasing all threads simultaneously...");
        startGate.countDown(); // fire all 5 requests at the exact same instant

        doneGate.await();
        System.out.println("Test complete.");
    }
    
    
}