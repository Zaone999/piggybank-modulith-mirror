package be.kdg.piggybankmodulith.grandparents.adapter.out;

import be.kdg.piggybankmodulith.grandparents.domain.SavingsEntryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(schema = "kdg_grandparents", name = "savings_entry")
class SavingsEntryJpaEntity {

    @Id
    @Column(name = "event_id")
    private UUID eventId;

    @Column(nullable = false)
    private UUID grandchild;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SavingsEntryType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "point_in_time", nullable = false)
    private LocalDateTime pointInTime;

    protected SavingsEntryJpaEntity() {} // for JPA

    SavingsEntryJpaEntity(UUID eventId, UUID grandchild, SavingsEntryType type, BigDecimal amount,
                          LocalDateTime pointInTime) {
        this.eventId = eventId;
        this.grandchild = grandchild;
        this.type = type;
        this.amount = amount;
        this.pointInTime = pointInTime;
    }
}
