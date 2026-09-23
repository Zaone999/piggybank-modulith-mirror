package be.kdg.piggybankmodulith.choreography;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class ScheduledPiggybankDrip {

    private final RestClient restClient;

    public ScheduledPiggybankDrip() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080") // Adjust base URL to your server port/host
                .build();
    }

    @Scheduled(fixedRate = 10_000)
    void topUp() {
        this.restClient.post()
                .uri("/piggybanks") // Replace with your REST endpoint path
                .contentType(MediaType.APPLICATION_JSON)
                .body(new DepositRequest(UUID.fromString("8c8c87c1-9b5f-4f73-9fa7-7f9b5c7f2296"), BigDecimal.TEN))
                .retrieve()
                .toBodilessEntity();
    }
}
