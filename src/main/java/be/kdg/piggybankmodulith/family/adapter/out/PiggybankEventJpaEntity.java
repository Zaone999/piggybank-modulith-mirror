package be.kdg.piggybankmodulith.family.adapter.out;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * One row in the event store: one event of one piggybank. Rows are only ever inserted, never updated.
 */
@Entity
@Table(schema = "kdg_family", name = "piggybank_event",
        uniqueConstraints = @UniqueConstraint(columnNames = {"piggybank_id", "version"}))
class PiggybankEventJpaEntity {

    @Id
    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "piggybank_id", nullable = false)
    private UUID piggybankId;

    @Column(nullable = false)
    private UUID owner;

    @Column(nullable = false)
    private int version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PiggybankEventType type;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "point_in_time", nullable = false)
    private LocalDateTime pointInTime;

    protected PiggybankEventJpaEntity() {} // for JPA

    PiggybankEventJpaEntity(UUID eventId, UUID piggybankId, UUID owner, int version,
                            PiggybankEventType type, BigDecimal amount, LocalDateTime pointInTime) {
        this.eventId = eventId;
        this.piggybankId = piggybankId;
        this.owner = owner;
        this.version = version;
        this.type = type;
        this.amount = amount;
        this.pointInTime = pointInTime;
    }

    UUID getEventId() {
        return eventId;
    }

    UUID getPiggybankId() {
        return piggybankId;
    }

    UUID getOwner() {
        return owner;
    }

    int getVersion() {
        return version;
    }

    PiggybankEventType getType() {
        return type;
    }

    BigDecimal getAmount() {
        return amount;
    }

    LocalDateTime getPointInTime() {
        return pointInTime;
    }
}
