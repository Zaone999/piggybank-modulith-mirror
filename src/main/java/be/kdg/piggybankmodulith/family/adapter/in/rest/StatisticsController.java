package be.kdg.piggybankmodulith.family.adapter.in.rest;

import be.kdg.piggybankmodulith.family.port.in.BestMonthQuery;
import be.kdg.piggybankmodulith.family.port.in.BiggestDonationQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * The query side: answers from the read models, never from the piggybank itself.
 */
@RestController
@RequestMapping("/piggybanks/{ownerId}")
public class StatisticsController {

    private final BestMonthQuery bestMonthQuery;
    private final BiggestDonationQuery biggestDonationQuery;

    public StatisticsController(BestMonthQuery bestMonthQuery, BiggestDonationQuery biggestDonationQuery) {
        this.bestMonthQuery = bestMonthQuery;
        this.biggestDonationQuery = biggestDonationQuery;
    }

    @GetMapping("/best-month")
    public ResponseEntity<BestMonthResponse> bestMonth(@PathVariable UUID ownerId) {
        return ResponseEntity.of(bestMonthQuery.bestMonth(ownerId).map(BestMonthResponse::from));
    }

    @GetMapping("/biggest-donation")
    public ResponseEntity<BiggestDonationResponse> biggestDonation(@PathVariable UUID ownerId) {
        return ResponseEntity.of(biggestDonationQuery.biggestDonation(ownerId).map(BiggestDonationResponse::from));
    }
}
