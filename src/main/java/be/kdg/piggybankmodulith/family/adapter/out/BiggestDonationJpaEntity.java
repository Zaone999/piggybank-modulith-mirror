package be.kdg.piggybankmodulith.family.adapter.out;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Read model: one row per piggybank, its biggest deposit.
 */
@Entity
@Table(schema = "kdg_family", name = "biggest_donation")
class BiggestDonationJpaEntity {

    @Id
    private UUID owner;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "point_in_time", nullable = false)
    private LocalDateTime pointInTime;

    protected BiggestDonationJpaEntity() {} // for JPA

    BiggestDonationJpaEntity(UUID owner, BigDecimal amount, LocalDateTime pointInTime) {
        this.owner = owner;
        this.amount = amount;
        this.pointInTime = pointInTime;
    }

    UUID getOwner() {
        return owner;
    }

    BigDecimal getAmount() {
        return amount;
    }

    LocalDateTime getPointInTime() {
        return pointInTime;
    }
}
